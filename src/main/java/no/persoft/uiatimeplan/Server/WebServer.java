package no.persoft.uiatimeplan.Server;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import no.persoft.uiatimeplan.Server.Servlets.CourseServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;

/**
 * Created by PerArne on 16.07.2014.
 */
public class WebServer {

    public WebServer()
    {
        // Do caching
        CourseServlet.Cache();

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "no.persoft.uiatimeplan.Server.Servlets");//Set the package where the services reside
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

        //String webDir = this.getClass().getClassLoader().getResource("src/main/java/no/persoft/uiatimeplan/Server/www").toExternalForm();
        try {

            Server server = new Server(4444);

            /* Course API */
            ServletContextHandler context = new ServletContextHandler(server, "/api", ServletContextHandler.SESSIONS);
            context.addServlet(sh, "/*");

            /* Web Root */
            ResourceHandler resource1 = new ResourceHandler();
            resource1.setResourceBase(new ClassPathResource("www").getURI().toString());
            //resource1.setBaseResource(Resource.newResource("src/main/java/no/persoft/uiatimeplan/Server/www"));
            ContextHandler context1 = new ContextHandler();
            context1.setContextPath("/");
            context1.setHandler(resource1);


            /* Create a Context Collection */
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(new Handler[] {
                    context,
                    context1
            });
            server.setHandler(contexts);

            /* Start the server */
            server.start();
            System.err.println(server.dump());
            server.join();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }

    
}
