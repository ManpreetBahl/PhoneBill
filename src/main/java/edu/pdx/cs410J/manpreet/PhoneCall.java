package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneCall;

import edu.pdx.cs410J.ParserException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * This class contains information about a phone call. This includes the caller's and callee's
 * phone number as well as the start and end time of the call. Validation of the phone numbers and
 * date are performed in the constructor to ensure that they meet the required standard.
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>{
  //Caller's phone number
  private String callerNumber;

  //Callee's phone number
  private String calleeNumber;

  //Start time of the call
  private String startTimeString;

  //End time of the call
  private String endTimeString;

  //Start time of the call- Date Object
  private Date startTime;

  //End time of the call- Date Object
  private Date endTime;

  //Date formatter
  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

  /**
   * Creates a new <code>PhoneCall</code>. The startTime and endTime Date objects need to be
   * verified beforehand before calling this constructor.
   * @param callerNumber Phone number of caller
   * @param calleeNumber Phone number of person who was called
   * @param startTime Date time when call began (am/pm)
   * @param endTime Date time when call ended (am/pm)
   */
  PhoneCall(String callerNumber, String calleeNumber, Date startTime, Date endTime){
    validateNumber(callerNumber);
    validateNumber(calleeNumber);

    //Check to make sure start time is before end time
    if (startTime.after(endTime)) {
      throw new IllegalArgumentException("Start time can't be after end time!");
    }

    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTime = startTime;
    this.endTime = endTime;
    this.startTimeString = sdf.format(startTime);
    this.endTimeString = sdf.format(endTime);
  }

  /**
   * Creates a new <code>PhoneCall</code>
   * @param callerNumber Phone number of caller
   * @param calleeNumber Phone number of person who was called
   * @param startTimeString Date and time call began (24-hour time)
   * @param endTimeString Date and time call ended (24-hour time)
   */
  PhoneCall(String callerNumber, String calleeNumber, String startTimeString, String endTimeString){
    validateNumber(callerNumber);
    validateNumber(calleeNumber);
    validateDateTime(startTimeString);
    validateDateTime(endTimeString);

    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTimeString = startTimeString;
    this.endTimeString = endTimeString;
    try{
      this.startTime = sdf.parse(this.startTimeString);
      this.endTime = sdf.parse(this.endTimeString);
    }catch (ParseException pe){
      throw new IllegalArgumentException("Can't parse the time string due to bad format");
    }
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
    String[] patterns = {"MM/dd/uuuu HH:mm", "MM/dd/uuuu kk:mm", "MM/dd/uuuu hh:mm a"};

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
    return this.startTimeString;
  }

  /**
   * Get the end time of the phone call between the caller and callee.
   *
   * @return The end time, as a string, of the end time of the call
   */
  @Override
  public String getEndTimeString() {
    return this.endTimeString;
  }

  /**
   * Get the start time date object of the phone call
   * @return The date object containing the start time information
   */
  @Override
  public Date getStartTime(){
    return this.startTime;
  }

  /**
   * Get the end time date object of the phone call
   * @return The date object containing the end time information
   */
  @Override
  public Date getEndTime(){
    return this.endTime;
  }

  /**
   * This function compares the current PhoneCall object with another
   * and is used to maintain a sorted order in the PhoneBill based on
   * start time of the call. In case of a tie, the caller's phone numbers
   * are used.
   * @param other The other PhoneCall object to compare to.
   * @return Integer indicating whether the current PhoneCall comes before
   *         or after the passed in PhoneCall.
   */
  @Override
  public int compareTo(PhoneCall other){
    if (this.startTime.before(other.getStartTime())) {
      return -1;
    } else if (this.startTime.after(other.getStartTime())) {
      return 1;
    } else{
      int callerPhone1 = Integer.parseInt(this.callerNumber.replace("-", ""));
      int callerPhone2 = Integer.parseInt(other.getCaller().replace("-", ""));

      return Integer.compare(callerPhone1,callerPhone2);
    }

  }

  /**
   * This function determines the duration of the call and converts it into minutes.
   * @return Long indicating the duration of the call in minutes.
   */
  public long duration(){
    return TimeUnit.MILLISECONDS.toMinutes(this.endTime.getTime() - this.startTime.getTime());
  }
}
