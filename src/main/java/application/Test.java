package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created By hiepnd
 * Date: 01/04/2021
 * Time: 4:20 PM
 * Contact me via mail hiepnd@vnpt-technology.vn
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        String testDateString = "02/04/2014";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            //format() method Formats a Date into a date/time string.
            Date d1 = df.parse(testDateString);
            System.out.println("Date: " + d1);
            System.out.println("Date in dd/MM/yyyy format is: "+df.format(d1));

        }
        catch (Exception ex ){
            System.out.println(ex);
        }
    }

}
