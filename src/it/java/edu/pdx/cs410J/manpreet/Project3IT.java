package edu.pdx.cs410J.manpreet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  public void testREADME(){
      MainMethodResult result = invokeMain("-README");
      assertThat(result.getExitCode(), equalTo(0));
      assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
          System.getProperty("line.separator"),
          "Assignment: Project 3",
          "Author: Manpreet Bahl",
          "This project builds upon Project 2 with several changes",
          "along with newly added features. First, the PhoneCall class",
          "now handles the start and end times of the calls as Date",
          "objects instead of strings. The PhoneBill class now sorts",
          "PhoneCalls by the start time of the call using the caller",
          "phone number as tiebreakers.",
          "A new feature called PrettyPrint was added that creates a nicely",
          "formatted textual presentation of the phone calls in a phone bill.",
          "usage: java edu.pdx.cs410J.manpreet.Project3 [options] <args>",
          "args are (in this order):",
          "  customer               Person whose phone bill we’re modeling",
          "  callerNumber           Phone number of caller",
          "  calleeNumber           Phone number of person who was called",
          "  startTime              Date and time (am/pm) call began",
          "  endTime                Date and time (am/pm) call ended",
          "options are (options may appear in any order):",
          "  -pretty file           Pretty print the phone bill to a text file",
          "                         or standard out (file -).",
          "  -textFile file         Where to read/write the phone bill",
          "  -print                 Prints a description of the new phone call",
          "  -README                Prints a README for this project and exits"
      )));
  }

  @Test
  public void testMissingCustomerName(){
      MainMethodResult result = invokeMain("-print");
      assertThat(result.getExitCode(),equalTo(1));
      assertThat(result.getTextWrittenToStandardError(), containsString("Missing customer name"));
  }

  @Test
  public void testMissingCallerNum(){
      MainMethodResult result = invokeMain("-print", "customer");
      assertThat(result.getExitCode(),equalTo(1));
      assertThat(result.getTextWrittenToStandardError(), containsString("Missing caller phone number"));
  }

  @Test
  public void testMissingCalleeNum(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing callee phone number"));
  }

  @Test
  public void testMissingStartDate(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing start date of the phone call"));
  }

  @Test
  public void testMissingStartTime(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing start time of the phone call"));
  }

  @Test
  public void testMissingStartMarker(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing am/pm marker for start time of phone call"));
  }

  @Test
  public void testMissingEndDate(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "am");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end date of the phone call"));
  }

  @Test
  public void testMissingEndTime(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00","am", "2/2/2302");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }

  @Test
  public void testMissingEndMarker(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00","am", "2/2/2302", "4:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing am/pm marker for end time of phone call"));
  }

  @Test
  public void testUnknownCommand(){
    MainMethodResult result = invokeMain("-print", "customer", "-123-456-7890", "123-456-7899");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid command: -123-456-7890"));
  }

  @Test
  public void testPhoneCallValidationBadCallerNum(){
    MainMethodResult result = invokeMain("customer", "123-456-7H90", "123-456-7899", "1/2/2301", "3:00", "am", "2/3/2402", "1:00", "pm");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9."));
  }

  @Test
  public void testPhoneCallValidationBadCalleeNum(){
    MainMethodResult result = invokeMain("customer", "123-456-7390", "123-456-899", "1/2/2301", "3:00", "am", "2/3/2402", "1:00", "pm");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9."));
  }

  @Test
  public void testDisplay(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "03:00", "am", "2/3/2402", "1:00", "pm");
    assertThat(result.getExitCode(),equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: customer",
        "Phone call from 123-456-7890 to 123-456-7899 from 01/02/2301 03:00 AM to 02/03/2402 01:00 PM"
        ))
    );
  }

  @Test
  public void testReadmeWithOtherCommands(){
    MainMethodResult result = invokeMain("-README", "-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "2/3/2402", "23:00");
    assertThat(result.getExitCode(), equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Assignment: Project 3",
        "Author: Manpreet Bahl",
        "This project builds upon Project 2 with several changes",
        "along with newly added features. First, the PhoneCall class",
        "now handles the start and end times of the calls as Date",
        "objects instead of strings. The PhoneBill class now sorts",
        "PhoneCalls by the start time of the call using the caller",
        "phone number as tiebreakers.",
        "A new feature called PrettyPrint was added that creates a nicely",
        "formatted textual presentation of the phone calls in a phone bill.",
        "usage: java edu.pdx.cs410J.manpreet.Project3 [options] <args>",
        "args are (in this order):",
        "  customer               Person whose phone bill we’re modeling",
        "  callerNumber           Phone number of caller",
        "  calleeNumber           Phone number of person who was called",
        "  startTime              Date and time (am/pm) call began",
        "  endTime                Date and time (am/pm) call ended",
        "options are (options may appear in any order):",
        "  -pretty file           Pretty print the phone bill to a text file",
        "                         or standard out (file -).",
        "  -textFile file         Where to read/write the phone bill",
        "  -print                 Prints a description of the new phone call",
        "  -README                Prints a README for this project and exits"
    )));
  }


  @Test
  public void testBadCommandArgumentMissingEndTime(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "pm", "2/3/2402");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }


  @Test
  public void testBadCommandArgumentMissingStartDate(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "3:00", "am", "2/3/2402", "4:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }


  @Test
  public void testMultiWordCustomerName(){
    MainMethodResult result = invokeMain("-print", "Monkey King", "123-456-7890", "123-456-7899", "1/06/2008", "3:00", "pm", "02/3/2402", "04:00", "am");
    assertThat(result.getExitCode(),equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: Monkey King",
        "Phone call from 123-456-7890 to 123-456-7899 from 01/06/2008 03:00 PM to 02/03/2402 04:00 AM"
        ))
    );
  }


  public void testNoTextFilePath(){
    MainMethodResult result = invokeMain("-textFile");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing file path"));
  }


  public void testFullCommandArgsWithTextFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "01/06/2008", "03:00", "am", "2/3/2402", "4:00", "pm");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyKing",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 03:00 AM to 02/03/2402 04:00 PM"
        ))
    );
    assertThat(result.getExitCode(),equalTo(0));
  }


  public void testTextParser(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "2:00", "pm", "2/3/2402", "4:00", "pm");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyKing",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 02:00 PM to 02/03/2402 04:00 PM"
        ))
    );
    assertThat(result.getExitCode(),equalTo(0));
  }


  public void testFileExistsButEmpty(){
    MainMethodResult result = invokeMain("-print", "-textFile", "ExistsButEmpty.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "6:00", "am", "2/3/2402", "5:00", "pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("Empty file detected!"));
    assertThat(result.getExitCode(),equalTo(1));
  }


  public void testMismatchCustomerName(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyBoy", "123-456-7891", "153-234-2199", "1/6/2008", "3:00", "am", "2/3/2402", "4:00", "pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("The name in the specified file does not match your name!"));
    assertThat(result.getExitCode(),equalTo(1));
  }


  public void testOnlyCustNameInFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "OnlyHasCustomerName.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "12:00", "PM", "2/3/2402", "4:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 12:00 PM to 02/03/2402 04:00 AM"
    )));
    assertThat(result.getExitCode(),equalTo(0));
  }

  public void testRelativePath(){
    MainMethodResult result = invokeMain("-print", "-textFile", "hello/there/test.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "10:00", "PM", "2/3/2402", "4:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 10:00 PM to 02/03/2402 04:00 AM")));
    assertThat(result.getExitCode(),equalTo(0));
  }

  public void testMalformedFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "Malformed.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "2:00", "pm", "2/3/2402", "4:00", "pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered."));
    assertThat(result.getExitCode(),equalTo(1));
  }

  public void testRelativePath1(){
    MainMethodResult result = invokeMain("-textFile", "manpreet/test.txt", "-print", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "9:00", "aM", "2/3/2402", "11:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 09:00 AM to 02/03/2402 11:00 AM")));
    assertThat(result.getExitCode(),equalTo(0));
  }
  @Test
  public void testFileProject2(){
    testNoTextFilePath();
    testFullCommandArgsWithTextFile();
    testTextParser();
    testFileExistsButEmpty();
    testMismatchCustomerName();
    testOnlyCustNameInFile();
    testRelativePath();
    testMalformedFile();
    testRelativePath1();
  }

  @Test
  public void testPrettyPrintBasic(){
    MainMethodResult result = invokeMain("-pretty", "PrettyTest.txt", "-print", "MonkeyQueen", "123-456-7891", "153-234-2199", "01/06/2008", "09:00", "aM", "02/03/2402", "11:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 09:00 AM to 02/03/2402 11:00 AM")));
    assertThat(result.getExitCode(),equalTo(0));
  }


  @Test
  public void testPrettyPrintStandardOut(){
    MainMethodResult result = invokeMain("-pretty", "-", "-print", "MonkeyQueen", "123-456-7891", "153-234-2199", "01/06/2008", "09:00", "aM", "1/6/2008", "11:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 09:00 AM to 01/06/2008 11:00 AM",
        "PHONE BILL: MonkeyQueen",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|             123-456-7891|             153-234-2199|      01/06/2008 09:00 AM|      01/06/2008 11:00 AM|                      120|",
        "-----------------------------------------------------------------------------------------------------------------------------------"
    )));
    assertThat(result.getExitCode(),equalTo(0));
  }

  @Test
  public void testPrettyPrintTextFile(){
    MainMethodResult result = invokeMain("-pretty", "-", "-print", "-textFile", "combined/test.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "9:00", "aM", "1/6/2008", "11:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/06/2008 09:00 AM to 01/06/2008 11:00 AM",
        "PHONE BILL: MonkeyQueen",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|             123-456-7891|             153-234-2199|      01/06/2008 09:00 AM|      01/06/2008 11:00 AM|                      120|",
        "-----------------------------------------------------------------------------------------------------------------------------------"
    )));
    assertThat(result.getExitCode(),equalTo(0));
  }

  @Test
  public void testPrettyPrintTextFile2(){
    MainMethodResult result = invokeMain("-pretty", "-", "-print", "-textFile", "combined/test.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/5/2008", "9:00", "Pm", "1/5/2008", "11:00", "pm");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 01/05/2008 09:00 PM to 01/05/2008 11:00 PM",
        "PHONE BILL: MonkeyQueen",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|             123-456-7891|             153-234-2199|      01/05/2008 09:00 PM|      01/05/2008 11:00 PM|                      120|",
        "-----------------------------------------------------------------------------------------------------------------------------------",
        "|             123-456-7891|             153-234-2199|      01/06/2008 09:00 AM|      01/06/2008 11:00 AM|                      120|",
        "-----------------------------------------------------------------------------------------------------------------------------------"
    )));
    assertThat(result.getExitCode(),equalTo(0));
  }

  @Test
  public void testPrettyPrintTextFileSameFilePath(){
    MainMethodResult result = invokeMain("-pretty", "combined/test.txt", "-print", "-textFile", "combined/test.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/5/2008", "9:00", "Pm", "1/5/2008", "11:00", "pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("The file path for textfile and pretty print cannot be the same!"));
    assertThat(result.getExitCode(),equalTo(1));
  }
}