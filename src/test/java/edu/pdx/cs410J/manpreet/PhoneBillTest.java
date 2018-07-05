package edu.pdx.cs410J.manpreet;

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
        bill.addPhoneCall(new PhoneCall("123-456-7890", "098-765-4321", "6/30/2018 1:00", "6/30/2018 2:00"));
        bill.addPhoneCall(new PhoneCall("123-456-7890", "098-765-4321", "02/12/1830 2:45", "1/30/2018 2:00"));
        Collection<PhoneCall> calls = bill.getPhoneCalls();
        System.out.println(calls.toString());
    }

    @Test
    public void canGetCustomerName(){
        bill = new PhoneBill("Yoda");
        assertThat(bill.getCustomer(), containsString("Yoda"));
    }
}
