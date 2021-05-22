package MyWebShop.model;

import java.util.Objects;

public class Book {
    private  int id;
    private  String name;
    private Double price;


   public Book(){}
   public Book(int id, String name, Double price){
       this.id=id;
       this.name=name;
       this.price=price;
   }

   public  int getId(){return id;}
   public  void setId(int id){this.id=id;}

   public  String getName(){return name;}
   public  void setName(String name){this.name=name;}

   public Double getPrice(){return price;}
   public void setPrice(Double Price){this.price=price;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                Objects.equals(name, book.name)&&
                price.equals(book.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

}
