package week_2.oops.contructors;

public class DeepAndShallowCopyConstructorProblem {
    public static void main(String[] args) {
        Address address = new Address("Laukahi");
        Student orginal = new Student("Aashish", address);
        Student copy = new Student(orginal);
        copy.address.city = "Hile";
        
        System.out.println(orginal.toString());
        System.out.println(copy.toString());
    }
}

class Address {
    String city;
    public Address(String city) {
        this.city = city;
    }
}

class Student {
    String name;
    Address address;

    public Student(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    /**
     * @param other
     * When there is change in the address of copy, it will change the address of orginal as well
     * so, to deal with this, we need to create a new object of address
     * But this problem can be solved by encapsulation as well
     */
    public Student(Student other) {
        this.name = other.name;
        // this.address = other.address;
        this.address = new Address(other.address.city);
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", address=" + address.city + "]";
    }

}