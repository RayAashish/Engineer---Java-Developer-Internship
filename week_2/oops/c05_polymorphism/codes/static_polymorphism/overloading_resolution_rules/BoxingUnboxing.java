package week_2.oops.c05_polymorphism.codes.static_polymorphism.overloading_resolution_rules;

public class BoxingUnboxing {
    public static void main(String[] args) {
        Box box = new Box();
        box.show(12);
        box.show((Long) 2L);
        box.show((byte) 123);
    }
}

class Box{
    void show(Integer a){
        System.out.println("Integer");
    }

    void show(long a){
        System.out.println("long");
    }
}