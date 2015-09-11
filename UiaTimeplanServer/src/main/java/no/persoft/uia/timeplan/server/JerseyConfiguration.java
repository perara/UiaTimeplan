package no.persoft.uia.timeplan.server;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by PerArne on 16.12.2014.
 */
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        packages("no.persoft.uia.timeplan.server.resources");
        property("com.sun.jersey.api.json.POJOMappingFeature", true);
        register(JacksonJsonProvider.class);
    }
}
