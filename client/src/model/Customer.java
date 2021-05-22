package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private  int id;
    private String firstName;
    private String lastName;
    private double balance;
    private List<Book> myBooks;


    public Customer(){}
    public Customer(int id, String firstName,String lastName,double balance){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.balance=balance;
        myBooks=new ArrayList<>();
    }
    public Customer(int id){
        this.id=id;
        myBooks=new ArrayList<>();

    }


    public  int getId(){return id;}
    public  void setId(int id){this.id=id;}

    public  String getFirstName(){return firstName;}
    public  void setFirstName(String firstName){this.firstName=firstName;}

    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName=lastName;}

    public List<Book> getMyBooks(){return myBooks;}
    public void setMyBooks(List<Book> myBooks){this.myBooks=myBooks;}

    public double getBalance(){return balance;}
    public void setBalance(double balance1){this.balance=balance1;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(firstName, customer.firstName)&&
                Objects.equals(lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName,lastName);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", first name='" + firstName + '\'' +
                ", last name=" + lastName +
                ", balance="+balance+
                '}';
    }






}
