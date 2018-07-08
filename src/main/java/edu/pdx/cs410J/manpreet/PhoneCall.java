package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class PhoneCall extends AbstractPhoneCall {
  //Caller's phone number
  private String callerNumber;

  //Callee's phone number
  private String calleeNumber;

  //Start time of the call
  private String startTime;

  //End time of the call
  private String endTime;

  /**
   * Creates a new <code>PhoneCall</code>
   * @param callerNumber Phone number of caller
   * @param calleeNumber Phone number of person who was called
   * @param startTime Date and time call began (24-hour time)
   * @param endTime Date and time call ended (24-hour time)
   */
  PhoneCall(String callerNumber, String calleeNumber, String startTime, String endTime){
    validateNumber(callerNumber);
    validateNumber(calleeNumber);
    validateDateTime(startTime);
    validateDateTime(endTime);

    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * Validates the phone number format. The regular expression was derived by reading the documentation found at:
   * https://docs.oracle.com/javase/10/docs/api/java/util/regex/Pattern.html
   *
   * @param number The phone number to validate
   */
  private void validateNumber(String number){
    if (! Pattern.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$", number)){
      throw new IllegalArgumentException("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9.");
    }
  }

  /**
   * Validates the datetime string. The code below was derived from reading through the documentation on
   * DateTimeFormatter found at this URL:
   * https://docs.oracle.com/javase/10/docs/api/java/time/format/DateTimeFormatter.html
   *
   * @param time The datetime string to validate
   */
  private void validateDateTime(String time){
    // DateTime format patterns that are acceptable
    String[] patterns = {"M/d/uuuu H:mm", "M/d/uuuu k:mm"};

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

  /**
   * Get the caller's number.
   *
   * @return The caller's number
   */
  @Override
  public String getCaller() {
    return this.callerNumber;
  }

  /**
   * Get the callee's number.
   *
   * @return The callee's number
   */
  @Override
  public String getCallee() {
    return this.calleeNumber;
  }

  /**
   * Get the start time of the phone call between the caller and callee.
   *
   * @return The start time, as a string, of the start time of the call
   */
  @Override
  public String getStartTimeString() {
    return this.startTime;
  }

  /**
   * Get the end time of the phone call between the caller and callee.
   *
   * @return The end time, as a string, of the end time of the call
   */
  @Override
  public String getEndTimeString() {
    return this.endTime;
  }
}
