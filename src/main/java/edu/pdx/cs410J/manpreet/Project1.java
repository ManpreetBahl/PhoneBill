package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Arrays;

/**
 * The main class for the CS410J Phone Bill Project
 */

public class Project1 {
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

  public static void main(String[] args) {
    String customer = null;
    String callerNum = null;
    String calleeNum = null;
    String startTime = null;
    String endTime = null;
    boolean toPrint = false;
    int startIndex = 0;
    PhoneCall toAdd;
    PhoneBill bill;

    if(args.length == 0){
      errorAndExit("Missing command line arguments");
    }

    if (containsOption(args, "-README")) {
      System.out.println(README);
      System.exit(0);
    }

    if (containsOption(args, "-print")){
      ++startIndex;
      toPrint = true;
    }

    for(int i = startIndex; i < args.length; i++){
      if(args[i].startsWith("-")){
        errorAndExit("Invalid command: " + args[i]);
      }
      if (customer == null){
        customer = args[i];
      } else if (callerNum == null){
        callerNum = args[i];
      } else if (calleeNum == null){
        calleeNum = args[i];
      } else if (startTime == null){
        startTime = args[i] + " " + args[++i];
      } else if (endTime == null) {
        endTime = args[i] + " " + args[++i];
      } else{
        errorAndExit("Too many arguments entered");
      }
    }

    if (customer == null){
      errorAndExit("Missing customer name");
    } else if (callerNum == null){
      errorAndExit("Missing caller phone number");
    } else if (calleeNum == null){
      errorAndExit("Missing callee phone number");
    } else if (startTime == null){
      errorAndExit("Missing start time of the phone call");
    } else if (endTime == null) {
      errorAndExit("Missing end time of the phone call");
    }

    try{
      toAdd = new PhoneCall(callerNum, calleeNum, startTime,endTime);
      bill = new PhoneBill(customer);
      bill.addPhoneCall(toAdd);

      if(toPrint){
        System.out.println(toAdd.toString());
      }

      System.exit(0);
    }catch (IllegalArgumentException ie){
      errorAndExit(ie.getMessage());
    }
  }

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
  private static void errorAndExit(String message){
    System.err.println(message);
    System.err.println(usage());
    System.exit(1);
  }

  private static boolean containsOption(String[] args, String option) {
    return Arrays.stream(args).anyMatch(s -> s.equals(option));
  }
}