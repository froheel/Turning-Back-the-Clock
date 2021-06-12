package question3;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SetDate {


    /**
     * Set Date: changes the date modified of the filename to the date and time in EST given as input
     * @param date
     * @param time
     * @param filename
     * @throws IllegalArgumentException
     */
    public static void setDate(String date, String time, String filename) throws IllegalArgumentException{

        //Making sure that date, time & filename are String
        if(date instanceof String && time instanceof String && filename instanceof String){

            /**
             *  extracting integers representing the month, day, year, hour, minute, second
             */

            // Making sure that the date matches the pattern of MM/DD/YYYY
            Pattern datePattern = Pattern.compile("(\\d{1,2})/(\\d{1,2})/(\\d{4})");
            Matcher m = datePattern.matcher(date);
            int month,day,year;

            // extracting and storing the values of month,day & year
            if(m.matches()){

                try {
                    // using indexOf & lastIndexOf methods
                    int delimiter1 = date.indexOf('/');
                    int delimiter2 = date.lastIndexOf('/');
                    int size = date.length();

                    // storing the values of month, day & year
                    month = parseInt(date.substring(0 ,delimiter1));
                    day = parseInt(date.substring(delimiter1+1,delimiter2));
                    year = parseInt(date.substring(delimiter2+1,size));
                }
                catch(NumberFormatException ex){
                    throw new IllegalArgumentException("Error in parsing the date");
                }
                catch (Exception ex){
                    throw new IllegalArgumentException("Error in parsing the time");
                }

            }
            else{
                throw new IllegalArgumentException("Date is not in the correct format");
            }

            //Making sure that the time matches the pattern of HH:MM:SS in 24 hour format
            Pattern timePattern = Pattern.compile("([01]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])");
            Matcher m2 = timePattern.matcher(time);
            int hour,minute,second;

            //extracting hour,minute & second from the 24 hour EST Time provided
            if(m2.matches()){
                try {
                    // using indexOf & lastIndexOf methods
                    int delimiter1 = time.indexOf(':');
                    int delimiter2 = time.lastIndexOf(':');
                    int size = time.length();

                    // storing the values of hour,minute & second
                    hour = parseInt(time.substring(0,delimiter1));
                    minute = parseInt(time.substring(delimiter1+1,delimiter2));
                    second = parseInt(time.substring(delimiter2+1,size));
                }
                catch (NumberFormatException ex){
                    throw new IllegalArgumentException("Error in parsing the time");
                }
                catch (Exception ex){
                    throw new IllegalArgumentException("Error in parsing the time");
                }
            }
            else{
                throw new IllegalArgumentException("Time is not in the correct format");
            }


            //Did not make a Date object because that API is depreciated. Directly made a Calendar Object
            //Checking if the date is actually a valid date or not
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            cal.setLenient(false);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month-1);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.HOUR_OF_DAY,hour);
            cal.set(Calendar.MINUTE,minute);
            cal.set(Calendar.SECOND,second);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
                sdf.format(cal.getTime());
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Date is not valid");
            }

            /**
             * Changing the last modified date of the file
             */
            try {
                // Checking if the file exists
                Path p = Paths.get(System.getProperty("user.dir") + "\\" + filename);
                boolean exists = Files.exists(p);
                if(!exists){
                    throw new IllegalArgumentException("File not found");
                }

                // Get the last modified date of the file in EST
                File file = p.toFile();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                System.out.println("Original Last Modified Date : "
                        + dateFormat.format(file.lastModified()));

                // Updating the date & Time of the file to the EST Time entered by the user
                SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
                newFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                file.setLastModified(cal.getTimeInMillis());
                System.out.println("New Modified Date : "
                        + newFormat.format(file.lastModified()));
            }
            catch (Exception e){
                throw new IllegalArgumentException("File not found");
            }
        }
        else{
            throw new IllegalArgumentException(" The data type of arguments is not String");
        }

    }


    /**
     * Turning back the clock : calls the setDate method and handles the IllegalArgument Exception
     * @param args
     */
    public static void main(String args[]){


        // Checks if length of the args is greater than or equal to 3
        if(args.length>=3){

            if(args.length>3){
                //Issue a warning if the length of args is greater than 3
                System.out.println("Warning! More than 3 arguments");
            }

            try{
                // call the setDate method with the CLI arguments
                setDate(args[0],args[1],args[2]);
            }
            catch (IllegalArgumentException ex){
                // printing the stack trace when there is an IllegalArgumentException
                // Also displaying the message
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }

        }
        else{
            // Message that arguments are less than 3
            System.out.println(" Invalid: The arguments are less than 3");
        }

    }

}
