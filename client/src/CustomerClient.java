import model.Book;
import model.Customer;
import javafx.application.Application;
import javafx.stage.Stage;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyInvocation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerClient extends Application {

    private static Client client;
    private static WebTarget serviceTarget;
    private static Scanner scanner;
    private static boolean loggedIn;
    private static int customerId;


    public static void main(String[] args) {


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        client = ClientBuilder.newClient(new ClientConfig());
        serviceTarget = client.target(UriBuilder.fromUri("http://localhost:9000").build());
        scanner=new Scanner(System.in);
        customerId=-1;
        loggedIn=false;

        showInitialMenu();
        displayMenu();

        }


    private static void showInitialMenu(){
        System.out.println("Welcome to our book shop!");
        System.out.println("Do you have a customer id? (Y/N) ");
        String userInput=scanner.nextLine();
        switch(userInput){
            case "Y":
                login();
               break;
            case "N":
                showRegisterForm();
                break;
            default:
                System.out.println("Wrong input");
                break;

        }

    }

    private static void login() {
        System.out.println("Please input your id: ");
        String userInputId = scanner.nextLine();
        customerId=Integer.parseInt(userInputId);
        loggedIn=true;
        displayMenu();

    }


    private static void showRegisterForm() //CREATE
    {
        System.out.println("Please enter a new id: ");
        int newId=scanner.nextInt();
        System.out.println();


        System.out.println("Please add your first name:");
        String firstName=scanner.nextLine();


        System.out.println("Please add your balance:");
        Double balance = scanner.nextDouble();

        System.out.println("Please add your last name:");
        String lastName=scanner.nextLine();


        //using form in POST
        Form form=new Form();
        form.param("id",Integer.toString(newId));
        form.param("firstName",firstName);
        form.param("lastName",lastName);
        form.param("balance",Double.toString(balance));

        Entity<Form> entity=Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);

        Response response =serviceTarget.path("customers/addCustomer").request().post(entity);
        if (response.getStatus()==Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("Created customer with  "+newId+"!");
            loggedIn=true;
            customerId=newId;
        }else{
            System.out.println("ERROR: " + response.readEntity(String.class));
        }
    }



    private static void orderBook(){
        System.out.println("Please input the book id:");
        int bookId=scanner.nextInt();

        Customer customer=new Customer(customerId);

        Entity<Customer> entity1=Entity.entity(customer, MediaType.APPLICATION_JSON);

        Response response = serviceTarget.path("customers/orderBook").queryParam("cusId",customerId).queryParam("bookId",bookId).request().put(entity1);


        if (response.getStatus()==Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("Updated list of books with book id "+ bookId);
        }else {
            System.out.println("ERROR message: "+response.readEntity(String.class));
        }
    }



    private static void removeCustomerBooks(){
        System.out.println("Enter book id to remove from your list: ");
        String removeBookId = scanner.nextLine();

        WebTarget resourceTarget=serviceTarget.path("customers").path("/deleteBookFromCustomer").queryParam("cusId",customerId).queryParam("bookId",removeBookId);
        JerseyInvocation.Builder requestBuilder= (JerseyInvocation.Builder) resourceTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response= requestBuilder.delete();

        if (response.getStatus()==Response.Status.NO_CONTENT.getStatusCode())
        {
            System.out.println("Deleted book with id " + removeBookId+" from the customer with id "+customerId);

        } else{
            System.out.print("ERROR: " + response.readEntity(String.class));
        }
    }

    private static void showCustomerBook(){

        JerseyInvocation.Builder requestBuilder= (JerseyInvocation.Builder) serviceTarget.path("/customers/allBooksByCustomer").queryParam("id",customerId).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            GenericType<ArrayList<Book>> genericType=new GenericType<>(){};
            ArrayList<Book> entity=response.readEntity(genericType);
            for(Book b:entity){
                System.out.println(b);
            }
        }
        else
            System.out.println(response);
    }
    private static void displayMenu(){
        System.out.println("Press 1: Order book"); //PUT
        System.out.println("Press 2: Delete book from my booklist"); //DELETE
        System.out.println("Press 3: Display my books"); //GET
        String userInput=scanner.nextLine();
        switch (userInput){
            case "1":
                orderBook();
                break;
            case "2":
                removeCustomerBooks();
                break;
            case "3":
                showCustomerBook();
                break;
            default:
                System.out.println("Wrong input");
                break;

        }


    }








}
