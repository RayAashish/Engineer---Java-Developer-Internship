package week_2.oops.c05_polymorphism.codes.dynamic_polymorphism;

public class AccessModifierWidening {

}

class Animal{
    protected  void makeSound(){
        System.out.println("Generic Sound...........");
    }
}

// class Cat extends Animal{
//     @Override 
//     default void makeSound(){ //Compile time error as access can't be narrowed but only can be expended
//         System.out.println("meow meow");
//     }
// }

class Dog extends Animal{

    @Override 
    public  void makeSound() {
        System.out.println("Vow vow");
    }
}