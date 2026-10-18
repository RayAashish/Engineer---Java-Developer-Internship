# Java Multithreading, JMM & Virtual Threads Mastery
> **Author:** raysi | Java Memory Model, CAS Atomics, AQS Locks, ThreadPoolExecutor, CompletableFuture & Java 21+ Virtual Threads

---

## 🎯 Architectural Mission & Junior-to-Senior Progression

Multithreading is often cited as the most difficult domain in software engineering because it transforms deterministic, single-threaded execution into non-deterministic, concurrent interleaved execution governed by CPU hardware caches, kernel context switches, and out-of-order instruction reordering.

This repository serves as an exhaustive, production-grade master curriculum designed to eliminate multithreading confusion entirely. It bridges the conceptual gap across three distinct career tiers:

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                                 CAREER PROGRESSION SPECTRUM                            │
├────────────────────────────┬─────────────────────────────┬─────────────────────────────┤
│   JUNIOR LEVEL MENTALITY   │     MID-LEVEL DEVELOPER     │    SENIOR / STAFF ARCHITECT │
├────────────────────────────┼─────────────────────────────┼─────────────────────────────┤
│ • "Extend Thread or        │ • Uses ThreadPoolExecutor   │ • Reason about MESI cache   │
│    implement Runnable"     │   instead of manual threads │   coherence & StoreLoad     │
│ • Slaps 'synchronized' on  │ • Understands Race Condition│   memory barriers           │
│    every method            │   and Deadlock conditions   │ • Designs lock-free CAS     │
│ • Confuses sleep() with    │ • Leverages AtomicInteger   │   algorithms & LongAdder    │
│    wait()                  │   and ReentrantLock         │ • Analyzes AQS CLH queues   │
│ • Thinks volatile makes    │ • Uses CompletableFuture    │ • Sizes thread pools via    │
│    count++ thread-safe     │   for async composition     │   Little's Law & Wait-Ratios│
│ • Unaware of OS kernel     │ • Profiles memory leaks     │ • Migrates to Java 21+      │
│    context switches        │   in ThreadLocal            │   Virtual Threads & Loom    │
└────────────────────────────┴─────────────────────────────┴─────────────────────────────┘
```

---

## 🧠 The 4-Layer Concurrency Decision Matrix

When asked *"How do you make this thread-safe?"*, never blindly slap `synchronized` on a method. Pick the right tool for the contention profile:

| Problem / Scenario | Best Tool to Use | Why It Wins Under the Hood |
| :--- | :--- | :--- |
| **1 Thread writes a flag, N Threads read it** | `volatile boolean` | Flushes CPU L1/L2 cache directly to Main RAM via a Memory Barrier (Zero locking overhead). |
| **Simple counter (`count++`) under medium load** | `AtomicInteger` / `AtomicLong` | Uses lock-free CPU hardware instruction **CAS (`cmpxchg`)** instead of suspending threads. |
| **Counter under extreme contention (64+ threads)** | `LongAdder` | Splits the counter into an array of `Cell[]` variables to avoid CAS retry storms, summing them on `.sum()`. |
| **95% Reads, 5% Writes on shared state** | `ReentrantReadWriteLock` or `StampedLock` | Allows unlimited concurrent readers while only locking exclusively during writes. |
| **Complex state invariant with timeout/fairness** | `ReentrantLock` | Supports `tryLock(timeout)`, interruptible lock waits, and multiple `Condition` queues. |
| **10,000+ Concurrent Blocking I/O Calls (DB/HTTP)** | **Java 21 Virtual Threads** (`Executors.newVirtualThreadPerTaskExecutor()`) | Unmounts blocked virtual threads from OS Carrier Threads automatically—zero thread-pool starvation! |

---

## 📐 How to Size a Classic `ThreadPoolExecutor` in Production

Never use `Executors.newFixedThreadPool()` or `newCachedThreadPool()` blindly in production (`newFixedThreadPool` uses an unbounded `LinkedBlockingQueue` of size `Integer.MAX_VALUE`, which causes `OutOfMemoryError` if downstream APIs slow down!). Always construct `new ThreadPoolExecutor(...)` with a bounded `ArrayBlockingQueue` and calculate core size using:

1. **For CPU-Bound Tasks (Hashing, Video Encoding, Sorting, JSON Parsing):**
   $$N_{\text{threads}} = N_{\text{CPU Cores}} + 1$$
   *(The $+1$ handles an occasional page fault or OS context switch without idling the physical core).*

2. **For I/O-Bound Tasks on Classic Platform Threads (DB Queries, REST Calls, Disk I/O):**
   $$N_{\text{threads}} = N_{\text{CPU Cores}} \times \left(1 + \frac{\text{Wait Time}}{\text{Compute Time}}\right)$$
   *Example:* If a DB call takes 95ms of network latency (Wait Time) and 5ms of CPU serialization (Compute Time) on an 8-core CPU:
   $$N_{\text{threads}} = 8 \times \left(1 + \frac{95}{5}\right) = 8 \times (1 + 19) = 160 \text{ Platform Threads}$$

3. **In Java 21+ Virtual Threads:**
   **Never pool Virtual Threads!** Create a new Virtual Thread per task and use a `Semaphore` or bounded connection pool to rate-limit downstream database/API connections.

---

## 📂 Repository Blueprint & Chronological Roadmap

```
java-concurrency-mastery/
├── pom.xml                                              # Java 21+ Maven Build with --enable-preview
├── README.md                                            # Master Concurrency Cheat Sheet & Sizing Formula
└── src/
    └── main/
        └── java/
            └── com/
                └── raysi/
                    └── concurrency/
                        ├── phase1_threads_and_memory_model/     # OS Threads, Lifecycle & The JMM
                        │   ├── codes/
                        │   │   ├── ThreadStatesLifecycleInspector.java
                        │   │   ├── VolatileCpuCacheVisibilityBugDemo.java
                        │   │   └── DaemonThreadAndGracefulInterruptDemo.java
                        │   └── notes/
                        │       ├── 01-Thread-Lifecycle-OS-Kernel-Mapping.md
                        │       └── 02-Java-Memory-Model-Volatile-And-Happens-Before.md
                        │
                        ├── phase2_synchronization_and_locks/    # Intrinsic Monitors vs Explicit Locks (AQS)
                        │   ├── codes/
                        │   │   ├── ObjectHeaderMarkWordMonitorDemo.java
                        │   │   ├── ProducerConsumerWaitNotifyVsCondition.java
                        │   │   ├── ReentrantReadWriteAndStampedLockDemo.java
                        │   │   └── DeadlockDetectionAndPreventionDemo.java
                        │   └── notes/
                        │       ├── 01-Synchronized-Object-Monitor-And-Lock-Escalation.md
                        │       ├── 02-ReentrantLock-AQS-And-ReadWriteLocks.md
                        │       └── 03-Deadlock-Livelock-Starvation-And-ThreadDumps.md
                        │
                        ├── phase3_lockfree_atomics_and_synchronizers/ # CAS Hardware Primitives & Coordination
                        │   ├── codes/
                        │   │   ├── AtomicCASVsSynchronizedBenchmark.java
                        │   │   ├── LongAdderHighContentionCounterDemo.java
                        │   │   ├── CountDownLatchCyclicBarrierSemaphoreDemo.java
                        │   │   └── ThreadLocalMemoryLeakInThreadPoolDemo.java
                        │   └── notes/
                        │       ├── 01-CompareAndSwap-CAS-Atomics-And-ABA-Problem.md
                        │       ├── 02-CountDownLatch-Vs-CyclicBarrier-Vs-Semaphore.md
                        │       └── 03-ThreadLocal-Internals-And-Memory-Leaks.md
                        │
                        ├── phase4_executors_and_completablefuture/    # Production Thread Pools & Async Pipelines
                        │   ├── codes/
                        │   │   ├── CustomThreadPoolExecutorRejectionDemo.java
                        │   │   ├── ForkJoinPoolWorkStealingDemo.java
                        │   │   └── CompletableFutureMicroserviceAggregator.java
                        │   └── notes/
                        │       ├── 01-ThreadPoolExecutor-Internals-And-Sizing-Math.md
                        │       └── 02-CompletableFuture-Async-Pipelines-And-Exception-Handling.md
                        │
                        └── phase5_virtual_threads_project_loom/       # Modern Java 21+: 1M Threads & Pinning
                            ├── codes/
                            │   ├── PlatformVsVirtualThreads100kBenchmark.java
                            │   ├── CarrierThreadMountingAndPinningTrapDemo.java
                            │   └── StructuredTaskScopeShutdownOnFailureDemo.java
                            └── notes/
                                ├── 01-Virtual-Threads-Continuations-And-Carrier-Mounting.md
                                └── 02-Thread-Pinning-And-Structured-Concurrency.md
```

---

## 🛠️ Compilation & Execution Guide

All modules are written in standard Java 21 with preview features enabled for Java 21 Structured Concurrency.

### Build the entire repository:
```bash
mvn clean compile
```

### Run individual interactive labs:
```bash
# Phase 1: CPU Cache Visibility & Volatile
java --enable-preview -cp target/classes com.raysi.concurrency.phase1_threads_and_memory_model.codes.VolatileCpuCacheVisibilityBugDemo

# Phase 2: Object Monitor & Lock Escalation
java --enable-preview -cp target/classes com.raysi.concurrency.phase2_synchronization_and_locks.codes.ObjectHeaderMarkWordMonitorDemo

# Phase 3: CAS Atomics vs Synchronized Benchmark
java --enable-preview -cp target/classes com.raysi.concurrency.phase3_lockfree_atomics_and_synchronizers.codes.AtomicCASVsSynchronizedBenchmark

# Phase 4: ThreadPoolExecutor Saturation & Rejection Handlers
java --enable-preview -cp target/classes com.raysi.concurrency.phase4_executors_and_completablefuture.codes.CustomThreadPoolExecutorRejectionDemo

# Phase 5: 100,000 Concurrent Threads Benchmark (Platform vs Virtual)
java --enable-preview -cp target/classes com.raysi.concurrency.phase5_virtual_threads_project_loom.codes.PlatformVsVirtualThreads100kBenchmark
```
