# Java Exception Handling & JVM Internals Mastery
> **Author:** raysi | Throwable Hierarchy, JVM Exception Tables, Suppressed Exceptions, Overriding Contracts & Production Architecture

---

## 🌳 The `java.lang.Throwable` Architecture

```text
                        ┌──────────────────────┐
                        │  java.lang.Object    │
                        └──────────┬───────────┘
                                   │
                        ┌──────────▼───────────┐
                        │ java.lang.Throwable  │ (Root of all errors & exceptions)
                        └──────────┬───────────┘
                 ┌─────────────────┴─────────────────┐
                 │                                   │
      ┌──────────▼───────────┐            ┌──────────▼───────────┐
      │   java.lang.Error    │            │ java.lang.Exception  │
      │ (JVM / Hardware)     │            │ (Application Level)  │
      │ [UNCHECKED]          │            └──────────┬───────────┘
      └──────────┬───────────┘                       │
                 │                        ┌──────────┴───────────────────┐
   • OutOfMemoryError                     │                              │
   • StackOverflowError        ┌──────────▼───────────┐       ┌──────────▼───────────┐
   • NoClassDefFoundError      │   Checked Exceptions │       │   RuntimeException   │
   • VirtualMachineError       │ (Compile-Time Check) │       │     [UNCHECKED]      │
                               └──────────┬───────────┘       └──────────┬───────────┘
                                          │                              │
                                • IOException                  • NullPointerException
                                • SQLException                 • IllegalArgumentException
                                • ClassNotFoundException       • IndexOutOfBoundsException
                                • InterruptedException         • ConcurrentModificationException
```

---

## ⚠️ 5 Production Golden Rules (What Interviewers Look For)

1. **Never put a `return` or `throw` inside a `finally` block:**  
   A `return` statement inside `finally` silently hijacks and destroys any exception thrown inside the `try` or `catch` block, swallowing fatal bugs in production and breaking the JVM operand stack guarantee.
2. **Never swallow exceptions (`catch (Exception e) {}`):**  
   At minimum, log the exception with its full stack trace (`log.error("Context msg", e)`). If catching `InterruptedException`, always restore the interrupt flag: `Thread.currentThread().interrupt();` to allow higher-level schedulers to terminate gracefully.
3. **Always preserve the Root Cause (Exception Chaining):**  
   When catching a low-level exception (`SQLException`) and translating it to a custom domain exception (`OrderProcessingException`), ALWAYS pass the original exception `e` into the constructor (`super(message, e)`) so the root cause stack trace and line numbers are preserved.
4. **Understand Method Overriding Rules (Liskov Substitution Principle):**  
   - If a parent method declares a Checked Exception, the child overridden method can declare the same exception, a narrower subclass, or **NO exception at all**—it can **NEVER** declare a broader Checked Exception (`Exception`).
   - For Unchecked Exceptions (`RuntimeException`), the child method has zero restrictions.
5. **Why Creating Exceptions is Expensive ($O(\text{Stack Depth})$):**  
   Calling `new Exception()` invokes the native method `Throwable.fillInStackTrace()`, which halts execution and walks the entire JVM thread call stack to capture class names, method names, bytecode indices (BCI), and source line numbers. **Never use exceptions for normal `if-else` business control flow.** For high-throughput scenarios where an exception is strictly used for control signaling, override `fillInStackTrace()` to make it a fast, stackless exception.

---

## 🧭 Repository Blueprint & Navigation

This repository is structured into 5 evolutionary phases, guiding you from low-level HotSpot bytecode mechanics to enterprise-grade Spring Boot domain architectures:

```text
java-exception-handling-mastery/
├── pom.xml                                              # Java 21 Maven Build Configuration
├── README.md                                            # Master Hierarchy Diagram & Production Anti-Patterns
└── src/
    └── main/
        └── java/
            └── com/
                └── raysi/
                    └── exceptions/
                        ├── phase1_hierarchy_and_jvm_internals/  # How the JVM actually creates & unwinds exceptions
                        │   ├── codes/
                        │   │   ├── CheckedVsUncheckedCompilerDemo.java
                        │   │   ├── StackUnwindingAndCallStackDemo.java
                        │   │   └── FillInStackTracePerformanceBenchmark.java
                        │   └── notes/
                        │       ├── 01-Throwable-Hierarchy-Error-Vs-Exception.md
                        │       └── 02-JVM-Exception-Table-And-Stack-Unwinding.md
                        │
                        ├── phase2_control_flow_and_traps/       # The Classic Interview Output Puzzles
                        │   ├── codes/
                        │   │   ├── TryCatchFinallyReturnOverrideTrap.java
                        │   │   ├── WhenFinallyDoesNotExecuteDemo.java
                        │   │   └── MultiCatchAndExceptionPrecisionDemo.java
                        │   └── notes/
                        │       └── 01-Try-Catch-Finally-Bytecode-And-Return-Traps.md
                        │
                        ├── phase3_autocloseable_and_suppressed/ # Try-With-Resources & Memory Leak Prevention
                        │   ├── codes/
                        │   │   ├── CustomAutoCloseableResourceDemo.java
                        │   │   └── SuppressedExceptionsRecoveryDemo.java
                        │   └── notes/
                        │       └── 01-TryWithResources-And-Suppressed-Exceptions.md
                        │
                        ├── phase4_oop_inheritance_and_lambdas/  # Method Overriding Rules & Functional Interfaces
                        │   ├── codes/
                        │   │   ├── OverridingExceptionRulesCovariantDemo.java
                        │   │   └── CheckedExceptionsInStreamsWrapperDemo.java
                        │   └── notes/
                        │       ├── 01-Exception-Rules-In-Method-Overriding-Liskov.md
                        │       └── 02-Handling-Checked-Exceptions-In-Java8-Streams.md
                        │
                        └── phase5_production_custom_exceptions/ # How Senior Backend Engineers Design Exceptions
                            ├── codes/
                            │   ├── DomainExceptionChainingAndErrorCodeDemo.java
                            │   └── StacklessFastControlFlowExceptionDemo.java
                            └── notes/
                                ├── 01-Custom-Exceptions-Chaining-And-Root-Cause.md
                                └── 02-Production-AntiPatterns-And-REST-Error-Design.md
```

---

## 📊 Summary of Phases & Core Deep Dives

| Phase | Core Focus | JVM / Bytecode Internals | Interview Puzzles & Labs |
|---|---|---|---|
| **Phase 1: Hierarchy & JVM Internals** | `Throwable`, `Error`, `Exception`, `RuntimeException` | JNI `fillInStackTrace()`, Stack frame unwinding, Zero-cost exception tables | Benchmark: Stack trace cost vs normal return; Checked vs unchecked compiler verification |
| **Phase 2: Control Flow & Traps** | `try`, `catch`, `finally`, multi-catch unions | Exception table ranges (`from`, `to`, `target`), `athrow`, `astore`, JVM termination | The `finally return` trap; `System.exit()` / `OOM` halting finally; Disjoint union types |
| **Phase 3: AutoCloseable & Suppressed** | `AutoCloseable`, `Closeable`, Try-with-resources | Desugaring to nested synthetic try-finally, `Throwable.addSuppressed()`, resource leak prevention | Dual exception collision: Try exception vs `close()` exception recovery |
| **Phase 4: OOP Inheritance & Lambdas** | Liskov Substitution Principle (LSP), Java 8 Streams | Covariant return types vs contravariant exception types, Functional interface contracts | Throwing checked exceptions inside `Stream.map()`; Generic sneaky throws / uncheck wrappers |
| **Phase 5: Production Architecture** | Domain modeling, Global REST handlers, Observability | Synthetic stackless exceptions (`fillInStackTrace() = this`), RFC 7807 Problem Details | Error code taxonomy; Preserving root causes in distributed microservices |

---

## 🛠️ Compilation and Execution Instructions

All code in this repository is written in modern Java (JDK 17/21+) and organized cleanly by package.

To compile all modules from the repository root:
```bash
mvn clean compile
```

Or run any individual demonstration with standard `javac` and `java`:
```bash
# Example: Phase 1 Performance Benchmark
javac -d target/classes src/main/java/com/raysi/exceptions/phase1_hierarchy_and_jvm_internals/codes/FillInStackTracePerformanceBenchmark.java
java -cp target/classes com.raysi.exceptions.phase1_hierarchy_and_jvm_internals.codes.FillInStackTracePerformanceBenchmark

# Example: Phase 3 Suppressed Exceptions Recovery
javac -d target/classes src/main/java/com/raysi/exceptions/phase3_autocloseable_and_suppressed/codes/SuppressedExceptionsRecoveryDemo.java
java -cp target/classes com.raysi.exceptions.phase3_autocloseable_and_suppressed.codes.SuppressedExceptionsRecoveryDemo
```
