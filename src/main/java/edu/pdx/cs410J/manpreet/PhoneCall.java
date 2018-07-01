package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
      /*The date format code was derived from the Java Documentation found at
          https://docs.oracle.com/javase/7/docs/api/java/text/DateFormat.html
      */
      DateFormat df = new SimpleDateFormat("mm/dd/yyyy hh:mm");
      try{
        df.parse(time);
      }
      catch (ParseException e){
        throw new IllegalArgumentException("Invalid time entered. The format is correct but the values are incorrect.");
      }
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
