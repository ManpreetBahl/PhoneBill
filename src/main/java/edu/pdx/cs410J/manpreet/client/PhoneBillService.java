package edu.pdx.cs410J.manpreet.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;

/**
 * A GWT remote service that returns a dummy Phone Bill
 */
@RemoteServiceRelativePath("phoneBill")
public interface PhoneBillService extends RemoteService {

  /**
   * Returns the a dummy Phone Bill
   */
  public PhoneBill getPhoneBill(String name);

  public PhoneBill addPhoneCall(String name, PhoneCall call);

  public PhoneBill searchPhoneBill(String name, Date start, Date end);

  /**
   * Always throws an undeclared exception so that we can see GWT handles it.
   */
  void throwUndeclaredException();

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException() throws IllegalStateException;

}
