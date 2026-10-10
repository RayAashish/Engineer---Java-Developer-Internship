package week_2.oops.c05_polymorphism.codes.dynamic_polymorphism;

public class ReturnTypeCovarience {
    public static void main(String[] args) {
        Animal dogAnimal = new Dog();
    }
}

class Animal {
    Animal reproduce() { return new Animal(); }
}
class Dog extends Animal {
    @Override
    Dog reproduce() {           // Dog is a subtype of Animal — valid covariant override
        return new Dog();
    }
}