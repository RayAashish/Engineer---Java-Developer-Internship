# Java IO, NIO & NIO.2 Internals Mastery
> **Author:** raysi | Streams, Decorator Pattern, Off-Heap Direct Buffers, OS Zero-Copy & Non-Blocking Multiplexing  
> **Target Level:** Senior / Staff / Principal Backend & Distributed Systems Engineers  
> **JDK Versions:** JDK 17 / JDK 21 LTS  

---

## 🧠 The Core Architectural Shift: Why Does Java Have Both `io` and `nio`?

Every file read or network call requires crossing the boundary between **User Space (JVM Heap)** and **Kernel Space (Linux OS Page Cache)**.

| Feature | `java.io` (Classic IO) | `java.nio` (New IO) | `java.nio.file` (NIO.2) |
| :--- | :--- | :--- | :--- |
| **Mental Model** | **Stream of Bytes** (Water pipe: 1 byte at a time) | **Blocks & Buffers** (Cargo train: chunks of memory) | **Modern File System API** (`Path`, `Files`) |
| **Blocking Behavior** | **Blocking** (1 Thread = 1 Connection) | **Non-Blocking** (1 Selector Thread = 10,000 Connections) | Mostly Synchronous Helpers & Lazy Streams |
| **Data Direction** | Unidirectional (`InputStream` OR `OutputStream`) | Bidirectional (`Channel` reads AND writes) | File/Directory Operations |
| **Memory Location** | Copies from OS Kernel -> Native Buffer -> JVM Heap | Supports **DirectBuffer** (Off-Heap Native Memory) | Uses NIO Channels under the hood |
| **Best Use Case** | Simple configs, small files, CP `BufferedReader` | High-throughput servers (Kafka, Netty), 10GB+ files | 99% of everyday file/directory manipulation |

---

## ⚡ 4 Production Golden Rules for Java I/O

1. **Never use `FileInputStream.read()` without a Buffer:**  
   Every unbuffered `.read()` triggers a costly Linux OS system call (`read()` syscall). Wrapping it in `BufferedInputStream` (8KB default array) cuts 1,000,000 syscalls down to ~125.
2. **Never use `java.io.File` in modern code:**  
   It swallows exceptions (returning a silent `false` when `file.delete()` fails) and scales poorly on large directories. Always use `java.nio.file.Path` and `Files`.
3. **Never use `Files.readAllLines()` on large files:**  
   It loads the entire file into a `List<String>` in the JVM Heap, triggering `OutOfMemoryError`. Always use `Files.lines(path)` inside a `try-with-resources` block to stream lines lazily.
4. **Understand the `InputStreamReader` Bridge:**  
   When somebody asks how to convert a raw `InputStream` (bytes) into a `Reader` (characters), `InputStreamReader` is the exact GoF **Adapter Pattern** bridge where you must explicitly pass `StandardCharsets.UTF_8`.

---

## 📁 Repository Architecture & Deep-Dive Map

```
java-io-nio-mastery/
├── README.md                                            # Master Architecture & Decision Matrix
└── src/
    └── main/
        └── java/
            └── com/
                └── raysi/
                    └── ionio/
                        ├── phase1_classic_io_streams/   # java.io: Blocking, Stream-Oriented & Decorator Pattern
                        │   ├── bytestreams/             # Raw 8-bit binary data (Images, PDFs, Raw Files)
                        │   │   ├── codes/
                        │   │   │   ├── UnbufferedVsBufferedCopyBenchmark.java
                        │   │   │   └── TryWithResourcesAutoCloseDemo.java
                        │   │   └── notes/
                        │   │       └── 01-InputStream-OutputStream-And-OS-Syscalls.md
                        │   │
                        │   ├── charstreams/             # 16-bit Unicode Text & Character Encodings (UTF-8)
                        │   │   ├── codes/
                        │   │   │   ├── InputStreamReaderBridgeDemo.java
                        │   │   │   └── BufferedReaderFastCPInputTemplate.java
                        │   │   └── notes/
                        │   │       └── 02-Readers-Writers-And-Decorator-Pattern.md
                        │   │
                        │   └── serialization/           # Object-to-Byte conversion & Security Pitfalls
                        │       ├── codes/
                        │       │   ├── CustomSerializationTransientDemo.java
                        │       │   └── ExternalizableVsSerializableDemo.java
                        │       └── notes/
                        │           └── 03-Java-Serialization-SerialVersionUID-And-Traps.md
                        │
                        ├── phase2_modern_nio2_files/    # java.nio.file (JDK 7+): The Modern Replacement for java.io.File
                        │   ├── codes/
                        │   │   ├── PathAndFilesUtilityMastery.java
                        │   │   ├── LazyStreamLargeFileLinesDemo.java
                        │   │   └── DirectoryWatchServicePoller.java
                        │   └── notes/
                        │       ├── 01-Why-Java-Io-File-Is-Dead-Path-And-Files-API.md
                        │       └── 02-Streaming-10GB-Files-Without-OutOfMemoryError.md
                        │
                        ├── phase3_nio_buffers_channels/ # java.nio Core: High-Performance Block I/O & Off-Heap Memory
                        │   ├── buffers/
                        │   │   ├── codes/
                        │   │   │   ├── ByteBufferPositionLimitCapacityDemo.java
                        │   │   │   └── HeapVsDirectByteBufferBenchmark.java
                        │   │   └── notes/
                        │   │       └── 01-Buffer-State-Machine-Flip-Clear-Compact.md
                        │   │
                        │   └── channels_and_zerocopy/
                        │       ├── codes/
                        │       │   ├── FileChannelTransferToZeroCopyDemo.java
                        │       │   └── MappedByteBufferMemoryMappedFile.java
                        │       └── notes/
                        │           └── 02-Channels-MemoryMappedFiles-And-OS-ZeroCopy.md
                        │
                        └── phase4_nonblocking_network/  # How Kafka, Netty & Modern Servers Work Under the Hood
                            ├── codes/
                            │   ├── SingleThreadSelectorEchoServer.java
                            │   └── VirtualThreadsVsNioSelectorDemo.java
                            └── notes/
                                ├── 01-Multiplexing-Selectors-And-Event-Loops.md
                                └── 02-Classic-IO-Vs-NIO-Vs-Java21-VirtualThreads.md
```

---

## 📊 Master Architectural Decision Matrix

Use this decision matrix to select the exact right I/O primitive for your production workload:

| Workload / Use Case | Recommended I/O API | Under-the-Hood Linux / JVM Mechanism | Why Other Approaches Fail |
| :--- | :--- | :--- | :--- |
| **Reading Small Config (< 1 MB)** | `Files.readString(path, UTF_8)` | NIO.2 Channel read into byte array | Fast, idiomatic, auto-closed. |
| **Processing 10GB+ Log Files** | `Files.lines(path, UTF_8)` inside TWR | `BufferedReader` + `Stream` Spliterator | `readAllLines()` causes instant `OutOfMemoryError`. |
| **Directory Iteration (100k+ files)**| `Files.newDirectoryStream(dir)` | POSIX `opendir()` / `readdir()` cursor | `File.listFiles()` allocates huge array causing GC spike. |
| **Serving Static Files / CDN** | `FileChannel.transferTo()` | Linux `sendfile(2)` + DMA Scatter-Gather | User-space stream loops cause 4 context switches + 4 copies. |
| **High-Speed IPC / Append Log** | `FileChannel.map()` (`MappedByteBuffer`)| Linux `mmap(2)` virtual memory pages | Standard write syscalls incur kernel trap frames. |
| **Competitive Programming Fast I/O**| Custom byte-level `FastScanner` | 32KB buffer + ASCII digit bit arithmetic | `java.util.Scanner` regex parsing is 30x–50x slower. |
| **High-Throughput Gateway / Proxy** | Netty (Multi-Reactor NIO) | Linux `epoll(7)` + Pooled Off-Heap Direct Buffers | Classic blocking IO exhausts OS thread limits at 3k conns. |
| **Standard Microservices / REST API**| Java 21 Virtual Threads + Blocking IO | Project Loom continuations + Carrier `ForkJoinPool` | Non-blocking reactive programming adds immense code complexity. |

---

## 🛠️ Step-by-Step Phase Breakdown

### Phase 1: Classic IO Streams (`java.io`)
- **[01-InputStream-OutputStream-And-OS-Syscalls.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/bytestreams/notes/01-InputStream-OutputStream-And-OS-Syscalls.md)**: Deep dive into the GoF Decorator Pattern, OS Ring 3 <-> Ring 0 context switches, DMA, Page Cache, and how `BufferedInputStream` cuts syscall storms by 99.98%.
  - Verified by: [`UnbufferedVsBufferedCopyBenchmark.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/bytestreams/codes/UnbufferedVsBufferedCopyBenchmark.java) and [`TryWithResourcesAutoCloseDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/bytestreams/codes/TryWithResourcesAutoCloseDemo.java).
- **[02-Readers-Writers-And-Decorator-Pattern.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/charstreams/notes/02-Readers-Writers-And-Decorator-Pattern.md)**: The GoF Adapter Pattern bridge (`InputStreamReader`), UTF-8 multi-byte decoding (1 to 4 bytes), surrogate pairs, and why `Scanner` is 38x slower than buffered parsing.
  - Verified by: [`InputStreamReaderBridgeDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/charstreams/codes/InputStreamReaderBridgeDemo.java) and [`BufferedReaderFastCPInputTemplate.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/charstreams/codes/BufferedReaderFastCPInputTemplate.java).
- **[03-Java-Serialization-SerialVersionUID-And-Traps.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/serialization/notes/03-Java-Serialization-SerialVersionUID-And-Traps.md)**: The `0xACED0005` wire format, `serialVersionUID` SHA-1 algorithm, Remote Code Execution (RCE) gadget chains, `transient` encryption, and Singleton restoration via `readResolve()`.
  - Verified by: [`CustomSerializationTransientDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/serialization/codes/CustomSerializationTransientDemo.java) and [`ExternalizableVsSerializableDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase1_classic_io_streams/serialization/codes/ExternalizableVsSerializableDemo.java).

### Phase 2: Modern NIO.2 Files (`java.nio.file`)
- **[01-Why-Java-Io-File-Is-Dead-Path-And-Files-API.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase2_modern_nio2_files/notes/01-Why-Java-Io-File-Is-Dead-Path-And-Files-API.md)**: The 5 fatal architectural flaws of `java.io.File`, Linux `stat64` single-syscall metadata batching via `BasicFileAttributes`, POSIX `ATOMIC_MOVE` renames, and OS `inotify`/`kqueue` directory polling.
  - Verified by: [`PathAndFilesUtilityMastery.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase2_modern_nio2_files/codes/PathAndFilesUtilityMastery.java) and [`DirectoryWatchServicePoller.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase2_modern_nio2_files/codes/DirectoryWatchServicePoller.java).
- **[02-Streaming-10GB-Files-Without-OutOfMemoryError.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase2_modern_nio2_files/notes/02-Streaming-10GB-Files-Without-OutOfMemoryError.md)**: Spliterator lazy pulling vs eager materialization, string object header memory inflation, OS kernel readahead, and preventing file descriptor exhaustion.
  - Verified by: [`LazyStreamLargeFileLinesDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase2_modern_nio2_files/codes/LazyStreamLargeFileLinesDemo.java).

### Phase 3: NIO Buffers & Channels (`java.nio`)
- **[01-Buffer-State-Machine-Flip-Clear-Compact.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/buffers/notes/01-Buffer-State-Machine-Flip-Clear-Compact.md)**: The 4-pointer Buffer state machine (`pos`, `lim`, `cap`, `mark`), JNI moving GC double-copy problem, off-heap `DirectByteBuffer` allocation via `Unsafe`, and `Cleaner` phantom references.
  - Verified by: [`ByteBufferPositionLimitCapacityDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/buffers/codes/ByteBufferPositionLimitCapacityDemo.java) and [`HeapVsDirectByteBufferBenchmark.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/buffers/codes/HeapVsDirectByteBufferBenchmark.java).
- **[02-Channels-MemoryMappedFiles-And-OS-ZeroCopy.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/channels_and_zerocopy/notes/02-Channels-MemoryMappedFiles-And-OS-ZeroCopy.md)**: Traditional 4-copy / 4-context switch bottleneck vs Linux `sendfile(2)` zero-copy via `FileChannel.transferTo()`, DMA Scatter-Gather, and `mmap(2)` virtual memory page faults.
  - Verified by: [`FileChannelTransferToZeroCopyDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/channels_and_zerocopy/codes/FileChannelTransferToZeroCopyDemo.java) and [`MappedByteBufferMemoryMappedFile.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase3_nio_buffers_channels/channels_and_zerocopy/codes/MappedByteBufferMemoryMappedFile.java).

### Phase 4: Non-Blocking Network (`java.nio.channels` & Virtual Threads)
- **[01-Multiplexing-Selectors-And-Event-Loops.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase4_nonblocking_network/notes/01-Multiplexing-Selectors-And-Event-Loops.md)**: The C10K problem, evolution from `select()` ($O(N)$) to Linux `epoll(7)` ($O(1)$ Red-Black tree + Ready List), Reactor Pattern, Level-Triggered vs Edge-Triggered, and the mandatory `it.remove()` rule.
  - Verified by: [`SingleThreadSelectorEchoServer.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase4_nonblocking_network/codes/SingleThreadSelectorEchoServer.java).
- **[02-Classic-IO-Vs-NIO-Vs-Java21-VirtualThreads.md](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase4_nonblocking_network/notes/02-Classic-IO-Vs-NIO-Vs-Java21-VirtualThreads.md)**: The 25-year concurrency evolution, Project Loom delimited continuations, unmounting carrier threads during blocking I/O, the thread-pinning hazard in `synchronized`, and why Netty is still used.
  - Verified by: [`VirtualThreadsVsNioSelectorDemo.java`](file:///Users/aashishkumarray/Desktop/engineer-java%20development/week_4/java-io-nio-mastery/src/main/java/com/raysi/ionio/phase4_nonblocking_network/codes/VirtualThreadsVsNioSelectorDemo.java).

---

## 🚀 How to Compile and Run All Demos (Java 21 LTS)

Navigate to the repository root directory:
```bash
cd "/Users/aashishkumarray/Desktop/engineer-java development/week_4/java-io-nio-mastery"
```

Compile all classes using Java 21:
```bash
javac -d bin $(find src/main/java -name "*.java")
```

Run any benchmark or demo directly:
```bash
# 1. Unbuffered vs Buffered Stream Benchmark
java -cp bin com.raysi.ionio.phase1_classic_io_streams.bytestreams.codes.UnbufferedVsBufferedCopyBenchmark

# 2. Competitive Programming FastScanner Benchmark
java -cp bin com.raysi.ionio.phase1_classic_io_streams.charstreams.codes.BufferedReaderFastCPInputTemplate

# 3. Serialization vs Externalizable Benchmark
java -cp bin com.raysi.ionio.phase1_classic_io_streams.serialization.codes.ExternalizableVsSerializableDemo

# 4. Lazy File Streaming (Constant Memory) Demo
java -cp bin com.raysi.ionio.phase2_modern_nio2_files.codes.LazyStreamLargeFileLinesDemo

# 5. ByteBuffer 4-Pointer State Machine Demo
java -cp bin com.raysi.ionio.phase3_nio_buffers_channels.buffers.codes.ByteBufferPositionLimitCapacityDemo

# 6. Heap vs Direct (Off-Heap) ByteBuffer Benchmark
java -cp bin com.raysi.ionio.phase3_nio_buffers_channels.buffers.codes.HeapVsDirectByteBufferBenchmark

# 7. Linux OS Zero-Copy transferTo() Benchmark
java -cp bin com.raysi.ionio.phase3_nio_buffers_channels.channels_and_zerocopy.codes.FileChannelTransferToZeroCopyDemo

# 8. Single-Thread Selector Non-Blocking Echo Server
java -cp bin com.raysi.ionio.phase4_nonblocking_network.codes.SingleThreadSelectorEchoServer

# 9. Java 21 Virtual Threads vs NIO Selector Benchmark
java -cp bin com.raysi.ionio.phase4_nonblocking_network.codes.VirtualThreadsVsNioSelectorDemo
```
