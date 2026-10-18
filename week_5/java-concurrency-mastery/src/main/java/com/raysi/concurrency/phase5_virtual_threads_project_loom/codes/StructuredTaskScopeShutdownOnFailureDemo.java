package com.raysi.concurrency.phase5_virtual_threads_project_loom.codes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Author: raysi
 * Demonstrates:
 * 1. Java 21+ Structured Concurrency (JEP 453): Eliminating Thread Leaks and Orphan Tasks.
 * 2. StructuredTaskScope.ShutdownOnFailure: If any subtask fails, cancel all running siblings immediately!
 * 3. StructuredTaskScope.ShutdownOnSuccess: First successful response wins; cancel slower redundant queries.
 */
public class StructuredTaskScopeShutdownOnFailureDemo {

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("  PHASE 5 LAB: STRUCTURED CONCURRENCY (STRUCTURED-TASK-SCOPE)");
        System.out.println("  Author: raysi | Package: com.raysi.concurrency");
        System.out.println("======================================================================\n");

        System.out.println("=== PART 1: StructuredTaskScope.ShutdownOnFailure (All Must Succeed) ===");
        try {
            OrderResponse order = fetchOrderDetails("order-456");
            System.out.println("Successfully compiled order: " + order);
        } catch (Exception e) {
            System.err.println("Order retrieval failed: " + e.getMessage());
        }

        System.out.println("\n=== PART 2: Fast-Fail Cancellation (Automatic Sibling Cancellation) ===");
        try {
            fetchOrderWithFailingInventory("order-789");
        } catch (Exception e) {
            System.out.println("[Handled Error] StructuredTaskScope cleanly aborted child tasks upon failure: " + e.getMessage());
        }

        System.out.println("\n=== PART 3: StructuredTaskScope.ShutdownOnSuccess (Race / First-to-Win) ===");
        try {
            String quote = fetchFastestWeatherQuote("Bengaluru");
            System.out.println("Fastest winning quote: " + quote);
        } catch (Exception e) {
            System.err.println("Failed to fetch weather: " + e.getMessage());
        }

        System.out.println("\n[Key Takeaway for Interviews]:");
        System.out.println("  - Structured Concurrency treats multi-threaded execution like single-threaded blocks;");
        System.out.println("    lifecycles of concurrent subtasks are strictly bounded by try-with-resources.");
        System.out.println("  - Eliminates orphan thread leaks: If subtask A throws an exception, ShutdownOnFailure");
        System.out.println("    immediately cancels subtasks B and C automatically.");
        System.out.println("  - ShutdownOnSuccess facilitates speculative parallelism (e.g. querying redundant replica servers).");
    }

    // 1. All-or-nothing task aggregation
    private static OrderResponse fetchOrderDetails(String orderId) throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> customerTask = scope.fork(() -> queryCustomerService(orderId));
            Subtask<String> paymentTask = scope.fork(() -> queryPaymentGateway(orderId));

            // Wait for both subtasks to complete (or first failure)
            scope.join();
            scope.throwIfFailed(); // Throws ExecutionException if any subtask failed

            return new OrderResponse(customerTask.get(), paymentTask.get());
        }
    }

    // 2. Demonstrates fast-fail sibling cancellation
    private static void fetchOrderWithFailingInventory(String orderId) throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> {
                Thread.sleep(500); // Slow long-running task
                System.out.println("  [SlowTask] Still running (should be canceled early!)...");
                return "Slow Result";
            });

            scope.fork(() -> {
                Thread.sleep(50);
                throw new IllegalStateException("Inventory out of stock for ID: " + orderId);
            });

            scope.join();
            scope.throwIfFailed();
        }
    }

    // 3. Speculative redundancy query
    private static String fetchFastestWeatherQuote(String city) throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> {
                Thread.sleep(120);
                return "Server A (OpenWeather): 24°C in " + city;
            });
            scope.fork(() -> {
                Thread.sleep(40); // Fastest
                return "Server B (AccuWeather): 24°C in " + city;
            });
            scope.fork(() -> {
                Thread.sleep(200);
                return "Server C (WeatherGov): 24°C in " + city;
            });

            scope.join(); // Waits until the first subtask succeeds
            return scope.result(); // Returns first successful result; cancels other subtasks automatically!
        }
    }

    private static String queryCustomerService(String orderId) throws InterruptedException {
        Thread.sleep(60);
        return "Customer: John Smith";
    }

    private static String queryPaymentGateway(String orderId) throws InterruptedException {
        Thread.sleep(80);
        return "Payment: Captured $199.99";
    }

    record OrderResponse(String customer, String payment) {}
}
