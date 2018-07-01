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
}
