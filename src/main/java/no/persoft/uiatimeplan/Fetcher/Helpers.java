package no.persoft.uiatimeplan.Fetcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by PerArne on 08.07.2014.
 */
public class Helpers {


    public static Date DateConvert(Integer weekNum, String dayString, String time)
    {



        HashMap<String,Integer> dayConvert = new HashMap<String, Integer>();
        dayConvert.put("Man", 2);
        dayConvert.put("Mon", 2);
        dayConvert.put("Tir", 3);
        dayConvert.put("Tue", 3);
        dayConvert.put("Ons", 4);
        dayConvert.put("Wed", 4);
        dayConvert.put("Tor", 5);
        dayConvert.put("Thu", 5);
        dayConvert.put("Fre", 6);
        dayConvert.put("Fri", 6);
        dayConvert.put("Lør", 7);
        dayConvert.put("Sat", 7);
        dayConvert.put("Løn", 7); // TODO - Error in UIA
        dayConvert.put("Søn", 1);
        dayConvert.put("Sun", 1);

        int dayInt = 0;
        try{
           dayInt = dayConvert.get(dayString);
        }catch (NullPointerException e)
        {
            e.printStackTrace();
            System.err.println("Error while converting date: " + dayString);
        }




        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.set(Calendar.DAY_OF_WEEK, dayInt);
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)); // TODO - Hardcode !
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split("\\.")[0])); // TODO? - testing
        cal.set(Calendar.MINUTE, Integer.parseInt(time.split("\\.")[1])); // TODO? -  testing
        cal.set(Calendar.SECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));

        return cal.getTime();
    }
}
