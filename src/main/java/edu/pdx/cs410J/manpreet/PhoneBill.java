package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.ArrayList;
import java.util.Collection;

public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
    //Name of customer
    private String customer;

    //Collection of PhoneCalls by the customer
    private Collection<PhoneCall> calls = new ArrayList<>();

    /**
     * Creates a new <code>PhoneBill</code>.
     *
     * @param customer The name of the customer for the bill
     */
    PhoneBill(String customer){
        this.customer = customer;
    }

    /**
     * Get the name of the customer
     * @return Name of the customer
     */
    @Override
    public String getCustomer() {
        return this.customer;
    }

    /**
     * Add a <code>PhoneCall</code> object to the collection of PhoneCall
     * for this customer.
     *
     * @param call The <code>PhoneCall</code> object to add to the collection
     */
    @Override
    public void addPhoneCall(PhoneCall call) {
        this.calls.add(call);
    }

    /**
     * Get the collection of <code>PhoneCall</code> for this customer.
     *
     * @return The collection of <code>PhoneCall</code>
     */
    @Override
    public Collection<PhoneCall> getPhoneCalls() {
        return this.calls;
    }
}
