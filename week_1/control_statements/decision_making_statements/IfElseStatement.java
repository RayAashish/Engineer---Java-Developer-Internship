package week_1.control_statements.decision_making_statements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IfElseStatement {

    private static void ifElseStatement(Short age){
        if (age >= 18){
            System.out.println("You're eligible for voting");
        } else {
            System.out.println("You're not eligible for voting");
        }
    }
    public static void main(String[] args) throws IOException{
        BufferedReader  reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your age: ");
        short age = Short.parseShort(reader.readLine());
        ifElseStatement(age);
        reader.close();
    }
}
