package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

public class PhoneCall extends AbstractPhoneCall {
  /**
  Fields
   */
  private String customer = null;
  private String callerNumber = null;
  private String calleeNumber = null;
  private String startTime = null;
  private String endTime = null;

  /**
   * Constructor
   * @param customer
   *        Person whose phone bill we’re modeling
   * @param callerNumber
   *        Phone number of caller
   * @param calleeNumber
   *        Phone number of person who was called
   * @param startTime
   *        Date and time call began (24-hour time)
   * @param endTime
   *        Date and time call ended (24-hour time)
   */
  public PhoneCall(String customer, String callerNumber, String calleeNumber, String startTime, String endTime){
    validateNumber(callerNumber);
    validateNumber(calleeNumber);
    validateTime(startTime);
    validateTime(endTime);

    this.customer = customer;
    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * Validates the phone number format. The regular expression was derived by reading the documentation found at:
   *     https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
   * @param number
   *        The phone number to validate
   */
  private void validateNumber(String number){
    if (! Pattern.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$", number)){
      throw new IllegalArgumentException("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9.");
    }
  }

  private void validateTime(String time){
    if (! Pattern.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4} [0-9]{1,2}:[0-9]{2}$", time)){
      throw new IllegalArgumentException("Invalid time entered. Time must be in the format mm/dd/yyyy hh:mm");
    }
    else{
      /*
        The code below was derived from reading through the documentation on DateTimeFormatter found at this URL:
          https://docs.oracle.com/javase/10/docs/api/java/time/format/DateTimeFormatter.html
      */
      // DateTime format patterns that are acceptable
      String[] patterns = {"M/d/uuuu h:mm", "M/d/uuuu H:mm", "M/d/uuuu k:mm", "M/d/uuuu K:mm"};

      //For each pattern, attempt to parse the date time string
      for (String pattern : patterns){
        try{
          LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
          return; //Parse is successful, so the time string is valid
        }
        catch (DateTimeParseException e){ } //Try the next pattern
      }
      //No patterns match so through an IllegalArgumentException
      throw new IllegalArgumentException("Invalid time entered. Time must be in the format mm/dd/yyyy hh:mm");
    }
  }

  @Override
  public String getCaller() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getCallee() {
    return "This method is not implemented yet";
  }

  @Override
  public String getStartTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
}
