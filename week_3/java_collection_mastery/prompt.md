### Role & Objective
Act as a Principal Java & JVM Systems Architect. Generate an exhaustive, interview-grade technical documentation file in Markdown format for **`[ArrayList]`** in the Java Collections Framework (JDK 17/21+ standard).

Do not give surface-level textbook definitions. Write this for a Backend Software Engineer preparing for high-stakes technical interviews. Focus heavily on JDK source-code internals, memory layout, CPU cache behavior, and edge cases. Use the author/attribution tag `raysi` in any headers, metadata, or package declarations (`com.raysi.jcf`).

### Strict Markdown Structure Required:

# 1. Architectural Overview & Hierarchy
- Exact package and class signature (including Generics, `extends`, and `implements` clauses).
- **ASCII Inheritance Diagram** showing where `[TARGET_CLASS]` sits in the hierarchy.
- **Why does this class exist?** What exact problem does it solve that sibling classes fail at?

# 2. Internal Data Structure & Memory Layout (JDK Source Code Level)
- Show the exact internal fields from the JDK source code (e.g., `transient Object[] elementData`, `Node<E>`, `modCount`, default constants).
- **ASCII Memory Diagram** illustrating how elements/nodes are laid out in Heap memory and how pointers/references connect them.
- **CPU Cache & Memory Overhead Analysis:** Explain pointer overhead, contiguous vs. scattered heap allocation, and memory footprint per element.

# 3. Step-by-Step Internal Working of Core Operations
Walk through the exact algorithmic execution under the hood for:
1. **Instantiation (Constructors):** Lazy vs. eager allocation, default capacity, and load factors.
2. **Insertion (`add` / `offer` / `put`):** Normal execution + exact math/bitwise operations used during **Resizing / Growing / Balancing**.
3. **Retrieval & Deletion (`get` / `poll` / `remove`):** Shifting overhead, pointer rewiring, or tree rebalancing, and how GC (Garbage Collection) memory leaks are prevented (e.g., nulling out obsolete references).

# 4. Complexity & Behavioral Matrix
Create a Markdown table covering:
- Operations (Insert at head, Insert at tail, Insert at middle, Lookup by index/key, Search by value, Delete)
- Average Time Complexity
- Worst-Case Time Complexity
- Amortized Time Complexity (if applicable)
- Ordering Guarantee, `null` element allowance, Duplicate allowance, and Iterator type (Fail-Fast vs. Fail-Safe).

# 5. Concurrency & Thread-Safety Mechanics
- How does `modCount` trigger `ConcurrentModificationException`?
- Why is this class unsafe (or safe) in multi-threaded environments? What is the exact modern concurrent alternative (`java.util.concurrent`), and how does it differ?

# 6. Production-Grade Java Code Verification
Provide a single, self-contained, runnable Java file (`package com.raysi.jcf...`) demonstrating:
- Non-obvious internal behavior, edge cases, or performance traps (no basic `add("Apple")` tutorials).
- Include clear comments explaining what happens in memory at each step.

# 7. Top 7 High-Signal Interview Questions & Deep Answers
Include 7 tricky, scenario-based or source-code-level interview questions (e.g., "What happens under the hood if...", "Why did JDK engineers choose X over Y..."). Provide concise, senior-level answers for each.




When you generate the notes for ArrayList, your codes/ package shouldn't just contain basic .add() and .remove() calls. Here is an example of the kind of file (SubListMemoryLeakDemo.java) you will put inside com/raysi/jcf/list/arraylist/codes/ to verify internal architecture:

package com.raysi.jcf.list.arraylist.codes;

import java.util.ArrayList;
import java.util.List;
``` java
/**
 * Author: raysi
 * Demonstrates how ArrayList.subList() does NOT create a new detached list,
 * but returns a view backed by the original parent array.
 */
public class SubListMemoryLeakDemo {
    public static void main(String[] args) {
        List<String> mainList = new ArrayList<>();
        mainList.add("Node-0");
        mainList.add("Node-1");
        mainList.add("Node-2");
        mainList.add("Node-3");

        // subList returns an internal 'SubList' class holding a reference to 'mainList'
        List<String> subView = mainList.subList(1, 3); // [Node-1, Node-2]
        
        // 1. Mutating the subList directly mutates the parent ArrayList!
        subView.set(0, "MUTATED-NODE-1");
        System.out.println("Parent List after subList mutation: " + mainList);

        // 2. Structural modification on the parent list invalidates the subList!
        mainList.add("Node-4"); // Increments parent modCount

        try {
            // SubList checks if parent.modCount == expectedModCount -> Throws CME!
            System.out.println(subView.get(0)); 
        } catch ( java.util.ConcurrentModificationException e) {
            System.out.println("Caught CME! Structural change in parent broke the subList view.");
        }
    }
}
```