package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Arrays;

/**
 * The main class for the CS410J Phone Bill Project
 */

public class Project1 {
  /**
   * The README text for the -README option parameter. The code for the String.join and
   * System.getProperty was found here:
   *       https://stackoverflow.com/questions/878573/java-multiline-string
   */
  static final String README = String.join(
    System.getProperty("line.separator"),
      "Assignment: Project 1",
      "Author: Manpreet Bahl",
      "This project implements the fundamental design of the PhoneBill application.",
      "Specifically, this project implements the PhoneCall class which contains",
      "information about one call: caller phone number, callee phone number, start",
      "and end time of the phone call.",
      "The PhoneBill class contains all the PhoneCalls for a particular customer to",
      "billed. By designing and testing these two fundamental classes, we can be sure",
      "these will work as intended even if the way they are used is different from",
      "project to project.",
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
     This also increments the startIndex for command line parsing to 1
     */
    if (containsOption(args, "-print")){
      ++startIndex;
      toPrint = true;
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
    }

    try{
      //Attempt to create a new PhoneCall. Validation of the parameters will be done here
      toAdd = new PhoneCall(callerNum, calleeNum, startDate + " " + startTime, endDate + " " + endTime);
      //Create a PhoneBill for the customer
      bill = new PhoneBill(customer);
      //Add the newly created PhoneCall object
      bill.addPhoneCall(toAdd);

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
    }catch (IllegalArgumentException ie){
      /*The PhoneCall validator functions throw IllegalArgumentException if they catch an error
        with the format and values of the phone numbers and dates. In that case, this will catch
        that exception, print the error message and exit with a status code of 1
       */
      errorAndExit(ie.getMessage());
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
        "usage: java edu.pdx.cs410J.manpreet.Project1 [options] <args>",
        "args are (in this order):",
        "  customer               Person whose phone bill we’re modeling",
        "  callerNumber           Phone number of caller",
        "  calleeNumber           Phone number of person who was called",
        "  startTime              Date and time call began (24-hour time)",
        "  endTime                Date and time call ended (24-hour time)",
        "options are (options may appear in any order):",
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