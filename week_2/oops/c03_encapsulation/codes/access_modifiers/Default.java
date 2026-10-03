package week_2.oops.c03_encapsulation.codes.access_modifiers;

import week_2.oops.c03_encapsulation.codes.access_modifiers.package1.Computer;

/**
 * Default
 * 
 */
public class Default {
    Computer computer = new Computer();
    // computer.accessed(); Will throw error because the Computer class 
    // is int different package than Default class

}
