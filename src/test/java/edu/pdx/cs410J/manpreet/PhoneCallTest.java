package edu.pdx.cs410J.manpreet;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link PhoneCall} class.
 */
public class PhoneCallTest {
  PhoneCall call;
  /*
  @Test(expected = UnsupportedOperationException.class)
  public void getStartTimeStringNeedsToBeImplemented() {
    PhoneCall call = new PhoneCall();
    call.getStartTimeString();
  }

  @Test
  public void initiallyAllPhoneCallsHaveTheSameCallee() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getCallee(), containsString("not implemented"));
  }

  @Test
  public void forProject1ItIsOkayIfGetStartTimeReturnsNull() {
    PhoneCall call = new PhoneCall();
    assertThat(call.getStartTime(), is(nullValue()));
  }
  */

  @Test
  public void canCreatePhoneCallObject(){
    new PhoneCall("123-456-7890", "098-765-4321", "6/30/2018 1:00", "6/30/2018 2:00");
  }

  //Caller Phone Number Tests
  @Test(expected = IllegalArgumentException.class)
  public void callerPhoneNumberEmptyString(){
    new PhoneCall("", "123-456-7890", "6/30/2018 1:00", "6/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void callerPhoneNumberNonNumbers(){
    new PhoneCall("022-Ab2$-34_2", "123-456-7890", "6/30/2018 1:00", "6/30/2018 2:00");
  }

  //Callee Phone Number Tests
  @Test(expected = IllegalArgumentException.class)
  public void calleePhoneNumberEmptyString(){
    new PhoneCall("123-456-7890", "", "6/30/2018 1:00", "6/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void calleePhoneNumberNonNumbers(){
    new PhoneCall("123-456-7890", "13%-NII-32*+", "6/30/2018 1:00", "6/30/2018 2:00");
  }

  //Start Time Tests
  @Test(expected = IllegalArgumentException.class)
  public void emptyStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "", "6/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void badStartTimeFormat(){
    new PhoneCall("123-456-7890", "132-456-7890", "6-30-2018 2:00", "6/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void noTimeInStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "6/30/2018", "6/30/2018 2:00");
  }

  @Test
  public void oneDigitMonthStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "6/30/2018 12:00", "6/30/2018 12:00");
  }

  @Test
  public void oneDigitDayStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "12/5/2018 12:00", "6/30/2018 2:00");
  }

  @Test
  public void oneDigitMonthDayStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 12:00", "6/30/2018 2:00");
  }

  @Test
  public void oneDigitMonthDayHourStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 1:00", "6/30/2018 2:00");
  }

  @Test
  public void militaryStartTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 15:00", "6/30/2018 2:00");
  }

  @Test
  public void startTimePast(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/0000 15:00", "6/30/2018 2:00");
  }

  @Test
  public void startTimeFuture(){
    new PhoneCall("123-456-7890", "132-456-7890", "5/26/9999 15:00", "6/30/2018 2:00");
  }

  @Test (expected = IllegalArgumentException.class)
  public void startTimeInvalidMonth(){
    new PhoneCall("123-456-7890", "132-456-7890", "15/5/2018 15:00", "6/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startTimeYearMoreThanFourDigits(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/201867 15:00", "65/30/2018 2:00");
  }

  @Test
  public void startTimeSingleZero(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 0:00", "5/30/2018 2:00");
  }

  @Test
  public void startTimeDoubleZeroes(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 00:00", "5/30/2018 2:00");
  }

  @Test
  public void startTimeTwentyFourHour(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 24:00", "5/30/2018 2:00");
  }

  //End Time Tests
  @Test(expected = IllegalArgumentException.class)
  public void emptyEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "06/30/2018 2:00", "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void badEndTimeFormat(){
    new PhoneCall("123-456-7890", "132-456-7890", "06/30/2018 2:00", "06-30-2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void noTimeInEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "06/30/2018 2:00", "06/30/2018");
  }

  @Test
  public void oneDigitMonthEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "06/30/2018 12:00", "6/30/2018 12:00");
  }

  @Test
  public void oneDigitDayEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "12/5/2018 12:00", "6/5/2018 2:00");
  }

  @Test
  public void oneDigitMonthDayEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 12:00", "6/3/2018 2:00");
  }

  @Test
  public void oneDigitMonthDayHourEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 1:00", "6/3/2018 2:00");
  }

  @Test
  public void militaryEndTime(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 1:00", "6/30/2018 24:00");
  }

  @Test
  public void endTimePast(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/0000 15:00", "6/30/0000 2:00");
  }

  @Test
  public void startTimeEnd(){
    new PhoneCall("123-456-7890", "132-456-7890", "5/26/2018 15:00", "6/30/9999 2:00");
  }

  @Test (expected = IllegalArgumentException.class)
  public void endTimeInvalidMonth(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 15:00", "65/30/2018 2:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void endTimeYearMoreThanFourDigits(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 15:00", "65/30/201802 2:00");
  }

  @Test
  public void endTimeSingleZero(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 2:00", "5/30/2018 0:00");
  }

  @Test
  public void endTimeDoubleZeroes(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 04:00", "5/30/2018 00:00");
  }

  @Test
  public void endTimeTwentyFourHour(){
    new PhoneCall("123-456-7890", "132-456-7890", "1/5/2018 23:00", "5/30/2018 24:00");
  }
}
