package MyWebShop.controllers;

import MyWebShop.model.Book;
import MyWebShop.model.Customer;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("customers")

public class CustomerController {
    private List<Customer> customers=new ArrayList<>();
    private BookController bookController;

    public CustomerController(){
        bookController=new BookController();

        customers.add(new Customer(1,"Anh","Le",50));
        customers.add(new Customer(2,"Mara","Coman",60));
        customers.add(new Customer(3,"Barra","Fattah",70));

    }


    public Customer getCustomer(int id){
        for(Customer customer:customers){
            if (customer.getId()==id)
                return customer;
        }
        return null;
    }

    public boolean exists(int id){
        for (Customer customer:customers){
            if (customer.getId()==id)
                return  true;
        }
        return  false;
    }

    //Check if the book is already existed in the bookList of customer
    public boolean checkBookExists(Customer customer, Book bookToCheck) {
        boolean bookExisted=false;
        for(Book book:customer.getMyBooks()){
            if (book==bookToCheck){
                bookExisted=true;
                break;
            }
        }
        return bookExisted;
    }



    @POST //POST at http://localhost:XXXX/customers/entity should be id=5, firstName="Ahmad", lastName="Akrad", balance=90
    @Path("/addCustomer")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response createCustomerForm(@FormParam("id") int id,@FormParam("firstName") String firstName,@FormParam("lastName") String lastName,@FormParam("balance") double balance) {
        if (this.exists(id)){
            return Response.serverError().entity("Error post/create: Customer with id "+ id+" already exists!" ).build();
        }
        customers.add(new Customer(id,firstName,lastName,0));
        return Response.noContent().build();
    }

    @DELETE //DELETE at http://localhost:XXXX/webshop/{id}/deleteCustomer.
    @Path("/{id}/deleteCustomer")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer customer =this.getCustomer(id);
        if (customer!=null){
            customers.remove(customer);
            return Response.noContent().build();
        }else {
            return Response.serverError().entity("Cannot find customer with id "+id+"!").build();
        }
    }

    @GET //GET at http://localhost:XXXX/webshop/getCustomer.
    @Path("/getCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerQuery(@QueryParam("cusId") int id) {
        Customer customer=this.getCustomer(id);
        if (customer!=null){
            return Response.ok(customer).build();
        }
        else {
            return Response.serverError().entity("Cannot find customer with id "+id+"!").build();
        }
    }

    @GET //GET at http://localhost:XXXX/webshop/allCustomers
    @Path("/allCustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        GenericEntity<List<Customer>> entity=new GenericEntity<>(customers){};
        return Response.ok(entity).build();
    }

    @PUT //PUT at http://localhost:XXXX/webshop/updateCustomer
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomerJSON(Customer customer){
        Customer existingCustomer=this.getCustomer(customer.getId());
        if (existingCustomer!=null){
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setMyBooks(customer.getMyBooks());
            existingCustomer.setBalance(customer.getBalance());
            return Response.noContent().build();
        }else{
           return Response.serverError().entity("Customer with id "+customer.getId()+" does not exist.").build();
        }
    }

    @PUT //PUT at http://localhost:XXXX/webshop/orderBook.
    @Path("/orderBook")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response orderBook(@QueryParam("cusId") int cusId, @QueryParam("bookId") int bookId){
        Customer existingCustomer=this.getCustomer(cusId);
        Book existingBook=bookController.getBook(bookId);
        if (existingBook==null){
            return Response.serverError().entity("Cannot find book with id "+existingBook.getId()+"!").build();
        } else if (existingCustomer==null){
            return  Response.serverError().entity("Cannot find customer with id "+existingCustomer.getId()+"!").build();
        } else if (this.checkBookExists(existingCustomer,existingBook)){
            return Response.serverError().entity("Order failed! Book with id "+existingBook.getId()+" existed in the booklist of customer with id "+existingCustomer.getId()).build();
        }else if(existingCustomer.getBalance()<existingBook.getPrice()){
            return Response.serverError().entity("Order failed! Customer with id "+existingCustomer.getId()+" does not have enough balance to order book with id "+existingBook.getId()).build();
        } else{
            existingCustomer.getMyBooks().add(existingBook);
            double newBalance=existingCustomer.getBalance()-existingBook.getPrice();
            existingCustomer.setBalance(newBalance);
            return Response.noContent().build();
        }
    }

    @GET //GET at http://localhost:XXXX/webshop/allBooksByCustomer?cusId=
    @Path("/allBooksByCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookFromCustomer(@QueryParam("id") int cusId){
        Customer existingCustomer=this.getCustomer(cusId);
        if (existingCustomer==null){
            return  Response.serverError().entity("Cannot find customer with id "+cusId+"!").build();
        } else {
            GenericEntity<List<Book>> entity=new GenericEntity<>(existingCustomer.getMyBooks()){};
            return Response.ok(entity).build();
        }
    }

    @DELETE
    @Path("/deleteBookFromCustomer")
    public Response deleteBookFromCustomer(@QueryParam("cusId") int cusId,@QueryParam("bookId") int bookId){
        Customer existingCustomer=this.getCustomer(cusId);
        Book existingBook=bookController.getBook(bookId);
        if (existingBook==null){
            return Response.serverError().entity("Cannot find book with id "+cusId+"!").build();
        } else if (existingCustomer==null){
            return  Response.serverError().entity("Cannot find customer with id "+bookId+"!").build();
        } else if (!this.checkBookExists(existingCustomer,existingBook)){
            return Response.serverError().entity("Delete failed! Book with id "+bookId+" does not exist in the booklist of customer with id "+cusId).build();
        } else{
            existingCustomer.getMyBooks().remove(existingBook);
            return Response.noContent().build();
        }
    }

}
