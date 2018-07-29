package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    /**
     * The README text for the -README option parameter. The code for the String.join and
     * System.getProperty was found here:
     *       https://stackoverflow.com/questions/878573/java-multiline-string
     */
    static private final String README = String.join(
        System.getProperty("line.separator"),
        "Assignment: Project 4",
        "Author: Manpreet Bahl",
        "This project extends the Phone Bill application to provide a REST-ful web services.",
        "This classes utilizes the PhoneCall, PhoneBill, and PrettyPrinter classes that were",
        "developed throughout the previous 3 iterations. There are some modifications made to",
        "PhoneBill and PrettyPrinter. The PhoneBill class now returns a Collection of PhoneCalls",
        "that are within a specified Date range. The PrettyPrinter supports a constructor that",
        "takes in a PrintWriter object and pretty dumps it using that object. The other",
        "constructor and dumps() method still use BufferedWriter and filepaths as legacy support.",
        "The following is the usage for this application: ",
        usage()
    );

    public static void main(String... args) {
        String hostName = null;
        String portString = null;

        //Name of the customer
        String customer = null;

        //Caller's phone number
        String callerNum = null;

        //Callee's phone number
        String calleeNum = null;

        //The date when the call began
        String startDate = null;

        //The time when the call began
        String startTime = null;

        //Start time AM or PM
        String startMarker = null;

        //The date when the call ended
        String endDate = null;

        //The time when the call ended
        String endTime = null;

        //End time AM or PM marker
        String endMarker = null;

        //Should the phone call to add be displayed?
        boolean toPrint = false;

        //Where to start in the args array
        int startIndex = 0;

        //The PhoneCall object to add
        PhoneCall toAdd;

        //The PhoneBill object
        PhoneBill bill;

        //Is the command a search?
        boolean toSearch = false;

        //Make sure there are command line arguments
        if(args.length == 0){
            errorAndExit("Missing command line arguments");
        }

        /*If the command line arguments contains -README, display the README
          and exit with status of 0
         */
        if (containsOption(args, "-README")) {
            PrintStream ps = System.out;
            ps.println(README);
            System.exit(0);
        }

        /*If the command line argument contains -print, set the flag to print to true
         This also increments the startIndex for command line parsing by 1.
         */
        if (containsOption(args, "-print")){
            ++startIndex;
            toPrint = true;
        }

        /*If the command line argument contains -search, set the flag to search to true
         This also increments the startIndex for command line parsing by 1.
         */
        if (containsOption(args, "-search")){
            ++startIndex;
            toSearch = true;
        }

        //Get the remaining commands from the args array
        for(int i = startIndex; i < args.length; i++){
            //If the command line argument starts with "-", then it's an invalid command
            if(args[i].startsWith("-")){
                errorAndExit("Invalid command: " + args[i]);
            }
            if (customer == null && !toSearch){
                customer = args[i];
            } else if (callerNum == null && !toSearch){
                callerNum = args[i];
            } else if (calleeNum == null && !toSearch){
                calleeNum = args[i];
            } else if (startDate == null) {
                startDate = args[i];
            } else if (startTime == null) {
                startTime = args[i];
            } else if (startMarker == null){
                startMarker = args[i];
            } else if (endDate == null){
                endDate = args[i];
            } else if (endTime == null) {
                endTime = args[i];
            } else if (endMarker == null) {
                endMarker = args[i];
            } else{
                errorAndExit("Too many arguments entered");
            }
        }

        if (customer == null && !toSearch){
            errorAndExit("Missing customer name");
        } else if (callerNum == null && !toSearch){
            errorAndExit("Missing caller phone number");
        } else if (calleeNum == null && !toSearch) {
            errorAndExit("Missing callee phone number");
        } else if (startDate == null) {
            errorAndExit("Missing start date of the phone call");
        } else if (startTime == null) {
            errorAndExit("Missing start time of the phone call");
        } else if (startMarker == null) {
            errorAndExit("Missing am/pm marker for start time of phone call");
        } else if (endDate == null){
            errorAndExit("Missing end date of the phone call");
        } else if (endTime == null) {
            errorAndExit("Missing end time of the phone call");
        } else if (endMarker == null) {
            errorAndExit("Missing am/pm marker for end time of phone call");
        }



        /*
        if (hostName == null) {
            usage( MISSING_ARGS );

        } else if ( portString == null) {
            usage( "Missing port" );
        }

        int port;
        try {
            port = Integer.parseInt( portString );
            
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        PhoneBillRestClient client = new PhoneBillRestClient(hostName, port);


        String message;
        try {
            // Print all word/definition pairs
            String customerName = "Customer";
            message = client.getPrettyPhoneBill(customerName);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(message);
        */
        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * This function returns a formatted string of the usage of the command line argument utilizing
     * the String.join and System.getProperty. These were found at the following URL:
     *    https://stackoverflow.com/questions/878573/java-multiline-string
     *
     * @return The formatted String containing usage information about the command line arguments
     */
    private static String usage(){
        return String.join(
            System.getProperty("line.separator"),
            "usage: java edu.pdx.cs410J.manpreet.Project4 [options] <args>",
            "args are (in this order):",
            "  customer               Person whose phone bill we’re modeling",
            "  callerNumber           Phone number of caller",
            "  calleeNumber           Phone number of person who was called",
            "  startTime              Date and time (am/pm) call began",
            "  endTime                Date and time (am/pm) call ended",
            "options are (options may appear in any order):",
            "  -host hostname         Host computer on which the server runs",
            "  -port port             Port on which the server is listening",
            "  -search                Phone calls should be searched for",
            "  -print                 Prints a description of the new phone call",
            "  -README                Prints a README for this project and exits"
        );
    }

    /**
     * This takes an error message, prints it to the Standard error along with the usage of the
     * command line argument, and exits with an error code of 1.
     *
     * @param message The error message to print to Standard error
     */
    private static void errorAndExit(String message){
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println(usage());
        System.exit(1);
    }


    /**
     * This function was taken from David Whitlock's PhoneBill.java class which checks to see if
     * the array contains a string. In this context, this will check to see if the command line
     * arguments contains one of the optional commands (-README and -print). The function can be
     * found at the following URL:
     * https://github.com/DavidWhitlock/PortlandStateJavaSummer2018/blob/master/phonebill/src/main/java/edu/pdx/cs410J/whitlock/Project1.java
     *
     * @param args Array of strings to check
     * @param option String to find in the array
     * @return Boolean whether option is in args String array or not
     */
    private static boolean containsOption(String[] args, String option) {
        return Arrays.stream(args).anyMatch(s -> s.equals(option));
    }
}