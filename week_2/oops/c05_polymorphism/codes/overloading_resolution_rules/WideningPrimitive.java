package week_2.oops.c05_polymorphism.codes.overloading_resolution_rules;

public class WideningPrimitive {
    /**
     * @param args
     * Widening order for numeric types: byte → short → int → long → float → double
     * and char → int. The compiler always prefers the smallest widening step that
     * makes a match.
     */
    public static void main(String[] args) {
        Box box = new Box();
        box.show(1);
        box.show(4l);
        box.show(12.34);
        box.show((byte) 10);
        box.show((Long) 12L);
        box.show((short) 98);
        box.show((float) 23.3f);
    }
}

class Box{
    void show(int x){
        System.out.println("int");
    }
    void show(double x){
        System.out.println("double");
    }
    void show(long x){
        System.out.println("long");
    }
}