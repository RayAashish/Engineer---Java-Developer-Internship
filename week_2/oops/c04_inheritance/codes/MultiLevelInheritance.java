package week_2.oops.c04_inheritance.codes;

import java.time.LocalDate;

public class MultiLevelInheritance {
    
    public static void main(String[] args) {
        Child child = new Child();
        child.dob = LocalDate.of(2002, 8, 24);
        System.out.println(child.dob.toString());
    }
    
}

class GrandParent{
    public LocalDate dob;

    // GrandParent(LocalDate dob)

    public void investments(){
        System.out.println("Invested in Land");
    }
}

/**
 * Parent
 * {@class GrandParent} is being inherited by Parent Class
 */
class Parent extends GrandParent{

    @Override 
    public void investments(){
        System.out.println("Invested in Gold & Stocks");
    }
}

class Child extends Parent{

    @Override 
    public void investments(){
        System.out.println("Invested in NFTs & Crypto");
    }
}