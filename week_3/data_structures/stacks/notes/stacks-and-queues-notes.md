# Stacks and Queues: Detailed Notes

**Stacks** and **queues** are the two most fundamental **restricted-access linear structures** in computer science: both store a sequence of elements, but each deliberately **limits where you can add and remove** elements, in exchange for guaranteed O(1) operations at those specific ends. That restriction is not a limitation — it's the entire point, and it's exactly what makes each structure the natural fit for a huge class of problems (undo history, matching brackets, BFS, scheduling, and more).

```text
STACK (LIFO — Last In, First Out):          QUEUE (FIFO — First In, First Out):

      push/pop                               enqueue ->                 -> dequeue
         |                                          |                           |
         v                                          v                           v
      [ top ]                               [ front ] [ ] [ ] [ ] [ rear ]
      [     ]
      [     ]
      [bottom]
```

---

## Table of Contents

1. [Introduction: Why Restrict Access At All?](#1-introduction-why-restrict-access-at-all)
2. [The Stack: LIFO](#2-the-stack-lifo)
3. [The Queue: FIFO](#3-the-queue-fifo)
4. [The "Brain" Behind Both: Two Implementation Strategies](#4-the-brain-behind-both-two-implementation-strategies)
5. [Array-Based Stack Implementation](#5-array-based-stack-implementation)
6. [Linked-List-Based Stack Implementation](#6-linked-list-based-stack-implementation)
7. [Array-Based Queue: The Circular Buffer Trick](#7-array-based-queue-the-circular-buffer-trick)
8. [Linked-List-Based Queue Implementation](#8-linked-list-based-queue-implementation)
9. [Complexity Summary Table](#9-complexity-summary-table)
10. [Stacks and Queues in Java: The Built-in Types](#10-stacks-and-queues-in-java-the-built-in-types)
11. [Why `Stack` (the Class) Is Discouraged](#11-why-stack-the-class-is-discouraged)
12. [`Deque`: The Unified Double-Ended Structure](#12-deque-the-unified-double-ended-structure)
13. [Variants Built on Top: Deque, Circular Queue, Priority Queue, Monotonic Stack/Queue](#13-variants-built-on-top-deque-circular-queue-priority-queue-monotonic-stackqueue)
14. [Types of Problems Stacks Solve](#14-types-of-problems-stacks-solve)
15. [Types of Problems Queues Solve](#15-types-of-problems-queues-solve)
16. [Recognizing When to Use a Stack vs a Queue](#16-recognizing-when-to-use-a-stack-vs-a-queue)
17. [Common Stack Patterns](#17-common-stack-patterns)
18. [Common Queue Patterns](#18-common-queue-patterns)
19. [Top 20 Stack & Queue Problems (By Pattern)](#19-top-20-stack--queue-problems-by-pattern)
20. [Sample Solutions (Worked)](#20-sample-solutions-worked)
21. [Common Pitfalls](#21-common-pitfalls)
22. [Quick Cheat Sheet](#22-quick-cheat-sheet)

---

## 1. Introduction: Why Restrict Access At All?

A plain array or `ArrayList` already lets you insert/remove from anywhere. So why deliberately build a structure that **only** allows access at one or two specific ends?

| Reason | Explanation |
|--------|--------------|
| **Guaranteed O(1) at the restricted ends** | By never needing to shift elements or search, both structures guarantee constant-time add/remove exactly where it matters |
| **Matches real-world processing order directly** | Many problems are *naturally* LIFO (undo history, function call returns, nested brackets) or FIFO (ticket queues, task scheduling, BFS layers) — modeling that directly avoids extra bookkeeping |
| **Simplicity and correctness** | A restricted interface prevents accidental misuse — you literally cannot "peek at the middle" and introduce subtle ordering bugs, because the type doesn't expose that operation |
| **The foundation for more complex structures** | Stacks underlie recursion/DFS/backtracking; queues underlie BFS and scheduling — understanding these two deeply pays off across nearly every other topic |

---

## 2. The Stack: LIFO

A **stack** only allows adding and removing elements from **one end**, called the **top**. The last element pushed is always the first one popped — **Last In, First Out (LIFO)**.

```text
push(1) -> push(2) -> push(3) -> pop() returns 3 -> pop() returns 2 -> pop() returns 1

Visualized:
push 1:  [1]
push 2:  [1,2]
push 3:  [1,2,3]   <- top
pop():   returns 3, stack becomes [1,2]
pop():   returns 2, stack becomes [1]
```

### 2.1 Core stack operations

| Operation | Meaning | Complexity |
|-----------|---------|------------|
| `push(x)` | Add `x` to the top | O(1) |
| `pop()` | Remove and return the top element | O(1) |
| `peek()` / `top()` | View the top element without removing it | O(1) |
| `isEmpty()` | Check whether the stack has any elements | O(1) |

### 2.2 Real-world intuition

A **stack of plates**: you can only take the top plate off, and you can only add a new plate to the top. To get the bottom plate, you'd have to remove every plate above it first — there is no shortcut, and that constraint is exactly what a stack enforces in code.

### 2.3 Where stacks show up implicitly, even when you don't write one

Every function call in a program uses an implicit **call stack**: calling a function **pushes** a new frame (local variables, return address); returning from it **pops** that frame off. This is precisely why **deep, unbounded recursion** causes a `StackOverflowError` — the call stack, like any stack, has finite capacity.

```java
void countDown(int n) {
    if (n == 0) return;
    System.out.println(n);
    countDown(n - 1);     // pushes a new call frame each time
}                            // each return pops a frame
```

---

## 3. The Queue: FIFO

A **queue** allows adding elements at one end (the **rear**/**tail**) and removing elements from the **other** end (the **front**/**head**). The first element added is always the first one removed — **First In, First Out (FIFO)**.

```text
enqueue(1) -> enqueue(2) -> enqueue(3) -> dequeue() returns 1 -> dequeue() returns 2

Visualized:
enqueue 1:  [1]
enqueue 2:  [1,2]
enqueue 3:  [1,2,3]    front=1, rear=3
dequeue():  returns 1, queue becomes [2,3]
dequeue():  returns 2, queue becomes [3]
```

### 3.1 Core queue operations

| Operation | Meaning | Complexity |
|-----------|---------|------------|
| `enqueue(x)` / `offer(x)` | Add `x` to the rear | O(1) |
| `dequeue()` / `poll()` | Remove and return the front element | O(1) |
| `peek()` / `front()` | View the front element without removing it | O(1) |
| `isEmpty()` | Check whether the queue has any elements | O(1) |

### 3.2 Real-world intuition

A **line at a ticket counter**: whoever arrived first is served first, and new arrivals always join at the back. There's no cutting in line (no access to the middle), and that constraint — fairness/order-of-arrival — is precisely what a queue models.

### 3.3 Where queues show up implicitly

**Breadth-first search (BFS)** on a tree or graph is fundamentally "process everything one step away, then everything two steps away, then three..." — and the only way to guarantee that strict layer-by-layer order is to use a queue (see the tree notes' level-order traversal, which is exactly this).

---

## 4. The "Brain" Behind Both: Two Implementation Strategies

Both stacks and queues are **abstract behaviors** (a contract: "only touch these specific ends"), not tied to one specific underlying storage mechanism. There are exactly two general-purpose ways to implement either:

```text
ARRAY-BASED (dynamic array / ArrayList-style)
  + Cache-friendly (contiguous memory)
  + No per-element pointer overhead
  - For a stack: trivial (just track a "top index")
  - For a queue: naive array shifting is O(n) per dequeue — SOLVED by the circular buffer trick (section 7)

LINKED-LIST-BASED (nodes with next/prev pointers)
  + Natural O(1) at both ends with NO resizing/wraparound logic needed at all
  + No fixed capacity — grows one node at a time
  - Slightly more memory overhead per element (the pointer itself)
  - Worse cache locality (nodes scattered across heap-allocated memory)
```

> This exact "array vs linked" choice — and its consequences for complexity — is the same fundamental tradeoff that distinguishes `ArrayList` from `LinkedList` in general, just now applied specifically to the restricted-access case.

---

## 5. Array-Based Stack Implementation

A stack maps almost perfectly onto a dynamic array: "push" is "append at the end," "pop" is "remove from the end" — both of which are already O(1) amortized for a resizable array (no shifting needed, since the end is exactly where growth happens).

```java
import java.util.Arrays;

class ArrayStack {
    private int[] data;
    private int top;         // index of the topmost element; -1 means empty

    ArrayStack(int capacity) {
        data = new int[capacity];
        top = -1;
    }

    void push(int value) {
        if (top == data.length - 1) {
            data = Arrays.copyOf(data, data.length * 2);   // grow, like an ArrayList
        }
        data[++top] = value;
    }

    int pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return data[top--];
    }

    int peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return data[top];
    }

    boolean isEmpty() {
        return top == -1;
    }
}
```

```java
ArrayStack s = new ArrayStack(5);
s.push(1); s.push(2); s.push(3);
System.out.println(s.pop());    // 3
System.out.println(s.peek());    // 2
```

---

## 6. Linked-List-Based Stack Implementation

Pushing and popping at the **head** of a singly linked list is naturally O(1) — no index tracking needed at all, and no resizing logic, since each push simply allocates one new node.

```java
class LinkedStack {
    private static class Node {
        int val;
        Node next;
        Node(int val, Node next) { this.val = val; this.next = next; }
    }

    private Node top;

    void push(int value) {
        top = new Node(value, top);     // new node becomes the head, pointing at the OLD head
    }

    int pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        int val = top.val;
        top = top.next;                  // simply detach the head
        return val;
    }

    int peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return top.val;
    }

    boolean isEmpty() {
        return top == null;
    }
}
```

> Note that pushing/popping at the **head** (not the tail) of a singly linked list is what keeps this O(1) — operating at the tail of a singly linked list would require traversing the whole list to find the second-to-last node first, which is exactly why a stack built on a linked list always grows from the head.

---

## 7. Array-Based Queue: The Circular Buffer Trick

A naive array-based queue — enqueue at the end, dequeue from the front by shifting everything left — costs **O(n) per dequeue**, because every remaining element must shift down one position to fill the gap left at the front. The fix: a **circular buffer**, which lets both `front` and `rear` "wrap around" to the start of the array once they reach the end, so no shifting is ever needed.

```java
class CircularArrayQueue {
    private int[] data;
    private int front, rear, size, capacity;

    CircularArrayQueue(int capacity) {
        this.capacity = capacity;
        data = new int[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    void enqueue(int value) {
        if (size == capacity) throw new RuntimeException("Queue is full");
        rear = (rear + 1) % capacity;      // WRAP AROUND once rear hits the end of the array
        data[rear] = value;
        size++;
    }

    int dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        int val = data[front];
        front = (front + 1) % capacity;    // WRAP AROUND once front hits the end of the array
        size--;
        return val;
    }

    boolean isEmpty() { return size == 0; }
    boolean isFull()   { return size == capacity; }
}
```

```text
capacity = 4, enqueue 1,2,3,4 then dequeue twice then enqueue 5,6:

enqueue 1,2,3,4:   [1,2,3,4]   front=0, rear=3
dequeue() -> 1:    [_,2,3,4]   front=1, rear=3
dequeue() -> 2:    [_,_,3,4]   front=2, rear=3
enqueue 5:         [_,_,3,4]   rear wraps: (3+1)%4=0 -> data[0]=5 -> [5,_,3,4]  front=2, rear=0
enqueue 6:         rear: (0+1)%4=1 -> data[1]=6 -> [5,6,3,4]   front=2, rear=1

The array's PHYSICAL layout no longer matches the logical front-to-rear order — the modulo arithmetic
handles that translation transparently.
```

### 7.1 Why `% capacity` is "the brain" of the array-based queue

This is the single key insight that makes an array-based queue efficient: instead of treating the array as a fixed, one-directional strip (which forces shifting when the front empties out), the modulo operation treats it as a **logical ring** — index `capacity - 1` is immediately followed by index `0` again, exactly like a clock face wrapping from 11 back to 12. This reclaims the "wasted" space at the front of the array left behind by earlier dequeues, without ever needing to physically move any element.

---

## 8. Linked-List-Based Queue Implementation

A doubly-tracked (head + tail reference) singly linked list gives O(1) enqueue (append at tail) and O(1) dequeue (remove from head) — no modulo arithmetic or capacity planning needed at all, since the list simply grows and shrinks node by node.

```java
class LinkedQueue {
    private static class Node {
        int val;
        Node next;
        Node(int val) { this.val = val; }
    }

    private Node head, tail;

    void enqueue(int value) {
        Node newNode = new Node(value);
        if (tail == null) {
            head = tail = newNode;        // first element: head and tail are the same node
        } else {
            tail.next = newNode;            // link the OLD tail to the new node
            tail = newNode;                  // the new node becomes the new tail
        }
    }

    int dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        int val = head.val;
        head = head.next;                   // simply advance the head
        if (head == null) tail = null;       // queue just became empty — reset tail too
        return val;
    }

    boolean isEmpty() { return head == null; }
}
```

> Keeping an explicit `tail` reference is what makes `enqueue` O(1) here — without it, appending to a singly linked list would require walking the entire list every time to find the current last node, turning enqueue into an O(n) operation.

---

## 9. Complexity Summary Table

| Operation | Array-based Stack | Linked Stack | Circular Array Queue | Linked Queue |
|-----------|----------------------|----------------|--------------------------|----------------|
| Push/Enqueue | O(1) amortized | O(1) | O(1) | O(1) |
| Pop/Dequeue | O(1) | O(1) | O(1) | O(1) |
| Peek | O(1) | O(1) | O(1) | O(1) |
| Space | O(n), some unused capacity possible | O(n), plus per-node pointer overhead | O(n), fixed capacity (or resizable) | O(n), plus per-node pointer overhead |
| Random access to middle elements | Not supported (by design) | Not supported (by design) | Not supported (by design) | Not supported (by design) |

---

## 10. Stacks and Queues in Java: The Built-in Types

```java
import java.util.*;
```

| Interface/Class | Role | Recommended? |
|---------------------|------|------------------|
| `Deque<E>` | Double-ended queue interface — supports both stack-like AND queue-like operations | **Yes — the modern recommended choice for both stacks and queues** |
| `ArrayDeque<E>` | The standard, high-performance implementation of `Deque`, array-based (resizable circular buffer) | **Yes — prefer this for both stack and queue use cases** |
| `LinkedList<E>` | Implements both `List` and `Deque` | Usable as a `Deque`, but generally no advantage over `ArrayDeque` and has more per-node overhead |
| `Queue<E>` | The basic FIFO interface | Fine for plain queue use; `Deque` is a superset and usually preferred anyway |
| `Stack<E>` | A legacy, `Vector`-based stack class from Java 1.0 | **Discouraged** (see section 11) |
| `PriorityQueue<E>` | A heap, not a FIFO queue despite the name | Covered fully in the heap notes — mentioned here only to avoid confusion |

### 10.1 Using `ArrayDeque` as a stack

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);     // adds to the "head" end
stack.push(2);
stack.push(3);
System.out.println(stack.pop());    // 3 — LIFO
System.out.println(stack.peek());    // 2
```

### 10.2 Using `ArrayDeque` as a queue

```java
Deque<Integer> queue = new ArrayDeque<>();
queue.offer(1);     // adds to the "tail" end
queue.offer(2);
queue.offer(3);
System.out.println(queue.poll());    // 1 — FIFO
System.out.println(queue.peek());     // 2
```

### 10.3 Method name cheat sheet for `Deque`

| Operation | Stack usage (LIFO, one end) | Queue usage (FIFO, two ends) |
|-----------|---------------------------------|-----------------------------------|
| Add | `push(e)` (adds to head) | `offer(e)` / `add(e)` (adds to tail) |
| Remove | `pop()` (removes from head) | `poll()` / `remove()` (removes from head) |
| Peek | `peek()` (views head) | `peek()` (views head) |
| Add explicitly to either end | `addFirst(e)` / `addLast(e)` | `addFirst(e)` / `addLast(e)` |
| Remove explicitly from either end | `removeFirst()` / `removeLast()` | `removeFirst()` / `removeLast()` |

> `push`/`pop` always operate on the **head** end of a `Deque` — that's exactly why using them gives you stack (LIFO) behavior, while `offer`/`poll` operate head-to-tail, giving you queue (FIFO) behavior. Same underlying object, two different "access pattern" method sets.

---

## 11. Why `Stack` (the Class) Is Discouraged

`java.util.Stack` has existed since Java 1.0, but modern guidance (including from Java's own documentation) recommends `Deque`/`ArrayDeque` instead, for concrete reasons:

| Problem with `Stack` | Explanation |
|------------------------|--------------|
| **Extends `Vector`** | `Vector` is a legacy, **synchronized** (thread-safe) class from Java 1.0 — every single operation pays a locking overhead cost, even in single-threaded code that never needs it |
| **Exposes non-stack operations** | Since it `extends Vector`, it inherits methods like `get(index)`, `insertElementAt(...)`, and `remove(index)` — letting code reach into the "middle" of what's supposed to be a restricted-access structure, defeating the entire purpose (this is precisely the "favor composition over inheritance" lesson from the composition notes — `Stack extends Vector` is the textbook cautionary example) |
| **Slower in practice** | The synchronization overhead makes `Stack` measurably slower than `ArrayDeque` for ordinary, non-concurrent use |

```java
Stack<Integer> stack = new Stack<>();
stack.push(1);
stack.push(2);
stack.get(0);              // compiles and works! — but this breaks the "stack" abstraction entirely
stack.insertElementAt(99, 0);   // also compiles — inserting into the middle of a "stack"
```

```java
// Preferred modern equivalent:
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);
stack.push(2);
// no way to reach the middle — the interface simply doesn't expose it
```

> This mirrors exactly the `ArrayListBasedStack extends ArrayList` cautionary example from the composition and association notes — `Stack` is a real, standard-library instance of the same mistake.

---

## 12. `Deque`: The Unified Double-Ended Structure

`Deque` ("deck," short for **Double-Ended QUEue**) generalizes both stack and queue behavior into a single interface: elements can be added or removed from **either end**, in O(1).

```text
addFirst/removeFirst <- [ e1, e2, e3, ... ] -> addLast/removeLast
```

```java
Deque<Integer> deque = new ArrayDeque<>();
deque.addFirst(1);
deque.addLast(2);
deque.addFirst(0);
System.out.println(deque);          // [0, 1, 2]
System.out.println(deque.pollFirst());   // 0
System.out.println(deque.pollLast());     // 2
```

Because a `Deque` can do **everything** a stack or a plain queue can do (and more), `ArrayDeque` is the single, recommended, general-purpose choice for both — you rarely need a type dedicated to just one or the other in modern Java.

---

## 13. Variants Built on Top: Deque, Circular Queue, Priority Queue, Monotonic Stack/Queue

| Variant | What it adds | Typical use |
|---------|------------------|----------------|
| **Deque** (section 12) | Access at both ends | Sliding window problems, palindrome checking from both ends |
| **Circular queue** (section 7) | Reuses freed array space without shifting | Fixed-capacity buffering (producer-consumer, streaming windows) |
| **Priority queue** (heap, see the heap notes) | Elements come out by *priority*, not arrival order — not FIFO at all | Scheduling by priority, top-K problems, Dijkstra |
| **Monotonic stack** | A stack kept **always increasing or always decreasing** from bottom to top, by popping violating elements before each push | Next Greater/Smaller Element family (section 17.3) |
| **Monotonic deque** | A deque kept monotonic, with the ability to discard from *either* end | Sliding Window Maximum/Minimum (section 18.2) |

---

## 14. Types of Problems Stacks Solve

```text
- Matching/validating nested or paired symbols (parentheses, brackets, tags)
- Expression evaluation and conversion (infix/postfix/prefix)
- Undo/redo history, browser back/forward navigation
- Tracking "most recent unmatched/unresolved" state in general
- Next Greater/Smaller Element family (via a monotonic stack)
- Simulating/replacing recursion iteratively (DFS, backtracking, tree traversal without the call stack)
- Histogram-style "largest rectangle" and "trapping water" problems (monotonic stack)
- Function call management (the call stack itself — usually implicit, but sometimes modeled explicitly)
```

---

## 15. Types of Problems Queues Solve

```text
- Breadth-first search (BFS) on trees and graphs — guaranteeing strict layer-by-layer order
- Task/job scheduling in arrival order (round-robin scheduling, print queues, request handling)
- Sliding window problems (via a monotonic deque — see section 18.2)
- Caching strategies, e.g., certain LRU cache implementation styles
- Simulating real-world waiting lines, buffering, or producer-consumer scenarios
- Level-by-level processing of any layered/graph-like structure
```

---

## 16. Recognizing When to Use a Stack vs a Queue

```text
Trigger phrases for a STACK:

"matching" / "balanced" / "valid parentheses"       -> stack of opening symbols
"next greater/smaller element"                        -> monotonic stack
"undo" / "backtrack" / "most recent"                  -> LIFO order is exactly what's needed
"evaluate expression" (postfix/infix conversion)       -> operator/operand stack
"simulate recursion iteratively" / avoid stack overflow -> explicit stack replacing the call stack
"largest rectangle in histogram" / "trapping rain water" -> monotonic stack

Trigger phrases for a QUEUE:

"level order" / "shortest path in an UNWEIGHTED graph"  -> BFS, needs a queue
"first come first served" / "in order of arrival"        -> FIFO order is exactly what's needed
"sliding window maximum/minimum"                           -> monotonic deque
"generate in order" (e.g., numbers with only factors 2,3,5) -> queue-driven generation
```

The simplest litmus test: **does the problem care about the ORDER elements were added (FIFO), or does it care about handling the MOST RECENT thing first (LIFO)?** That question alone resolves the vast majority of stack-vs-queue decisions.

---

## 17. Common Stack Patterns

### 17.1 Pattern: Matching/Balanced Symbols

Push every "opening" symbol; on a "closing" symbol, check it matches the most recently pushed (unmatched) opener, then pop. An empty stack at the end means everything matched.

```java
boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character, Character> pairs = Map.of(')', '(', ']', '[', '}', '{');

    for (char c : s.toCharArray()) {
        if (pairs.containsValue(c)) {
            stack.push(c);                     // opening symbol
        } else if (pairs.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != pairs.get(c)) return false;   // mismatch or nothing to match
        }
    }
    return stack.isEmpty();                     // everything matched if nothing is left unclosed
}
```

### 17.2 Pattern: Expression Evaluation (Postfix/RPN)

Push operands; when an operator is seen, pop the two most recent operands, apply the operator, and push the result back.

```java
int evalRPN(String[] tokens) {
    Deque<Integer> stack = new ArrayDeque<>();
    for (String token : tokens) {
        if ("+-*/".contains(token) && token.length() == 1) {
            int b = stack.pop(), a = stack.pop();
            switch (token) {
                case "+" -> stack.push(a + b);
                case "-" -> stack.push(a - b);
                case "*" -> stack.push(a * b);
                case "/" -> stack.push(a / b);
            }
        } else {
            stack.push(Integer.parseInt(token));
        }
    }
    return stack.pop();
}
```

### 17.3 Pattern: Monotonic Stack — Next Greater/Smaller Element

Maintain a stack that is always **decreasing** (for "next greater") or **increasing** (for "next smaller") from bottom to top. For each new element, pop everything the new element "beats," recording that the popped elements' answer has just been found.

```java
int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();   // stores INDICES, kept decreasing by value

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
            result[stack.pop()] = nums[i];        // the popped index just found its "next greater"
        }
        stack.push(i);
    }
    return result;
}
```

This pattern is worked in full, with a hand-trace, in section 20.

### 17.4 Pattern: Simulating Recursion Iteratively

Any recursive DFS-style algorithm can be rewritten iteratively by maintaining an **explicit stack** holding exactly the state each recursive call would have needed — directly mirrored in the tree notes' iterative preorder traversal example.

---

## 18. Common Queue Patterns

### 18.1 Pattern: BFS Layer-by-Layer Processing

Covered in full in the tree notes (level-order traversal) — the same "snapshot queue size, process exactly that many before moving on" technique applies identically to graph BFS.

```java
void bfs(int start, Map<Integer, List<Integer>> graph) {
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();
    queue.add(start);
    visited.add(start);

    while (!queue.isEmpty()) {
        int node = queue.poll();
        for (int neighbor : graph.getOrDefault(node, List.of())) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.add(neighbor);
            }
        }
    }
}
```

### 18.2 Pattern: Monotonic Deque — Sliding Window Maximum/Minimum

Maintain a deque of **indices**, kept monotonically decreasing (for max) or increasing (for min) by value, and remove indices that have "fallen out" of the current window from the front.

```text
Core idea for SLIDING WINDOW MAXIMUM:
1. Before pushing a new index, pop from the BACK any index whose value is <= the new value
   (they can never be the max again while the new, bigger element is still in the window).
2. If the index at the FRONT has fallen outside the current window, pop it from the front.
3. The index at the FRONT is always the current window's maximum.
```

This pattern is worked in full, with a hand-trace, in section 20.

### 18.3 Pattern: Multi-Source BFS

Instead of seeding the queue with a single start node, seed it with **multiple** starting nodes simultaneously (e.g., every "0" cell in a grid), and run ordinary BFS from there — this naturally computes the shortest distance to the *nearest* of any source, for every node, in one single pass.

```java
Queue<int[]> queue = new LinkedList<>();
for (int r = 0; r < grid.length; r++)
    for (int c = 0; c < grid[0].length; c++)
        if (grid[r][c] == 0) queue.add(new int[]{r, c});   // seed with ALL sources at once
// then run normal BFS from here
```

---

## 19. Top 20 Stack & Queue Problems (By Pattern)

★ = worked in section 20; the rest are for you to solve.

### Matching / Balanced-Symbol family (Stack)

1. ★ Valid Parentheses
2. Minimum Add to Make Parentheses Valid
3. Remove Invalid Parentheses (harder variant)

### Expression Evaluation family (Stack)

4. Evaluate Reverse Polish Notation
5. Basic Calculator (with `+`, `-`, parentheses)
6. Decode String (nested, bracket-driven repetition)

### Monotonic Stack family

7. ★ Next Greater Element I
8. Daily Temperatures
9. Largest Rectangle in Histogram
10. Trapping Rain Water (stack-based variant)

### Stack-Simulates-Recursion family

11. Implement Queue using Stacks (and the reverse: Stack using Queues)
12. Min Stack (O(1) `getMin()` alongside normal stack operations)
13. Asteroid Collision

### BFS / Queue-Driven family

14. ★ Sliding Window Maximum
15. Binary Tree Level Order Traversal (see the tree notes for the full worked version)
16. Rotting Oranges (multi-source BFS)
17. Number of Islands (BFS/DFS on a grid)

### Design / Miscellaneous

18. Design Circular Queue
19. Implement a Queue using a fixed-size circular array
20. First Unique Character in a Stream (queue-assisted)

---

## 20. Sample Solutions (Worked)

### 20.1 Valid Parentheses

**Problem:** Given a string containing just the characters `(`, `)`, `{`, `}`, `[`, `]`, determine if the input string is valid (every opening bracket is closed by the same type, in the correct order).

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> closeToOpen = Map.of(')', '(', ']', '[', '}', '{');

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                // c is a closing bracket
                if (stack.isEmpty() || stack.pop() != closeToOpen.get(c)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();     // true only if every opener found a matching closer
    }
}
```

```text
s = "{[()]}"

'{' -> push -> stack: [{]
'[' -> push -> stack: [{, []
'(' -> push -> stack: [{, [, (]
')' -> pop '(' , matches closeToOpen[')']='(' -> OK -> stack: [{, []
']' -> pop '[' , matches closeToOpen[']']='[' -> OK -> stack: [{]
'}' -> pop '{' , matches closeToOpen['}']='{' -> OK -> stack: []

End of string, stack is empty -> return true
```

```text
s = "([)]"

'(' -> push -> stack: [(]
'[' -> push -> stack: [(, []
')' -> pop '[' , but closeToOpen[')']='(' , '[' != '(' -> MISMATCH -> return false
```

**Complexity:** O(n) time, O(n) space (worst case, all openers with no closers).

---

### 20.2 Next Greater Element I

**Problem:** For each element in `nums1` (a subset of `nums2`), find the **next greater element** to its right in `nums2`. If none exists, output `-1`.

```java
import java.util.*;

class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreaterMap = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();    // monotonically DECREASING stack of VALUES

        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                nextGreaterMap.put(stack.pop(), num);   // "num" is the next greater element for whatever we just popped
            }
            stack.push(num);
        }
        // anything left in the stack at the end has NO next greater element — stays absent from the map

        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nextGreaterMap.getOrDefault(nums1[i], -1);
        }
        return result;
    }
}
```

```text
nums2 = [1, 3, 4, 2]

Process 1: stack empty -> push 1 -> stack: [1]
Process 3: 1 < 3 -> pop 1, map[1]=3 -> stack empty -> push 3 -> stack: [3]
Process 4: 3 < 4 -> pop 3, map[3]=4 -> stack empty -> push 4 -> stack: [4]
Process 2: 4 > 2, stop popping -> push 2 -> stack: [4, 2]

End of array. Remaining in stack {4, 2} have NO next greater element.

map = {1: 3, 3: 4}

nums1 = [4, 1, 2]
result = [map.get(4, -1), map.get(1, -1), map.get(2, -1)] = [-1, 3, -1]
```

**Complexity:** O(n + m) time, where n = `nums2.length`, m = `nums1.length` — each element of `nums2` is pushed and popped **at most once**, which is exactly why a monotonic stack achieves O(n) overall despite the nested-looking `while` loop (this is a classic "amortized" analysis: the total work across all iterations of the inner `while` loop, summed over the entire outer loop, is bounded by n, not n²).

---

### 20.3 Sliding Window Maximum

**Problem:** Given an array `nums` and a window size `k`, return the maximum value in each sliding window of size `k` as it moves from left to right across the array.

```java
import java.util.*;

class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();   // stores INDICES, values kept monotonically DECREASING

        for (int i = 0; i < n; i++) {
            // 1. Remove indices that have fallen OUT of the current window from the FRONT
            if (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            // 2. Remove indices from the BACK whose values are <= the current value
            //    (they can never be the max again while nums[i] is still in the window)
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            // 3. Once the window is fully formed (i >= k-1), the FRONT of the deque is the current max
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }
}
```

```text
nums = [1,3,-1,-3,5,3,6,7], k = 3

i=0 (val=1):  deque:[0]                                            (window not full yet)
i=1 (val=3):  nums[0]=1<=3 -> pop 0 -> deque:[] -> push 1 -> deque:[1]
i=2 (val=-1): -1 < nums[1]=3, no pop -> push 2 -> deque:[1,2]
              window full (i>=2): max = nums[deque.front=1] = 3     -> result[0]=3
i=3 (val=-3): -3 < nums[2]=-1, no pop -> push 3 -> deque:[1,2,3]
              front index 1 <= i-k=0? 1<=0 false, still valid
              max = nums[1] = 3                                      -> result[1]=3
i=4 (val=5):  nums[3]=-3<=5 -> pop 3; nums[2]=-1<=5 -> pop 2; nums[1]=3<=5 -> pop 1 -> deque:[] -> push 4 -> deque:[4]
              max = nums[4] = 5                                      -> result[2]=5
i=5 (val=3):  3 < nums[4]=5, no pop -> push 5 -> deque:[4,5]
              front index 4 <= i-k=2? false, still valid
              max = nums[4] = 5                                      -> result[3]=5
i=6 (val=6):  nums[5]=3<=6 -> pop 5; nums[4]=5<=6 -> pop 4 -> deque:[] -> push 6 -> deque:[6]
              max = nums[6] = 6                                      -> result[4]=6
i=7 (val=7):  nums[6]=6<=7 -> pop 6 -> deque:[] -> push 7 -> deque:[7]
              max = nums[7] = 7                                      -> result[5]=7

result = [3, 3, 5, 5, 6, 7]
```

**Complexity:** O(n) time — each index is pushed and popped from the deque **at most once** across the entire run (same amortized argument as the monotonic stack above). O(k) space for the deque.

---

## 21. Common Pitfalls

| # | Pitfall | Fix |
|---|---------|-----|
| 1 | Using `java.util.Stack` in new code | Prefer `ArrayDeque` — `Stack` extends the legacy, synchronized `Vector` and exposes non-stack operations |
| 2 | Implementing a queue with a plain array and shifting elements on dequeue | O(n) per dequeue — use a circular buffer (modulo arithmetic) or a linked-list-based queue instead |
| 3 | Forgetting `push`/`pop` on a `Deque` operate on the HEAD, while `offer`/`poll` operate head-to-tail | Mixing them inconsistently can silently produce LIFO behavior where FIFO was intended, or vice versa |
| 4 | Checking `stack.isEmpty()` AFTER popping, instead of BEFORE | Popping from an empty stack throws an exception (`ArrayDeque`) or returns an incorrect sentinel — always check emptiness first |
| 5 | Assuming a monotonic stack/deque algorithm is O(n²) because of the nested `while` loop | Each element is pushed and popped **at most once** total — the amortized cost across the whole run is O(n), not O(n²) |
| 6 | Forgetting to remove "expired" indices from the FRONT of a monotonic deque in sliding-window problems | Without this check, the window can report a maximum/minimum from an element that's no longer actually in range |
| 7 | Using a stack where order of arrival (not most-recent-first) actually matters | That's exactly what a queue is for — reaching for a stack there inverts the processing order incorrectly |
| 8 | Implementing BFS with a stack instead of a queue by mistake | This silently turns BFS into something closer to DFS — the strict layer-by-layer guarantee is lost |
| 9 | Forgetting `LinkedList` also implements `Deque` and could be used, but has worse per-element overhead than `ArrayDeque` | Prefer `ArrayDeque` unless you specifically need `LinkedList`'s other `List` capabilities too |
| 10 | Confusing `PriorityQueue` with a plain FIFO `Queue` because of the name | `PriorityQueue` is a heap — elements come out by priority/ordering, never by arrival order; see the heap notes |

---

## 22. Quick Cheat Sheet

```text
STACK (LIFO)           push/pop/peek, all O(1), access restricted to ONE end (the top)
                       Real-world: plate stack, call stack, undo history

QUEUE (FIFO)            enqueue/dequeue/peek, all O(1), access restricted to TWO ends (front removal, rear insertion)
                       Real-world: ticket line, BFS layer processing, task scheduling

IMPLEMENTATION CHOICES   Array-based:  contiguous, cache-friendly, needs resizing logic
                       Linked-based: O(1) at the right ends with NO resizing, but per-node pointer overhead

ARRAY STACK              Trivial: track a single "top index"; push/pop = increment/decrement + access
ARRAY QUEUE               Needs a CIRCULAR BUFFER (index = (index + 1) % capacity) to avoid O(n) shifting
LINKED STACK              Push/pop at the HEAD of a singly linked list — O(1), no tail tracking needed
LINKED QUEUE              Enqueue at TAIL, dequeue at HEAD — needs an explicit tail reference for O(1) enqueue

JAVA BUILT-INS            Deque<E> / ArrayDeque<E>   <- RECOMMENDED for BOTH stack and queue use cases
                       push()/pop()      -> HEAD end     -> gives STACK (LIFO) behavior
                       offer()/poll()     -> tail/head     -> gives QUEUE (FIFO) behavior
                       java.util.Stack    -> AVOID: extends synchronized Vector, exposes non-stack ops
                       PriorityQueue      -> NOT a FIFO queue — it's a heap (see the heap notes)

KEY PATTERNS             Matching/balanced symbols         -> stack of openers, pop-and-compare on closers
                       Expression evaluation (RPN)          -> operand stack, apply operator on pop-pop-push
                       Next Greater/Smaller Element          -> MONOTONIC stack, O(n) amortized (not O(n^2)!)
                       BFS / shortest path (unweighted)       -> queue, strict layer-by-layer with size-snapshot
                       Sliding window max/min                 -> MONOTONIC deque, trim expired front + dominated back
                       Multi-source BFS                        -> seed the queue with ALL sources before starting
```

**Remember:**

1. Restricting access to specific ends isn't a limitation — it's what guarantees O(1) operations and makes the structure the natural, bug-resistant fit for LIFO or FIFO problems.
2. An array-based queue needs the circular-buffer (`% capacity`) trick to avoid O(n) shifting on every dequeue; a linked queue needs an explicit tail reference for O(1) enqueue.
3. Prefer `ArrayDeque` over `java.util.Stack` or `LinkedList` for both stack and queue needs in modern Java — `push`/`pop` for LIFO, `offer`/`poll` for FIFO, on the exact same object type.
4. Monotonic stacks and deques look like they cost O(n²) from the nested loop, but are actually O(n) amortized, since each element is pushed and popped at most once overall.
5. The fastest way to choose between the two: does the problem care about FIFO (arrival order) or LIFO (most-recent-first)? That question alone resolves almost every stack-vs-queue decision.
