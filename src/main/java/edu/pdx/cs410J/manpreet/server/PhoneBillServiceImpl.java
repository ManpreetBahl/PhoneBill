package edu.pdx.cs410J.manpreet.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.manpreet.client.PhoneBill;
import edu.pdx.cs410J.manpreet.client.PhoneCall;
import edu.pdx.cs410J.manpreet.client.PhoneBillService;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * The server-side implementation of the Phone Bill service
 */
public class PhoneBillServiceImpl extends RemoteServiceServlet implements PhoneBillService
{
  private final Map<String, PhoneBill> bills = new HashMap<String, PhoneBill>();

  /**
   * This method fetches the customer's phone bill
   * @param name Name of the customer
   * @return PhoneBill object containing the customer's information
   */
  @Override
  public PhoneBill getPhoneBill(String name) {
    return this.bills.get(name);
  }

  /**
   * This method adds a phone call for a customer. It creates a new PhoneBill object if the
   * customer doesn't exist in the HashMap.
   * @param name Name of the customer
   * @param call PhoneCall object to add
   * @return Updated/New PhoneBill object for that customer
   */
  @Override
  public PhoneBill addPhoneCall(String name, PhoneCall call){
    //Get the customer's Phone Bill. If none exist, create one and add the PhoneCall to it.
    PhoneBill bill = this.getPhoneBill(name);
    if (bill == null) {
      bill = new PhoneBill(name);
      this.bills.put(name, bill);
    }
    bill.addPhoneCall(call);
    return bill;
  }

  /**
   * This method searches for phone calls for a customer within a specified date range.
   * @param name Name of the customer
   * @param start Start date of the search
   * @param end End date of the search
   * @return PhoneBill object containing PhoneCalls for that customer within the specified date
   *         range
   */
  @Override
  public PhoneBill searchPhoneBill(String name, Date start, Date end){
    PhoneBill customer = this.getPhoneBill(name);
    if (customer == null){
      return null;
    }
    Collection<PhoneCall> callsBetweenRange = customer.getPhoneCallsBetweenDate(start, end);
    if (callsBetweenRange.size() == 0){
      return null;
    }
    else{
      PhoneBill search = new PhoneBill(name);
      for(PhoneCall pc : callsBetweenRange){
        search.addPhoneCall(pc);
      }
      return search;
    }
  }

  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
