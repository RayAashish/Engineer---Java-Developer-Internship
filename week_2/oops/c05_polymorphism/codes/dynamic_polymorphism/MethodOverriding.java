package week_2.oops.c05_polymorphism.codes.dynamic_polymorphism;

public class MethodOverriding {
    public static void main(String[] args) {
        Animal[] animals = {new Dog(), new Cat(), new Cow()};
        for (Animal a : animals){
            a.makeSound();//same method for different animals applies differenctly
        }
    }
}

class Animal{
    void makeSound(){
        System.out.println("Generic sound");
    }
}

class Dog extends Animal{
    @Override 
    void makeSound(){
        System.out.println("bhow bhow");
    }
}

class Cat extends Animal{
    @Override 
    void makeSound(){
        System.out.println("meow meow");
    }
}

class Cow extends Animal{
    @Override 
    void makeSound(){
        System.out.println("mooo mooo");
    }
}