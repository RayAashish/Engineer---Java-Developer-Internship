# Week 2: Java Object-Oriented Programming (OOP) Laboratory

Welcome to the comprehensive Java Object-Oriented Programming (OOP) master suite. This repository contains the complete curriculum, source code labs, architectural design patterns, edge-case analysis, and interview mastery materials.

---

## 🚀 Primary Resource: OOP in One Go (Master Compendium)

For a unified, all-in-one guide combining all 11 modules, 38 runnable code files, memory models, JVM internals, and interview output questions into a single comprehensive manual:

👉 **[Navigate to `c00_oops-in-one-go/README.md`](c00_oops-in-one-go/README.md)**

---

## 📂 Laboratory Directory Structure

| Module Directory | Topic & Key Concepts | Hands-on Java Labs |
|---|---|:---:|
| **[`c00_oops-in-one-go`](c00_oops-in-one-go/)** | **The Unified Master Compendium & Complete Note** | **38 Integrated Labs** |
| [`c01_foundations_of_oops`](c01_foundations_of_oops/) | Procedural vs OOP, Classes, Heap/Stack Memory, Metaspace, Object Headers | 3 Source Codes |
| [`c02_constructors`](c02_constructors/) | Constructors, Overloading, Chaining `this()`, Shallow vs Deep Copy | 6 Source Codes |
| [`c03_encapsulation`](c03_encapsulation/) | Information Hiding, 4 Access Modifiers, Packages, Immutable Classes | 12 Source Codes |
| [`c04_inheritance`](c04_inheritance/) | `extends`, Single Inheritance, `super`, Method Overriding, Object Root | 4 Source Codes |
| [`c05_polymorphism`](c05_polymorphism/) | Static Overloading (4-Tier Rules) vs Dynamic Overriding (`vtable`) | 11 Source Codes |
| [`c06_abstraction`](c06_abstraction/) | Abstract Classes, Interfaces, Java 8/9 Default/Static/Private Methods | 2 Source Codes |
| [`c07_static_and_final_keyword`](c07_static_and_final_keyword/) | Class-level state, Static Blocks, Method Hiding, `final` Constants | Production Notes |
| [`c08_object_class_essential`](c08_object_class_essential/) | `toString()`, `equals()` 5 Axioms, `hashCode()` Distribution, Cloneable | Production Notes |
| [`c09_composition_and_association`](c09_composition_and_association/) | HAS-A vs IS-A, Weak vs Strong Ownership, Favor Composition Over Inheritance | Production Notes |
| [`c10_nested_and_inner_classes`](c10_nested_and_inner_classes/) | Static Nested, Member Inner (`Outer.this`), Local, Anonymous Classes | Production Notes |
| [`c11_enums_class_type`](c11_enums_class_type/) | `java.lang.Enum`, Constant-Specific Bodies, Switch Expressions, EnumSet | Production Notes |
| [`away_from_old_school_examples.md`](away_from_old_school_examples.md) | Modern domain modeling (Gaming, Smart Home, Sci-Fi, Creative Tools) | Design Manifest |

---

## 🛠️ Verification & Compilation

All source code files are standard standalone Java classes compatible with Java 8 through Java 21+.
To compile and execute any individual lab:

```bash
# Example: Executing the Shallow vs Deep Copy Constructor Lab
cd "week_2/oops/c02_constructors/codes"
javac DeepAndShallowCopyConstructorProblem.java
java DeepAndShallowCopyConstructorProblem

# Example: Executing the Polymorphic Overload Resolution Lab
cd "week_2/oops/c05_polymorphism/codes/static_polymorphism/overloading_resolution_rules"
javac BoxingUnboxing.java
java BoxingUnboxing
```
