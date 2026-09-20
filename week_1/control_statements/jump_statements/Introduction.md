### Jump statements in Java are control flow mechanisms that unconditionally transfer program execution from one part of the code to another. Java provides three main jump statements:
### 1. The break Statement 
The  break statement is used to immediately terminate the execution of the nearest loop (, , ) or a  block. Once encountered, control jumps to the statement right after that block. 

• Labeled Break: Java does not feature a  statement. However, you can combine  with a label to exit out of a deeply nested block or outer loop. [1]  

### 2. The  continue Statement 
The  continue statement skips the remaining code inside the current loop iteration and immediately jumps to the next evaluation step of that loop. 

• In a  loop, control jumps directly to the increment/decrement step. 
• In a  or  loop, control jumps straight to the conditional check. [5]  

### 3. The  return Statement 
The  return statement explicitly terminates the execution of the current method and passes control back to the caller. It can also optionally pass a data value back to the caller depending on the method's return type. 

• In a void method: Used without a value to exit the method early. 
• In a value-returning method: Must be followed by an expression matching the defined data type.   

### Comparison of Jump Statements 

| Jump Statement | Scope | Primary Behavior  |
| --- | --- | --- |
| — | Loops and  blocks | Completely terminates the loop or block.  |
| — | Loops only | Bypasses the rest of the current iteration.  |
| — | Methods | Terminates the active method and sends control back.  |
