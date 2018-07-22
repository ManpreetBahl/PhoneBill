package edu.pdx.cs410J.manpreet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link PhoneBill} class.
 */
public class PhoneBillTest {
    PhoneBill bill;
    PhoneCall call;

    @Test
    public void canCreatePhoneBillObject(){
        bill = new PhoneBill("Yoda");
    }

    @Test
    public void canAddPhoneCall(){
        bill = new PhoneBill("Yoda");
        call = new PhoneCall("123-456-7890", "098-765-4321", "6/30/2018 1:00", "6/30/2018 2:00");
        bill.addPhoneCall(call);
    }

    @Test
    public void canDisplayPhoneCalls(){
        canAddPhoneCall();
        Collection<PhoneCall> calls = bill.getPhoneCalls();
        System.out.println(calls.toString());
    }

    @Test
    public void canAddandDisplayMultipleCalls(){
        bill = new PhoneBill("Yoda");
        Date start = null;
        Date end = null;
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy h:mm a");
        try{
            start = sdf.parse("6/30/2018 1:00 PM");
        } catch (ParseException pe){
            System.err.println("Invalid start time! Please use AM/PM Date time format");
        }

        try{
            end = sdf.parse("6/30/2018 2:00 AM");
        } catch (ParseException pe){
            System.err.println("Invalid end time! Please use AM/PM Date time format");
        }
        bill.addPhoneCall(new PhoneCall("123-456-7890", "098-765-4321", start, end));

        try{
            start = sdf.parse("6/30/2017 4:00 PM");
        } catch (ParseException pe){
            System.err.println("Invalid start time! Please use AM/PM Date time format");
        }

        try{
            end = sdf.parse("6/30/2017 9:00 PM");
        } catch (ParseException pe){
            System.err.println("Invalid end time! Please use AM/PM Date time format");
        }
        bill.addPhoneCall(new PhoneCall("123-456-7899", "098-765-4321", start, end));
        Collection<PhoneCall> calls = bill.getPhoneCalls();
        System.out.println(calls.toString());
    }

    @Test
    public void canGetCustomerName(){
        bill = new PhoneBill("Yoda");
        assertThat(bill.getCustomer(), containsString("Yoda"));
    }
}
