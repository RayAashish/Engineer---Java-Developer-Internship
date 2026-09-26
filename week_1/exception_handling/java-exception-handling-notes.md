# Java Exception Handling: Detailed Notes

An **exception** is an event that disrupts the normal flow of a program's instructions. Java uses **objects** to represent these events and a dedicated `try/catch/finally` mechanism to handle them gracefully instead of crashing.

```java
try {
    int r = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero: " + e.getMessage());
}
```

---

## Table of Contents

1. [Why Exception Handling?](#1-why-exception-handling)
2. [The Throwable Hierarchy](#2-the-throwable-hierarchy)
3. [Checked vs Unchecked Exceptions](#3-checked-vs-unchecked-exceptions)
4. [try, catch, finally](#4-try-catch-finally)
5. [Multi-catch and Catch Ordering](#5-multi-catch-and-catch-ordering)
6. [try-with-resources](#6-try-with-resources)
7. [throw vs throws](#7-throw-vs-throws)
8. [Custom (User-Defined) Exceptions](#8-custom-user-defined-exceptions)
9. [Exception Chaining (Cause)](#9-exception-chaining-cause)
10. [Overriding Methods and Checked Exceptions](#10-overriding-methods-and-checked-exceptions)
11. [Common Built-in Exceptions](#11-common-built-in-exceptions)
12. [Stack Traces and printStackTrace](#12-stack-traces-and-printstacktrace)
13. [finally, return and Control Flow Edge Cases](#13-finally-return-and-control-flow-edge-cases)
14. [Best Practices](#14-best-practices)
15. [Common Pitfalls](#15-common-pitfalls)
16. [Interview-Style Output Questions](#16-interview-style-output-questions)
17. [Quick Cheat Sheet](#17-quick-cheat-sheet)

---

## 1. Why Exception Handling?

Without exception handling, a runtime error terminates the program immediately (a "crash"). Exception handling lets you:

- Separate **error-handling code** from normal business logic.
- **Recover** from a problem (retry, use a default, log and continue) instead of crashing.
- **Propagate** an error up to a caller better equipped to handle it.
- Guarantee **cleanup** code runs (closing files, connections, locks) via `finally` or try-with-resources.

```java
// Without handling: program terminates
int[] a = {1, 2, 3};
System.out.println(a[5]);          // crashes with a stack trace, nothing after this runs

// With handling: program continues
try {
    System.out.println(a[5]);
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Invalid index, skipping.");
}
System.out.println("Program continues...");
```

---

## 2. The Throwable Hierarchy

```text
                         Object
                            |
                        Throwable
                       /          \
                   Error          Exception
                  /    \         /          \
        OutOfMemoryError  ...  RuntimeException   IOException, SQLException, ...
                                /       |        \        (checked)
              NullPointerException  ArithmeticException  ArrayIndexOutOfBoundsException
              ClassCastException  NumberFormatException  IllegalArgumentException  ...
```

| Class | Meaning |
|-------|---------|
| `Throwable` | Root of everything that can be thrown or caught |
| `Error` | Serious problems the application usually **should not** try to catch (`OutOfMemoryError`, `StackOverflowError`) |
| `Exception` | Conditions a well-written application **should** anticipate and handle |
| `RuntimeException` | **Unchecked** exceptions — usually programming mistakes |
| Everything else under `Exception` | **Checked** exceptions — external/expected failure conditions |

```java
System.out.println(new NullPointerException() instanceof RuntimeException);  // true
System.out.println(new RuntimeException() instanceof Exception);             // true
System.out.println(new IOException() instanceof RuntimeException);           // false
```

---

## 3. Checked vs Unchecked Exceptions

| | Checked | Unchecked (Runtime) | Error |
|---|---------|----------------------|-------|
| Parent | `Exception` (not `RuntimeException`) | `RuntimeException` | `Error` |
| Checked by compiler? | **Yes** — must catch or declare | No | No |
| Typical cause | External conditions (file missing, network down, SQL failure) | Programming bugs (null access, bad cast, bad index) | JVM-level problems |
| Examples | `IOException`, `SQLException`, `ParseException`, `ClassNotFoundException` | `NullPointerException`, `ArithmeticException`, `ArrayIndexOutOfBoundsException`, `NumberFormatException`, `ClassCastException`, `IllegalArgumentException` | `OutOfMemoryError`, `StackOverflowError` |
| Must handle? | Yes, or the code doesn't compile | No, but you usually should | Generally don't catch |

### 3.1 Checked exceptions must be handled or declared

```java
void readFile() {
    FileReader fr = new FileReader("data.txt");   // COMPILE ERROR
    // IOException is checked; must catch it or declare "throws IOException"
}

void readFile() throws IOException {              // OK: declared
    FileReader fr = new FileReader("data.txt");
}

void readFile() {
    try {                                          // OK: caught
        FileReader fr = new FileReader("data.txt");
    } catch (IOException e) {
        System.out.println("File not found: " + e.getMessage());
    }
}
```

### 3.2 Unchecked exceptions compile fine either way

```java
void divide(int a, int b) {
    System.out.println(a / b);      // ArithmeticException is unchecked; no try/catch required
}
```

---

## 4. try, catch, finally

### 4.1 Basic structure

```java
try {
    // risky code
} catch (SomeException e) {
    // handle it
} finally {
    // always runs (cleanup)
}
```

- `try` must be followed by at least one `catch`, or a `finally`, or both.
- A `try` with only `finally` (no `catch`) is legal — used purely for guaranteed cleanup.

```java
try {
    riskyOperation();
} finally {
    System.out.println("Cleanup, always runs");
}
```

### 4.2 `finally` always runs

`finally` executes whether or not an exception occurred, and even if the `try`/`catch` block contains a `return`.

```java
static int test() {
    try {
        return 1;
    } finally {
        System.out.println("finally runs even though try returned");
    }
}
System.out.println(test());
// prints: "finally runs even though try returned"
// then:   1
```

The **only** cases where `finally` does **not** run:

- The JVM shuts down via `System.exit()` inside `try`/`catch`.
- The thread is forcibly killed, or the JVM crashes (e.g., power failure, native crash).
- An infinite loop or fatal `Error` prevents reaching `finally`.

```java
try {
    System.out.println("try");
    System.exit(0);
} finally {
    System.out.println("finally");   // NEVER prints
}
```

### 4.3 Order of execution

```java
try {
    System.out.println("A");
    throw new RuntimeException("boom");
} catch (RuntimeException e) {
    System.out.println("B: " + e.getMessage());
} finally {
    System.out.println("C");
}
System.out.println("D");
// Output: A, B: boom, C, D
```

If no exception occurs:

```java
try {
    System.out.println("A");
} catch (RuntimeException e) {
    System.out.println("B");   // skipped
} finally {
    System.out.println("C");
}
// Output: A, C
```

### 4.4 Nested try blocks

```java
try {
    try {
        throw new NullPointerException("inner");
    } finally {
        System.out.println("inner finally");
    }
} catch (NullPointerException e) {
    System.out.println("outer catch: " + e.getMessage());
}
// Output: inner finally
//         outer catch: inner
```

---

## 5. Multi-catch and Catch Ordering

### 5.1 Multiple catch blocks

```java
try {
    int[] a = new int[3];
    a[5] = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Arithmetic problem");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Index problem");
} catch (Exception e) {
    System.out.println("Something else: " + e);
}
```

**Rule: subclasses must be caught before their superclasses.** Otherwise the compiler flags unreachable code.

```java
try {
    // ...
} catch (Exception e) {                 // catches everything
    System.out.println("Generic");
} catch (ArithmeticException e) {       // COMPILE ERROR: already caught above
    System.out.println("Specific");
}
```

Correct order (most specific first):

```java
try {
    // ...
} catch (ArithmeticException e) {
    System.out.println("Specific");
} catch (Exception e) {
    System.out.println("Generic");
}
```

### 5.2 Multi-catch with `|` (Java 7+)

Combine unrelated exception types handled the same way. The exception variable is implicitly `final`.

```java
try {
    process();
} catch (IOException | SQLException e) {
    System.out.println("I/O or DB problem: " + e.getMessage());
}
```

Rules:

- The alternatives must **not** be related by inheritance (e.g., `IOException | FileNotFoundException` is a compile error, since `FileNotFoundException` **is-a** `IOException`, making it redundant).
- You cannot reassign the caught variable inside a multi-catch block (it's effectively `final`).

### 5.3 Only one block executes

Only the **first matching** `catch` runs; control then jumps to `finally` (if present), skipping the rest.

---

## 6. try-with-resources

Introduced in Java 7 to auto-close resources (anything implementing `AutoCloseable`/`Closeable`) without an explicit `finally`.

```java
try (FileReader fr = new FileReader("data.txt");
     BufferedReader br = new BufferedReader(fr)) {

    System.out.println(br.readLine());

} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}
// fr and br are closed automatically, in reverse order of creation, even if an exception is thrown
```

### 6.1 Rules

- Resource variables declared in the `try(...)` parentheses are implicitly `final`.
- Resources are closed in **reverse order** of declaration.
- The resource's `close()` runs **before** any `catch` or `finally` block.
- Multiple resources are separated by `;`.
- Java 9+ allows using an **already-declared effectively-final variable** directly:

```java
BufferedReader br = new BufferedReader(new FileReader("data.txt"));
try (br) {                       // Java 9+: reuse an existing effectively-final resource
    System.out.println(br.readLine());
}
```

### 6.2 Custom AutoCloseable resources

```java
class MyResource implements AutoCloseable {
    public void use() { System.out.println("using resource"); }
    @Override public void close() { System.out.println("resource closed"); }
}

try (MyResource r = new MyResource()) {
    r.use();
    throw new RuntimeException("oops");
} catch (RuntimeException e) {
    System.out.println("caught: " + e.getMessage());
}
// Output: using resource
//         resource closed
//         caught: oops
```

Note: the resource is closed **before** the exception reaches the `catch` block.

### 6.3 Suppressed exceptions

If both the `try` body **and** `close()` throw, the `close()` exception is **suppressed**, attached to the primary one, and retrievable via `getSuppressed()`.

```java
class Bad implements AutoCloseable {
    public void close() { throw new RuntimeException("close failed"); }
}

try (Bad b = new Bad()) {
    throw new RuntimeException("body failed");
} catch (RuntimeException e) {
    System.out.println("Primary: " + e.getMessage());              // body failed
    for (Throwable t : e.getSuppressed()) {
        System.out.println("Suppressed: " + t.getMessage());       // close failed
    }
}
```

### 6.4 try-with-resources vs manual finally

```java
// Old style — verbose, error-prone
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("data.txt"));
    System.out.println(br.readLine());
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
} finally {
    if (br != null) {
        try { br.close(); } catch (IOException e) { /* ignored */ }
    }
}
```

Prefer try-with-resources whenever the resource implements `AutoCloseable`.

---

## 7. throw vs throws

| | `throw` | `throws` |
|---|---------|----------|
| Used | Inside a method body | In a method signature |
| Purpose | Actually **throw** an exception instance | **Declare** that a method might propagate a checked exception |
| Followed by | An object (`throw new IOException(...)`) | One or more class names (`throws IOException, SQLException`) |
| Count | Only ever throws **one** object at a time | Can list **multiple** exception types |

```java
static void validateAge(int age) {
    if (age < 18) {
        throw new IllegalArgumentException("Age must be at least 18");
    }
}

static void readData() throws IOException, SQLException {
    // may propagate either checked exception to its caller
}
```

### 7.1 `throw` requires a `Throwable` instance

```java
throw new RuntimeException("bad input");   // OK
throw new Exception("checked");            // OK if enclosing method declares/handles it
throw null;                                // compiles, throws NullPointerException at runtime
```

### 7.2 `throws` only matters for checked exceptions

Declaring `throws SomeRuntimeException` is legal but **optional** documentation; the compiler does not enforce it for unchecked exceptions.

---

## 8. Custom (User-Defined) Exceptions

Create custom exceptions to represent domain-specific error conditions clearly.

### 8.1 Checked custom exception

```java
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class BankAccount {
    private double balance;

    void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Cannot withdraw " + amount + "; balance is only " + balance);
        }
        balance -= amount;
    }
}
```

Callers **must** handle it:

```java
BankAccount acc = new BankAccount();
try {
    acc.withdraw(500);
} catch (InsufficientFundsException e) {
    System.out.println(e.getMessage());
}
```

### 8.2 Unchecked custom exception

```java
class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}

void placeOrder(int quantity) {
    if (quantity <= 0) {
        throw new InvalidOrderException("Quantity must be positive");
    }
}
```

No `throws` declaration or mandatory `catch` needed for callers.

### 8.3 When to use checked vs unchecked for your own exceptions

- **Checked**: the caller can reasonably be expected to **recover** (retry, use a fallback, prompt the user again) — e.g., file-not-found, network timeout, invalid business state during a workflow.
- **Unchecked**: the failure indicates a **programming error** or a violated precondition that the caller shouldn't normally need to handle at every call site — e.g., invalid arguments, broken invariants.

### 8.4 Adding extra fields

```java
class OrderException extends RuntimeException {
    private final int orderId;

    public OrderException(String message, int orderId) {
        super(message);
        this.orderId = orderId;
    }

    public int getOrderId() { return orderId; }
}
```

---

## 9. Exception Chaining (Cause)

Preserve the **original** exception when wrapping it in a higher-level one, so debugging isn't blind.

```java
try {
    parseConfig();
} catch (NumberFormatException e) {
    throw new RuntimeException("Failed to load configuration", e);   // e is the "cause"
}
```

`Throwable` provides:

```java
Throwable getCause();
void initCause(Throwable cause);
Throwable(String message, Throwable cause);
Throwable(Throwable cause);
```

```java
try {
    try {
        throw new NullPointerException("root cause");
    } catch (NullPointerException e) {
        throw new IllegalStateException("wrapper", e);
    }
} catch (IllegalStateException e) {
    System.out.println(e.getMessage());              // wrapper
    System.out.println(e.getCause().getMessage());    // root cause
}
```

A `printStackTrace()` on the wrapper prints `Caused by: ...` showing the full chain — essential for diagnosing layered systems (e.g., a DB error wrapped by a service-layer exception).

---

## 10. Overriding Methods and Checked Exceptions

An overriding method can **narrow** or **eliminate** the checked exceptions of the method it overrides, but cannot **widen** them (add new or broader checked exceptions).

```java
class Base {
    void work() throws IOException { }
}

class Derived extends Base {
    @Override
    void work() throws FileNotFoundException { }   // OK: subclass of IOException (narrower)
}

class Derived2 extends Base {
    @Override
    void work() { }                                 // OK: throwing fewer/no checked exceptions is fine
}

class Derived3 extends Base {
    @Override
    void work() throws Exception { }                // COMPILE ERROR: broader than IOException
}
```

Unchecked exceptions are **not restricted** — an override can throw any `RuntimeException` freely, regardless of what the superclass method declares.

### 10.1 Interfaces

The same rule applies to interface method implementations.

```java
interface Task {
    void run() throws IOException;
}

class MyTask implements Task {
    public void run() { }               // OK
    // public void run() throws Exception { }   // COMPILE ERROR
}
```

---

## 11. Common Built-in Exceptions

### 11.1 Unchecked (RuntimeException subclasses)

| Exception | Typical cause |
|-----------|----------------|
| `NullPointerException` | Calling a method or accessing a field on `null` |
| `ArithmeticException` | Integer division/modulus by zero |
| `ArrayIndexOutOfBoundsException` | Array index `< 0` or `>= length` |
| `StringIndexOutOfBoundsException` | Invalid `String` index |
| `ClassCastException` | Invalid downcast between unrelated types |
| `NumberFormatException` | `Integer.parseInt("abc")` on non-numeric text |
| `IllegalArgumentException` | A method received an invalid argument |
| `IllegalStateException` | A method called at the wrong time / wrong object state |
| `UnsupportedOperationException` | Unsupported operation, e.g., modifying an immutable list |
| `ConcurrentModificationException` | Structurally modifying a collection while iterating it |
| `NegativeArraySizeException` | `new int[-1]` |
| `IndexOutOfBoundsException` | General index problems (parent of array/string variants) |

```java
Object o = "text";
Integer i = (Integer) o;               // ClassCastException

Integer.parseInt("12a");               // NumberFormatException

List<Integer> immutable = List.of(1, 2, 3);
immutable.add(4);                      // UnsupportedOperationException

List<Integer> list = new ArrayList<>(List.of(1, 2, 3));
for (int x : list) {
    if (x == 2) list.remove(Integer.valueOf(2));   // ConcurrentModificationException
}
```

### 11.2 Checked (Exception subclasses, not RuntimeException)

| Exception | Typical cause |
|-----------|----------------|
| `IOException` | General I/O failure |
| `FileNotFoundException` | File doesn't exist (subclass of `IOException`) |
| `SQLException` | Database access error |
| `ClassNotFoundException` | `Class.forName("...")` can't find the class |
| `InterruptedException` | A thread is interrupted while waiting/sleeping |
| `ParseException` | Text couldn't be parsed (e.g., `SimpleDateFormat`) |
| `CloneNotSupportedException` | `clone()` called on a non-`Cloneable` object |

### 11.3 Errors (do not normally catch)

| Error | Typical cause |
|-------|----------------|
| `StackOverflowError` | Excessive/infinite recursion |
| `OutOfMemoryError` | Heap exhausted |
| `NoClassDefFoundError` | A class present at compile time is missing at runtime |
| `AssertionError` | A failed `assert` statement |

```java
static void recurse() { recurse(); }
recurse();     // StackOverflowError
```

---

## 12. Stack Traces and printStackTrace

Every `Throwable` records where it was created.

```java
try {
    int[] a = new int[2];
    a[5] = 1;
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Message: " + e.getMessage());
    System.out.println("toString: " + e);
    e.printStackTrace();
}
```

Sample output:

```text
Message: Index 5 out of bounds for length 2
toString: java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 2
java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 2
    at Main.main(Main.java:5)
```

### 12.1 Useful `Throwable` methods

| Method | Purpose |
|--------|---------|
| `getMessage()` | The detail message passed to the constructor |
| `getLocalizedMessage()` | Localized version (defaults to `getMessage()`) |
| `toString()` | `ClassName: message` |
| `printStackTrace()` | Prints the full trace to `System.err` |
| `getStackTrace()` | Returns `StackTraceElement[]` for programmatic inspection |
| `getCause()` | The wrapped underlying exception, or `null` |
| `getSuppressed()` | Exceptions suppressed by try-with-resources |

### 12.2 Custom exception `toString`

```java
class MyException extends RuntimeException {
    MyException(String msg) { super(msg); }
}

try {
    throw new MyException("custom error");
} catch (MyException e) {
    System.out.println(e);   // MyException: custom error
}
```

---

## 13. finally, return and Control Flow Edge Cases

### 13.1 `finally` can override a `return`

If `finally` **itself** contains a `return`, it **replaces** any pending `return` or exception from the `try`/`catch` — this silently swallows exceptions and is considered bad practice.

```java
static int test() {
    try {
        return 1;
    } finally {
        return 2;          // overrides the try's return
    }
}
System.out.println(test());   // 2
```

```java
static int test2() {
    try {
        throw new RuntimeException("boom");
    } finally {
        return 99;          // swallows the exception entirely — DANGEROUS
    }
}
System.out.println(test2());   // 99, exception is lost silently
```

### 13.2 Return value is computed before finally runs (for non-return finally)

```java
static int test3() {
    int x = 1;
    try {
        return x;
    } finally {
        x = 2;               // does NOT change the already-computed return value
    }
}
System.out.println(test3());   // 1
```

The return value `1` is evaluated and stored **before** `finally` executes; only a `return` *inside* `finally` can change the outcome.

### 13.3 Exception thrown in `finally` masks one from `try`

```java
static void test4() {
    try {
        throw new RuntimeException("from try");
    } finally {
        throw new RuntimeException("from finally");   // this one propagates instead
    }
}
test4();
// RuntimeException: from finally  (the "from try" exception is lost)
```

### 13.4 Unreachable `catch` after a caught superclass

```java
try {
    throw new RuntimeException();
} catch (Exception e) {
    System.out.println("caught as Exception");
}
// If a later catch(RuntimeException e) existed here, it would be a compile error (unreachable)
```

---

## 14. Best Practices

1. **Catch specific exceptions**, not blanket `Exception` or `Throwable`, unless truly necessary (e.g., at a top-level handler/logging boundary).
2. **Never swallow exceptions silently:**
   ```java
   catch (Exception e) { }              // BAD: error disappears without a trace
   catch (Exception e) { log.error("...", e); }   // GOOD
   ```
3. **Don't use exceptions for normal control flow.** They are relatively expensive and hurt readability.
4. **Always clean up resources** — prefer try-with-resources over manual `finally` blocks.
5. **Preserve the cause** when wrapping exceptions (`throw new RuntimeException("...", e)`), never discard the original.
6. **Don't return from `finally`** — it hides exceptions and bugs.
7. **Throw early, catch late**: validate inputs and fail fast near the source of the problem; handle/log at a layer that can actually respond meaningfully.
8. **Use unchecked exceptions for programming errors**, checked for recoverable, expected conditions — and don't overuse checked exceptions, since they clutter every calling method's signature.
9. **Include a meaningful message** in every thrown exception (`what` failed and, where useful, `why`/relevant values).
10. **Don't catch `Error`** (like `OutOfMemoryError`) — the JVM is usually in an unstable state.

---

## 15. Common Pitfalls

| # | Pitfall | Fix |
|---|---------|-----|
| 1 | Catching `Exception` (or `Throwable`) generically, hiding real bugs | Catch the specific exception types you expect |
| 2 | Empty `catch` block | At minimum log it |
| 3 | `return` inside `finally` | Avoid; let `try`/`catch` control the return value |
| 4 | Declaring `throws Exception` on every method | Declare only the specific checked exceptions actually thrown |
| 5 | Catching a superclass before a subclass | Order `catch` blocks from most specific to most general |
| 6 | Losing the original exception when wrapping | Pass it as the `cause`: `new RuntimeException(msg, e)` |
| 7 | Using exceptions for expected, frequent conditions (e.g., end-of-input) | Use return values/optional checks instead |
| 8 | Forgetting `close()` on resources in older code | Use try-with-resources |
| 9 | Assuming `finally` always runs | It skips on `System.exit()` or JVM crash |
| 10 | Catching `NullPointerException` instead of checking for `null` | Null-check proactively; catching NPE is usually a design smell |
| 11 | Overriding a method and widening its checked exceptions | Only narrow or remove checked exceptions in overrides |
| 12 | Assuming `Arrays.asList(...).add()` failures are catchable/fixable at runtime the same way | Understand the type: `UnsupportedOperationException` means "fix the code", not "retry" |

---

## 16. Interview-Style Output Questions

**Q1**
```java
try {
    System.out.println("A");
    throw new RuntimeException("x");
} catch (RuntimeException e) {
    System.out.println("B");
} finally {
    System.out.println("C");
}
System.out.println("D");
```
**Answer:** `A`, `B`, `C`, `D`.

---

**Q2**
```java
static int f() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
System.out.println(f());
```
**Answer:** `2`. `finally`'s `return` overrides the `try`'s.

---

**Q3**
```java
static int g() {
    int x = 10;
    try {
        return x;
    } finally {
        x = 20;
    }
}
System.out.println(g());
```
**Answer:** `10`. The return value was captured before `finally` ran.

---

**Q4**
```java
try {
    throw new NullPointerException("npe");
} catch (RuntimeException e) {
    System.out.println("Runtime: " + e.getMessage());
} catch (NullPointerException e) {
    System.out.println("NPE: " + e.getMessage());
}
```
**Answer:** Compile error — `NullPointerException` is unreachable because `RuntimeException` (its superclass) is caught first.

---

**Q5**
```java
try {
    System.out.println(10 / 0);
} catch (Exception e) {
    System.out.println(e.getClass().getSimpleName());
}
```
**Answer:** `ArithmeticException`.

---

**Q6**
```java
class A implements AutoCloseable {
    public void close() { System.out.println("closed"); }
}
try (A a = new A()) {
    System.out.println("using");
    throw new RuntimeException("fail");
} catch (RuntimeException e) {
    System.out.println("caught: " + e.getMessage());
}
```
**Answer:** `using`, `closed`, `caught: fail`. The resource is closed before the exception reaches `catch`.

---

**Q7**
```java
try {
    try {
        throw new RuntimeException("inner");
    } finally {
        throw new RuntimeException("outer");
    }
} catch (RuntimeException e) {
    System.out.println(e.getMessage());
}
```
**Answer:** `outer`. The exception from `finally` replaces the one from `try`.

---

**Q8**
```java
Object o = "hello";
try {
    Integer i = (Integer) o;
} catch (ClassCastException e) {
    System.out.println("CCE caught");
} catch (RuntimeException e) {
    System.out.println("RE caught");
}
```
**Answer:** `CCE caught`. `ClassCastException` is matched first since it comes before `RuntimeException` in this valid ordering.

---

**Q9**
```java
static void m() throws IOException {
    throw new FileNotFoundException("missing");
}
try {
    m();
} catch (IOException e) {
    System.out.println(e.getClass().getSimpleName());
}
```
**Answer:** `FileNotFoundException`. The actual runtime type is caught, not the declared/static type.

---

**Q10**
```java
try {
    List<Integer> l = List.of(1, 2, 3);
    l.add(4);
} catch (UnsupportedOperationException e) {
    System.out.println("immutable list");
} finally {
    System.out.println("done");
}
```
**Answer:** `immutable list`, `done`.

---

## 17. Quick Cheat Sheet

```text
HIERARCHY      Throwable -> Error | Exception -> RuntimeException (unchecked) | other (checked)

CHECKED        Must catch or declare "throws".  Examples: IOException, SQLException
UNCHECKED      No compiler enforcement.          Examples: NullPointerException, ArithmeticException

try            risky code
catch (E e)    handle a specific exception type (most specific first!)
catch (A|B e)  multi-catch, unrelated types, variable is effectively final
finally        always runs (except System.exit / JVM crash); return inside it overrides everything
try (res)      auto-closes AutoCloseable resources, in reverse order, before catch/finally

throw new X()  actually throw an instance
throws X, Y    declare possible checked exceptions in a method signature

OVERRIDING     Can narrow/remove checked exceptions, never widen them. Unchecked: no restriction.

CUSTOM         extends Exception        -> checked  (caller must handle)
               extends RuntimeException -> unchecked (caller need not handle)

CHAINING       new RuntimeException("wrapper msg", originalException)
               e.getCause() to retrieve it
```

**Remember:**

1. Checked exceptions are compiler-enforced and represent expected, recoverable conditions; unchecked ones are usually bugs.
2. Order `catch` blocks from most specific to most general, or it won't compile.
3. `finally` always runs — but never put a `return` inside it.
4. Prefer try-with-resources over manual `close()` in `finally`.
5. Always preserve the original exception as a `cause` when wrapping.
6. An override can only narrow or drop checked exceptions, never add broader ones.
7. Don't swallow exceptions silently — at minimum log them.
