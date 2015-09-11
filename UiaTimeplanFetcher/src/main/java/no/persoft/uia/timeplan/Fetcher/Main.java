package no.persoft.uia.timeplan.Fetcher;

import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

import java.io.InputStream;


public class Main {

    public static void main(String[] args){

        ///
        /// LOG4J Configuration
        ///
        InputStream iss = Main.class.getResourceAsStream("/log4j.properties");
        PropertyConfigurator.configure(iss);



        Fetcher fetchCourse = new Fetcher(false);
        Fetcher fetchSingle = new Fetcher(true);
        try {
            fetchCourse.run();
            fetchSingle.run();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}