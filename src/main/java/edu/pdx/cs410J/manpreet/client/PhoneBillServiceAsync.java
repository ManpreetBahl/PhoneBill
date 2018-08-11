package edu.pdx.cs410J.manpreet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;

/**
 * The client-side interface to the phone bill service
 */
public interface PhoneBillServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void getPhoneBill(String name, AsyncCallback<PhoneBill> async);

  void addPhoneCall(String name, PhoneCall call, AsyncCallback<PhoneBill> async);

  void searchPhoneBill(String name, Date start, Date end, AsyncCallback<PhoneBill> async);

  /**
   * Always throws an exception so that we can see how to handle uncaught
   * exceptions in GWT.
   */
  void throwUndeclaredException(AsyncCallback<Void> async);

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException(AsyncCallback<Void> async);

}
