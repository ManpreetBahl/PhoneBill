package edu.pdx.cs410J.manpreet;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
  private static final String WEB_APP = "phonebill";
  private static final String SERVLET = "calls";

  private final String url;


  /**
   * Creates a client to the Phone Bil REST service running on the given host and port.
   * @param hostName The name of the host.
   * @param port The port.
   */
  public PhoneBillRestClient( String hostName, int port )
  {
      this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
  }

  /**
   * Returns the definition for the given word.
   * @param customerName The name of the customer.
   * @return A HTTP Response object containing the result of the API request.
   * @throws IOException
   */
  public Response getPrettyPhoneBill(String customerName) throws IOException {
    return get(this.url, "customer", customerName);
  }

  /**
   * This method searches for phone calls for a customer within a given time interval
   * @param customerName The name of the customer.
   * @param startTime The start time of the of search range.
   * @param endTime The end time of the search range.
   * @return A HTTP response object containing the result of the API request.
   * @throws IOException
   */
  public Response searchPhoneBill(String customerName, String startTime, String endTime) throws IOException {
    return get(this.url, "customer", customerName, "startTime", startTime, "endTime", endTime);
  }

  /**
   * This method adds a <code>PhoneCall</code> to the customer's phone bill (or creates one).
   * @param customer The name of the customer.
   * @param toAdd The <code>PhoneCall</code> object to add
   * @return A HTTP response object containing the results of the API request.
   * @throws IOException
   */
  public Response addPhoneCall(String customer, PhoneCall toAdd) throws IOException {
    String[] formData = {
        "customer", customer,
        "caller", toAdd.getCaller(),
        "callee", toAdd.getCallee(),
        "startTime", toAdd.getStartTimeString(),
        "endTime", toAdd.getEndTimeString(),
    };

    return postToMyURL(formData);
  }

  /**
   * Makes a POST request to the specified URL
   * @param data The data to send to the server
   * @return A HTTP response object containing the results of the API request.
   * @throws IOException
   */
  @VisibleForTesting
  Response postToMyURL(String... data) throws IOException {
    return post(this.url, data);
  }

  /**
   * This deletes all phone bills on the server.
   * @return A HTTP response object containing the results of the API request.
   * @throws IOException
   */
  public Response removeAllPhoneBills() throws IOException {
    return delete(this.url);
  }
}
