package data_structures.stacks.codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackImplementation {
    public static void main(String[] args) {
        StackImp<Integer> s = new StackImp<>();
        s.push(12);
        s.push(9);
        s.push(19);
        System.out.println(s.peek());
        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.peek());
        System.out.println(s.pop());
        System.out.println(s.pop());
    }
}

class StackImp<T> {
    private List<T> list = list = new ArrayList<>();;

    public void push(T e){
        list.add(e);
    }

    public T pop(){
        if (list.isEmpty())
            throw new RuntimeException("Stack Underflow");
        T e = list.removeLast();
        return e;
    }
    
    public T peek(){
        if (list.isEmpty()) {
            throw new RuntimeException("Stack Underflow");
        }
        return list.getLast();
    }

    public int size(){
        return list.size();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }
}