import MyWebShop.controllers.CustomerController;
import MyWebShop.controllers.BookController;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Publisher {
    public static void main(String[] args){
        int port=9090;
        URI baseUri= UriBuilder.fromUri("http://localhost/").port(port).build();
        ResourceConfig controllerConfig=new ResourceConfig(CustomerController.class, BookController.class);
        JdkHttpServerFactory.createHttpServer(baseUri,controllerConfig,true);
        System.out.println("Hosting service at http://localhost:"+port);
    }

}
