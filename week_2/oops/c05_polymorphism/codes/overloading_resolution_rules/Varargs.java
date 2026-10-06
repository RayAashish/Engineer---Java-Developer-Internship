package week_2.oops.c05_polymorphism.codes.overloading_resolution_rules;

public class Varargs {
    public static void main(String[] args) {
        Box box = new Box();
        box.show(2, 1, 9);
        box.show(2, 3, 4, 8);
    }
}

class Box{
    void show(int a, int b, int c){
        System.out.println("int");
    }

    void show (int... nums){
        System.out.println("Variable arguments");
    }
}