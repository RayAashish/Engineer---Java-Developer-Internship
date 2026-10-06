package week_2.oops.c05_polymorphism.codes.static_polymorphism;

public class Polymorphism {
    public static void main(String[] args) {
        Circle circle = new Circle();
        circle.radius = 12.4;
        System.out.println(circle.area());
    }
}

class Shape{
    double area(){
        return 0;
    }
}

class Circle extends Shape{
    double radius;
    @Override 
    double area(){
        return Math.PI * Math.pow(radius, 2);
    }

}