package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.ParserException;
import java.io.IOException;
import java.util.Arrays;

public class Project2 {
  /**
   * The README text for the -README option parameter. The code for the String.join and
   * System.getProperty was found here:
   *       https://stackoverflow.com/questions/878573/java-multiline-string
   */
  static private final String README = String.join(
      System.getProperty("line.separator"),
      "Assignment: Project 2",
      "Author: Manpreet Bahl",
      "This project builds upon Project 1 by storing PhoneBill data to text files.",
      "There are two new classes added: TextParser and TextDumper. TextParser parses a",
      "text file and creates a PhoneBill object from it. TextDumper dumps the contents of",
      "a PhoneBill object to a text file specified by the filepath.",
      "The Project2 class builds upon Project1 class by adding the -textFile option command",
      "to the command line parsing",
      usage()
  );

  /**
   * The main method for the application
   *
   * @param args The command line arguments for the application
   */
  public static void main(String[] args) {
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

    //The date when the call ended
    String endDate = null;

    //The time when the call ended
    String endTime = null;

    //Should the phone call to add be displayed?
    boolean toPrint = false;

    //Where to start in the args array
    int startIndex = 0;

    //The PhoneCall object to add
    PhoneCall toAdd;

    //The PhoneBill object
    PhoneBill bill;

    //File Path
    String filepath = null;

    //Should the phone call be added to a text file?
    boolean toText = false;

    //Make sure there are command line arguments
    if(args.length == 0){
      errorAndExit("Missing command line arguments");
    }

    /*If the command line arguments contains -README, display the README
      and exit with status of 0
     */
    if (containsOption(args, "-README")) {
      System.out.println(README);
      System.exit(0);
    }

    /*If the command line argument contains -print, set the flag to print to true
     This also increments the startIndex for command line parsing by 1.
     */
    if (containsOption(args, "-print")){
      ++startIndex;
      toPrint = true;
    }

    /*If the command line argument contains -textFile, get the filepath from the next args
    parameter. This also increments the startIndex for command line parsing by 2.
     */
    if (containsOption(args, "-textFile")){
      try{
        startIndex += 2;
        filepath = args[Arrays.asList(args).indexOf("-textFile") + 1];
        toText = true;
      }catch (ArrayIndexOutOfBoundsException ae){
        errorAndExit("Missing file path");
      }
    }

    //Get the remaining commands from the args array
    for(int i = startIndex; i < args.length; i++){
      //If the command line argument starts with "-", then it's an invalid command
      if(args[i].startsWith("-")){
        errorAndExit("Invalid command: " + args[i]);
      }
      if (customer == null){
        customer = args[i];
      } else if (callerNum == null){
        callerNum = args[i];
      } else if (calleeNum == null){
        calleeNum = args[i];
      } else if (startDate == null) {
        startDate = args[i];
      } else if (startTime == null) {
        startTime = args[i];
      } else if (endDate == null){
        endDate = args[i];
      } else if (endTime == null) {
        endTime = args[i];
      } else{
        errorAndExit("Too many arguments entered");
      }
    }

    /*Make sure that all the command line arguments are present.
      This does not perform any validation of the arguments, just ensures that they have
      been entered.
     */
    if (customer == null){
      errorAndExit("Missing customer name");
    } else if (callerNum == null){
      errorAndExit("Missing caller phone number");
    } else if (calleeNum == null) {
      errorAndExit("Missing callee phone number");
    } else if (startDate == null) {
      errorAndExit("Missing start date of the phone call");
    } else if (startTime == null) {
      errorAndExit("Missing start time of the phone call");
    } else if (endDate == null){
      errorAndExit("Missing end date of the phone call");
    } else if (endTime == null) {
      errorAndExit("Missing end time of the phone call");
    } else if (filepath == null && toText) {
      errorAndExit("Missing file path");
    }

    try{
      //Attempt to create a new PhoneCall. Validation of the parameters will be done here
      toAdd = new PhoneCall(callerNum, calleeNum, startDate + " " + startTime, endDate + " " + endTime);

      if(toText){
        //Attempt to parse the file specified to get customer data
        try{
          TextParser tp = new TextParser(filepath);
          bill = tp.parse();
        }catch(IOException ie){ //There's no file to parse, so create a new PhoneBill
          bill = new PhoneBill(customer);
        }

        //Check to make sure the customer name matches the text file specified
        if(!bill.getCustomer().equals(customer)){
         throw new IllegalArgumentException("The name in the specified file does not match your name!");
        }

        //Add the newly created PhoneCall object
        bill.addPhoneCall(toAdd);

        //Dump the PhoneBill contents to the file
        TextDumper td = new TextDumper(filepath);
        td.dump(bill);
      }
      else{
        //Create a PhoneBill for the customer
        bill = new PhoneBill(customer);
        //Add the newly created PhoneCall object
        bill.addPhoneCall(toAdd);
      }

      //Print the call information if the user specified to do so
      if(toPrint){
        System.out.println(String.join(
            System.getProperty("line.separator"),
            "Customer: " + bill.getCustomer(),
            toAdd.toString()
            )
        );
      }

      //Exit gracefully with status code of 0
      System.exit(0);
    }catch (IllegalArgumentException | IOException | ParserException | ArrayIndexOutOfBoundsException e){
      errorAndExit(e.getMessage());
    }
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
        "usage: java edu.pdx.cs410J.manpreet.Project2 [options] <args>",
        "args are (in this order):",
        "  customer               Person whose phone bill we’re modeling",
        "  callerNumber           Phone number of caller",
        "  calleeNumber           Phone number of person who was called",
        "  startTime              Date and time call began (24-hour time)",
        "  endTime                Date and time call ended (24-hour time)",
        "options are (options may appear in any order):",
        "  -textFile file         Where to read/write the phone bill",
        "  -print                 Prints a description of the new phone call",
        "  -README                Prints a README for this project and exits",
        "Date and time should be in the format: mm/dd/yyyy hh:mm"
    );
  }

  /**
   * This takes an error message, prints it to the Standard error along with the usage of the
   * command line argument, and exits with an error code of 1.
   *
   * @param message The error message to print to Standard error
   */
  private static void errorAndExit(String message){
    System.err.println(message);
    System.err.println(usage());
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
