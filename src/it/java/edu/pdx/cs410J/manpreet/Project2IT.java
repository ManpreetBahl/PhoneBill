package edu.pdx.cs410J.manpreet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

/**
 * Tests the functionality in the {@link Project2} main class.
 */
public class Project2IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
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
              "Assignment: Project 2",
              "Author: Manpreet Bahl",
              "This project implements the fundamental design of the PhoneBill application.",
              "Specifically, this project implements the PhoneCall class which contains",
              "information about one call: caller phone number, callee phone number, start",
              "and end time of the phone call.",
              "The PhoneBill class contains all the PhoneCalls for a particular customer to",
              "billed. By designing and testing these two fundamental classes, we can be sure",
              "these will work as intended even if the way they are used is different from",
              "project to project.",
              "usage: java edu.pdx.cs410J.manpreet.Project2 [options] <args>",
              "args are (in this order):",
              "  customer               Person whose phone bill we’re modeling",
              "  callerNumber           Phone number of caller",
              "  calleeNumber           Phone number of person who was called",
              "  startTime              Date and time call began (24-hour time)",
              "  endTime                Date and time call ended (24-hour time)",
              "options are (options may appear in any order):",
              "  -textFile file         Where to read/write the phone bill",
              "  -print                 Prints a description of the new phone call",
              "  -README                Prints a README for this project and exits",
              "Date and time should be in the format: mm/dd/yyyy hh:mm"
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
  public void testMissingEndDate(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end date of the phone call"));
  }

  @Test
  public void testMissingEndTime(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "2/2/2302");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }

  @Test
  public void testUnknownCommand(){
    MainMethodResult result = invokeMain("-print", "customer", "-123-456-7890", "123-456-7899");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid command: -123-456-7890"));
  }

  @Test
  public void testPhoneCallValidationBadCallerNum(){
    MainMethodResult result = invokeMain("customer", "123-456-7H90", "123-456-7899", "1/2/2301", "3:00", "2/3/2402", "23:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9."));
  }

  @Test
  public void testPhoneCallValidationBadCalleeNum(){
    MainMethodResult result = invokeMain("customer", "123-456-7390", "123-456-899", "1/2/2301", "3:00", "2/3/2402", "23:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered. Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9."));
  }

  @Test
  public void testDisplay(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "2/3/2402", "23:00");
    assertThat(result.getExitCode(),equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: customer",
        "Phone call from 123-456-7890 to 123-456-7899 from 1/2/2301 3:00 to 2/3/2402 23:00"
        ))
    );
  }

  @Test
  public void testReadmeWithOtherCommands(){
    MainMethodResult result = invokeMain("-README", "-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "2/3/2402", "23:00");
    assertThat(result.getExitCode(), equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Assignment: Project 2",
        "Author: Manpreet Bahl",
        "This project implements the fundamental design of the PhoneBill application.",
        "Specifically, this project implements the PhoneCall class which contains",
        "information about one call: caller phone number, callee phone number, start",
        "and end time of the phone call.",
        "The PhoneBill class contains all the PhoneCalls for a particular customer to",
        "billed. By designing and testing these two fundamental classes, we can be sure",
        "these will work as intended even if the way they are used is different from",
        "project to project.",
        "usage: java edu.pdx.cs410J.manpreet.Project2 [options] <args>",
        "args are (in this order):",
        "  customer               Person whose phone bill we’re modeling",
        "  callerNumber           Phone number of caller",
        "  calleeNumber           Phone number of person who was called",
        "  startTime              Date and time call began (24-hour time)",
        "  endTime                Date and time call ended (24-hour time)",
        "options are (options may appear in any order):",
        "  -textFile file         Where to read/write the phone bill",
        "  -print                 Prints a description of the new phone call",
        "  -README                Prints a README for this project and exits",
        "Date and time should be in the format: mm/dd/yyyy hh:mm"
    )));
  }

  @Test
  public void testBadCommandArgumentMissingEndTime(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "1/2/2301", "3:00", "2/3/2402");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }

  @Test
  public void testBadCommandArgumentMissingStartDate(){
    MainMethodResult result = invokeMain("-print", "customer", "123-456-7890", "123-456-7899", "3:00", "2/3/2402", "4:00");
    assertThat(result.getExitCode(),equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing end time of the phone call"));
  }

  @Test
  public void testMultiWordCustomerName(){
    MainMethodResult result = invokeMain("-print", "Monkey King", "123-456-7890", "123-456-7899", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getExitCode(),equalTo(0));
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: Monkey King",
        "Phone call from 123-456-7890 to 123-456-7899 from 1/6/2008 23:00 to 2/3/2402 4:00"
        ))
    );
  }

  public void testNoTextFilePath(){
    MainMethodResult result = invokeMain("-textFile");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing file path"));
  }


  public void testFullCommandArgsWithTextFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyKing",
        "Phone call from 123-456-7891 to 153-234-2199 from 1/6/2008 23:00 to 2/3/2402 4:00"
        ))
    );
    assertThat(result.getExitCode(),equalTo(0));
  }


  public void testTextParser(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyKing",
        "Phone call from 123-456-7891 to 153-234-2199 from 1/6/2008 23:00 to 2/3/2402 4:00"
        ))
    );
    assertThat(result.getExitCode(),equalTo(0));
  }


  public void testFileExistsButEmpty(){
    MainMethodResult result = invokeMain("-print", "-textFile", "ExistsButEmpty.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Empty file detected!"));
    assertThat(result.getExitCode(),equalTo(1));
  }


  public void testMismatchCustomerName(){
    MainMethodResult result = invokeMain("-print", "-textFile", "MonkeyKing.txt", "MonkeyBoy", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("The file specified does not match your name"));
    assertThat(result.getExitCode(),equalTo(1));
  }


  public void testOnlyCustNameInFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "OnlyHasCustomerName.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString(String.join(
        System.getProperty("line.separator"),
        "Customer: MonkeyQueen",
        "Phone call from 123-456-7891 to 153-234-2199 from 1/6/2008 23:00 to 2/3/2402 4:00"
    )));
    assertThat(result.getExitCode(),equalTo(0));
  }

  public void testRelativePath(){
    MainMethodResult result = invokeMain("-print", "-textFile", "hello/there/test.txt", "MonkeyQueen", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Unable to create a file!"));
    assertThat(result.getExitCode(),equalTo(1));
  }

  public void testMalformedFile(){
    MainMethodResult result = invokeMain("-print", "-textFile", "Malformed.txt", "MonkeyKing", "123-456-7891", "153-234-2199", "1/6/2008", "23:00", "2/3/2402", "4:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Invalid phone number entered."));
    assertThat(result.getExitCode(),equalTo(1));
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
  }
}