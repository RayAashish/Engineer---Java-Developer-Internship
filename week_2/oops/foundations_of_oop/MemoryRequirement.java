package week_2.oops.foundations_of_oop;
import week_2.oops.foundations_of_oop.BankAccount;

/**
 * @author Aashish Kumar Ray
 * MemoryRequirement 
 * When a class is created, it doesn't take any memory on itself 
 * & it stays in "Method Area or Metaspace" in java holding the varaibles, methods & all
 * Whereas in Object case :
 * The variable used to create the object is stored in stack keeping the reference of object
 * which is stored in heap memory 
 * Empty Object takes around 8-16 bytes
 */
public class MemoryRequirement {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount(10000.0, "Mr. Ray");
        System.out.println(acc);
    }
}
