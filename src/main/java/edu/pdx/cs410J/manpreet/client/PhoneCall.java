package edu.pdx.cs410J.manpreet.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.lang.Override;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall> {
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

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public PhoneCall() {

  }

  /**
   * Creates a new <code>PhoneCall</code>. The startTime and endTime Date objects need to be
   * verified beforehand before calling this constructor.
   * @param callerNumber Phone number of caller
   * @param calleeNumber Phone number of person who was called
   * @param startTime Date time when call began (am/pm)
   * @param endTime Date time when call ended (am/pm)
   */
  PhoneCall(String callerNumber, String calleeNumber, Date startTime, Date endTime){
    //Check to make sure start time is before end time
    if (startTime.after(endTime)) {
      throw new IllegalArgumentException("Start time can't be after end time!");
    }

    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTime = startTime;
    this.endTime = endTime;

    DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
    this.startTimeString = dtf.format(startTime);
    this.endTimeString = dtf.format(endTime);
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
   * Get the start time date object of the phone call
   * @return The date object containing the start time information
   */
  @Override
  public Date getStartTime() {
    return this.startTime;
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
   * Get the callee's number.
   *
   * @return The callee's number
   */
  @Override
  public String getCallee() {
    return this.calleeNumber;
  }

  /**
   * Get the end time date object of the phone call
   * @return The date object containing the end time information
   */
  @Override
  public Date getEndTime() {
    return this.endTime;
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

