package week_2.oops.c03_encapsulation.codes.access_modifiers;

import week_2.oops.c03_encapsulation.codes.access_modifiers.package2.Submarine;

public class Protected {
    /**
     * @param args
     * Can't access the submarine because it's in the
     * different package
     */
    public static void main(String[] args) {
        Submarine submarine = new Submarine();
        // submarine.setType("War");
        // String type = submarine.type();
        
    }
}
