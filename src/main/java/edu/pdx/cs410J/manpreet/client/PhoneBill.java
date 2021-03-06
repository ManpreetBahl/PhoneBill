package edu.pdx.cs410J.manpreet.client;

import edu.pdx.cs410J.AbstractPhoneBill;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

/**
 * This class describes a phone bill for a customer. It contains the name of the customer and the
 * list of phone calls made by that customer. Each phone call is a <code>PhoneCall</code> object
 * as described in the PhoneCall class.
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
  //Name of customer
  private String customer;

  //Collection of PhoneCalls by the customer
  private List<PhoneCall> calls = new ArrayList<>();

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public PhoneBill() {
    this.customer = null;
  }

  /**
   * Creates a new <code>PhoneBill</code>.
   *
   * @param customer The name of the customer for the bill
   */
  public PhoneBill(String customer){
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
    Collections.sort(this.calls);
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

  /**
   * This method finds all phone calls for the phone bill within the specified Date
   * ranges
   * @param start The start date of the search range.
   * @param end The end date of the search range.
   * @return Collection containing <code>PhoneCall</code> within that range.
   */
  public Collection<PhoneCall> getPhoneCallsBetweenDate(Date start, Date end){
    ArrayList<PhoneCall>foundList = new ArrayList<>();

    for(PhoneCall pc : this.calls){
      if(pc.getStartTime().equals(start) || pc.getStartTime().after(start)){
        if(pc.getEndTime().before(end) || pc.getEndTime().equals(end)){
          foundList.add(pc);
        }
      }
    }
    return foundList;
  }
}

