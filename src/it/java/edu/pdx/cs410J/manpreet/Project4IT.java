package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.InvokeMainTestCase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    public void test0RemoveAllMappings(){
        try{
            PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
            client.removeAllPhoneBills();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_ARGS));
    }

    @Test
    public void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(String.join(
            System.getProperty("line.separator"),
            "** Missing customer name",
            "usage: java edu.pdx.cs410J.manpreet.Project4 [options] <args>",
            "args are (in this order):",
            "  customer               Person whose phone bill we’re modeling",
            "  callerNumber           Phone number of caller",
            "  calleeNumber           Phone number of person who was called",
            "  startTime              Date and time (am/pm) call began",
            "  endTime                Date and time (am/pm) call ended",
            "options are (options may appear in any order):",
            "  -host hostname         Host computer on which the server runs",
            "  -port port             Port on which the server is listening",
            "  -search                Phone calls should be searched for",
            "  -print                 Prints a description of the new phone call",
            "  -README                Prints a README for this project and exits"
        )));
    }

    @Test
    public void test3NoPhoneBill() {
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("There are no phonebills for " + customer));
    }

    @Test
    public void test4AddPhoneCall() {
        String customer= "New Customer";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer, "123-456-7890", "234-456-7890","1/2/2018", "3:00", "PM", "1/3/2018", "4:00", "AM");
        assertThat(result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("New phone call added to bill for customer: New Customer"));


        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("PHONE BILL: New Customer\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|             123-456-7890|             234-456-7890|      01/02/2018 03:00 PM|      01/03/2018 04:00 AM|                      780|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------"));
    }

    @Test
    public void test5AddMultiplePhoneCalls(){
        String customer= "Customer 1";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer, "123-456-7890", "234-456-7890","1/2/2018", "3:00", "PM", "1/3/2018", "4:00", "AM");
        assertThat(result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("New phone call added to bill for customer: Customer 1"));

        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("PHONE BILL: Customer 1\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|             123-456-7890|             234-456-7890|      01/02/2018 03:00 PM|      01/03/2018 04:00 AM|                      780|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------"));


        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer, "123-456-7890", "234-456-7890","1/4/2018", "3:00", "PM", "1/4/2018", "4:00", "PM");
        assertThat(result.getExitCode(), equalTo(0));
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("New phone call added to bill for customer: Customer 1"));


        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("PHONE BILL: Customer 1\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|             123-456-7890|             234-456-7890|      01/02/2018 03:00 PM|      01/03/2018 04:00 AM|                      780|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|             123-456-7890|             234-456-7890|      01/04/2018 03:00 PM|      01/04/2018 04:00 PM|                       60|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------"));
    }

    @Test
    public void test6SearchPhoneCall(){
        String customer = "Customer 1";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", customer, "1/1/2018", "3:00", "PM", "1/3/2018", "4:00", "PM");
        assertThat(result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("PHONE BILL: Customer 1\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|                   Caller|                   Callee|               Start Time|                 End Time|        Duration(minutes)|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------\n"
            + "|             123-456-7890|             234-456-7890|      01/02/2018 03:00 PM|      01/03/2018 04:00 AM|                      780|\n"
            + "-----------------------------------------------------------------------------------------------------------------------------------"));
    }

    @Test
    public void test7SearchAndPrintPhoneCall(){
        String customer = "Customer 1";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "-print", customer, "1/1/2018", "3:00", "PM", "1/3/2018", "4:00", "PM");
        assertThat(result.getExitCode(), equalTo(1));
        String out = result.getTextWrittenToStandardError();
        assertThat(out, out, containsString("Cannot print and search at the same time!"));
    }

    @Test
    public void test8AddAndPrintPhoneCall(){
        String customer= "Customer 2";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-print", "-port", PORT, customer, "123-456-7890", "234-456-7890","1/2/2018", "3:00", "PM", "1/3/2018", "4:00", "AM");
        assertThat(result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(String.join(
            System.getProperty("line.separator"),
            "Customer: " + customer,
            "Phone call from 123-456-7890 to 234-456-7890 from 01/02/2018 03:00 PM to 01/03/2018 04:00 AM",
            "New phone call added to bill for customer: Customer 2"
        )));
    }

    @Test
    public void test90MissingHostCommandArg(){
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-port", PORT, customer);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Need to specify host with the port!"));
    }

    @Test
    public void test91MissingPortCommandArg(){
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, customer);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Need to specify port with the host!"));
    }

    @Test
    public void test92StartTimeAfterEndTime(){
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-search", "-port", PORT, customer, "1/2/2018", "3:00", "PM", "1/1/2018", "4:00", "AM");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(String.join(
            System.getProperty("line.separator"),
            "Start time cannot be after end time!",
            "usage: java edu.pdx.cs410J.manpreet.Project4 [options] <args>",
            "args are (in this order):",
            "  customer               Person whose phone bill we’re modeling",
            "  callerNumber           Phone number of caller",
            "  calleeNumber           Phone number of person who was called",
            "  startTime              Date and time (am/pm) call began",
            "  endTime                Date and time (am/pm) call ended",
            "options are (options may appear in any order):",
            "  -host hostname         Host computer on which the server runs",
            "  -port port             Port on which the server is listening",
            "  -search                Phone calls should be searched for",
            "  -print                 Prints a description of the new phone call",
            "  -README                Prints a README for this project and exits"
        )));
    }

    @Test
    public void test93FakeHostName(){
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-host", "muhahhahahahahahaa", "-port", PORT, customer);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(String.join(
            System.getProperty("line.separator"),
            "** Unable to connect to the server",
            "usage: java edu.pdx.cs410J.manpreet.Project4 [options] <args>",
            "args are (in this order):",
            "  customer               Person whose phone bill we’re modeling",
            "  callerNumber           Phone number of caller",
            "  calleeNumber           Phone number of person who was called",
            "  startTime              Date and time (am/pm) call began",
            "  endTime                Date and time (am/pm) call ended",
            "options are (options may appear in any order):",
            "  -host hostname         Host computer on which the server runs",
            "  -port port             Port on which the server is listening",
            "  -search                Phone calls should be searched for",
            "  -print                 Prints a description of the new phone call",
            "  -README                Prints a README for this project and exits"
        )));
    }

    @Test
    public void test94BadPortNumber(){
        String customer = "NoCustomer";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", "923123", customer);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(String.join(
            System.getProperty("line.separator"),
            "** Port number can't be greater than 65535!",
            "usage: java edu.pdx.cs410J.manpreet.Project4 [options] <args>",
            "args are (in this order):",
            "  customer               Person whose phone bill we’re modeling",
            "  callerNumber           Phone number of caller",
            "  calleeNumber           Phone number of person who was called",
            "  startTime              Date and time (am/pm) call began",
            "  endTime                Date and time (am/pm) call ended",
            "options are (options may appear in any order):",
            "  -host hostname         Host computer on which the server runs",
            "  -port port             Port on which the server is listening",
            "  -search                Phone calls should be searched for",
            "  -print                 Prints a description of the new phone call",
            "  -README                Prints a README for this project and exits"
        )));
    }
}