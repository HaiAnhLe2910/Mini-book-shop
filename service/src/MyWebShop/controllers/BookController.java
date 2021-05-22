package MyWebShop.controllers;

import MyWebShop.model.Book;

import javax.inject.Singleton;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("books")
public class BookController {
    private List<Book> bookList=new ArrayList<>();

    public BookController(){
        bookList.add(new Book(1,"Lords of the ring",10.5));
        bookList.add(new Book(2,"The best of me ",12.5));
        bookList.add(new Book(3,"The choice",20.5));
        bookList.add(new Book(4,"Message in a Bottle",25.5));
    }

    public Book getBook(int id){
        for (Book book:bookList){
            if (book.getId()==id)
                return  book;
        }
        return  null;
    }

    public  boolean exists(int id){
        for (Book book:bookList){
            if (book.getId()==id){
                return true;
            }
        }
        return false;
    }

    @POST //POST at http://localhost:XXXX/webshop/addBook.
    @Path("/addBook")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addBook(Book book) {
        if (this.exists(book.getId())){
           return Response.serverError().entity("Error post/create: Book with id "+ book.getId()+" already exists!" ).build();
        }
        bookList.add(book);
        return Response.noContent().build();
    }

    @DELETE //DELETE at http://localhost:XXXX/webshop/{id}/deleteBook
    @Path("/{id}/deleteBook")
    public Response deleteBook(@PathParam("id") int id) {
        Book book =this.getBook(id);
        if (book !=null){
            bookList.remove(book);
            return Response.noContent().build();
        }else {
            return Response.serverError().entity("Cannot find book with id "+id).build();
        }
    }

    @GET //GET at http://localhost:XXXX/webshop/getBook
    @Path("/getBook")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookQuery(@QueryParam("id") int stNr) {
        Book book =this.getBook(stNr);
        if (book !=null){
            return Response.ok(book).build();
        }else {
            return Response.serverError().entity("Cannot find book with id "+stNr).build();
        }
    }

    @GET //GET at http://localhost:XXXX/webshop/allBooks
    @Path("/allBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        GenericEntity<List<Book>> entity=new GenericEntity<>(bookList){};
        return Response.ok(entity).build();
    }

    @PUT //PUT at http://localhost:XXXX/webshop/updateBook
    @Path("/updateBook")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBookJSON(Book book){
        Book existingBook=this.getBook(book.getId());
        if (existingBook!=null){
            existingBook.setName(book.getName());
            existingBook.setPrice(book.getPrice());
            return  Response.noContent().build();
        }else{
            return Response.serverError().entity("Book with id "+book.getId()+" does not exist.").build();
        }

    }
}
