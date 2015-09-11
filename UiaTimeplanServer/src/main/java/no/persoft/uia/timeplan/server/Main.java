package no.persoft.uia.timeplan.server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.io.InputStream;

/**
 * Created by PerArne on 19.12.2014.
 */
@ApplicationPath("/")
public class Main extends Application {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {


        ///
        /// LOG4J Configuration
        ///
        InputStream iss = Main.class.getResourceAsStream("/log4j.properties");
        PropertyConfigurator.configure(iss);


        //////////////////////////////////////////////////////
        ///
        /// Server Configuration
        ///
        //////////////////////////////////////////////////////
        // Multithread
        //QueuedThreadPool threadPool = new QueuedThreadPool(200, 10);
        Server server = new Server(); //threadPool


        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //////////////////////////////////////////////////////
        /// HTTP Connector
        //////////////////////////////////////////////////////
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory());
        http.setPort(80);
        http.setIdleTimeout(15000);


        //////////////////////////////////////////////////////
        ///
        /// API Servlet
        /// With API Handler
        ///
        //////////////////////////////////////////////////////
        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("javax.ws.rs.Application", "no.persoft.uia.timeplan.server.JerseyConfiguration");

        servletHandler.addServlet(sh, "/api/*");
        servletHandler.setWelcomeFiles(new String[] { "index.html" });

        //////////////////////////////////////////////////////
        ///
        /// Web Application - Javascript
        ///
        //////////////////////////////////////////////////////
        ServletHolder timePlanApp = new ServletHolder(DefaultServlet.class);
        timePlanApp.setInitParameter("resourceBase",
                Main.class.getClassLoader().getResource("web").toExternalForm());
        timePlanApp.setInitParameter("dirAllowed","false");
        timePlanApp.setInitParameter("pathInfoOnly", "true");
        servletHandler.addServlet(timePlanApp, "/*");




        //////////////////////////////////////////////////////
        ///
        /// Join Handlers
        /// API + Static
        //////////////////////////////////////////////////////
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { servletHandler});
        server.setHandler(handlers);

        server.setConnectors(new Connector[] { http});
        server.start();
        server.join();




    }





}
