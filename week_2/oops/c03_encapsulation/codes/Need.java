package week_2.oops.c03_encapsulation.codes;

public class Need {
    public static void main(String[] args) {
        Account account = new Account();
        // System.out.println(account.balance); //Compile time error because no one can modify the balance
        account.depositBalance(200.15);
        System.out.println(account.getBalance());
    }
}

class Account{
    private double balance = 0;

    public void depositBalance(double amount){
        if (this.balance < 0)
            throw new RuntimeException("Balance can't be negative");
        this.balance += amount;
    }

    double getBalance(){
        return this.balance;
    }
}