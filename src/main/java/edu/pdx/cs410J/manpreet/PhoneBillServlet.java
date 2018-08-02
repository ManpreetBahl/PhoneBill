package edu.pdx.cs410J.manpreet;

import com.google.common.annotations.VisibleForTesting;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{
  static final String CUSTOMER_PARAMETER = "customer";
  private static final String CALLER_PARAMETER = "caller";
  private static final String CALLEE_PARAMETER = "callee";
  private static final String START_TIME_PARAMETER = "startTime";
  private static final String END_TIME_PARAMETER = "endTime";

  private final Map<String, PhoneBill> bills = new HashMap<String, PhoneBill>();
  private PhoneBill pb = null;

  /**
   * Handles both variations of GET requests. The first variation is where only the "customer"
   * parameter is specified, thus resulting in a response of all phone calls for that customer
   * in a pretty text format. The second variation is where "customer", "startTime", and "endTime"
   * are specified in the parameters. This means to search for all phone calls for that customer
   * between the time ranges.
   * @param request The HTTP request object containing the GET parameters.
   * @param response The HTTP response after processing the request.
   * @throws ServletException Throws any ServletExceptions that may occur.
   * @throws IOException Throws any IOException that may happen when using the PrintWriter.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

    //Get parameters
    String customer = getParameter(CUSTOMER_PARAMETER, request);
    String startTime = getParameter(START_TIME_PARAMETER, request);
    String endTime = getParameter(END_TIME_PARAMETER, request);

    //Make sure that at least the customer name is specified
    if(customer == null){
      missingRequiredParameter(response, CUSTOMER_PARAMETER);
    }

    //First variation of GET requests where only customer name is specified. Gets all phone calls
    //for that customer
    if(customer != null && startTime == null && endTime == null){
      pb = getPhoneBill(customer);
      if (pb == null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "There are no phonebills for " + customer);
      }
      else{
        PrettyPrinter pp = new PrettyPrinter(response.getWriter());
        pp.prettyDump(pb);
        response.setStatus(HttpServletResponse.SC_OK);
      }
    }
    //Second variation of GET request where all 3 parameters are specified. Gets all phone calls for
    //that customer within the specified range.
    else if(customer != null && startTime != null && endTime != null){
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
      Date start = null;
      Date end = null;

      try{
        start = sdf.parse(startTime);
        end = sdf.parse(endTime);
      }catch (ParseException pe){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The datetime entered is malformed!");
      }

      if(start.after(end)){
        response.sendError(HttpServletResponse.SC_CONFLICT, "Start time cannot be after end time!");
      }

      PhoneBill pb = getPhoneBill(customer);
      if(pb == null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "There are no phonebills for " + customer);
      }
      else{
        PhoneBill pbClone = new PhoneBill(pb.getCustomer());
        Collection<PhoneCall> callsBetweenRange = pb.getPhoneCallsBetweenDate(start, end);
        if (callsBetweenRange.size() == 0){
          response.sendError(HttpServletResponse.SC_NO_CONTENT, "There are no phone calls between that range");
        }
        else{
          for(PhoneCall pc : callsBetweenRange){
            pbClone.addPhoneCall(pc);
          }
          PrettyPrinter pp = new PrettyPrinter(response.getWriter());
          pp.prettyDump(pbClone);
          response.setStatus(HttpServletResponse.SC_OK);
        }
      }
    }
    else{
      //The GET request sent is invalid
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to process malformed request");
    }
  }

  /**
   * Handles the POST request to add a <code>PhoneCall</code> to a <code>PhoneBill</code>.
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @throws ServletException Throws any ServletExceptions that may occur.
   * @throws IOException Throws any IOException that may happen when using the PrintWriter.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

    //Check all parameters to make sure they are present before adding them to PhoneBill
    String customer = getParameter(CUSTOMER_PARAMETER, request );
    if (customer == null) {
      missingRequiredParameter(response, CUSTOMER_PARAMETER);
      return;
    }
    String caller = getParameter(CALLER_PARAMETER, request);
    if (caller == null) {
      missingRequiredParameter(response, CALLER_PARAMETER);
      return;
    }

    String callee = getParameter(CALLEE_PARAMETER, request);
    if (callee == null) {
      missingRequiredParameter(response, CALLEE_PARAMETER);
      return;
    }

    String startTime = getParameter(START_TIME_PARAMETER, request);
    if (startTime == null) {
      missingRequiredParameter(response, START_TIME_PARAMETER);
      return;
    }

    String endTime = getParameter(END_TIME_PARAMETER, request);
    if (endTime == null) {
      missingRequiredParameter(response, END_TIME_PARAMETER);
      return;
    }

    //Parse the dates to make sure they are in the required format
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    Date start = null;
    Date end = null;
    try{
      start = sdf.parse(startTime);
      end = sdf.parse(endTime);
    }catch (ParseException pe){
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The datetime entered is malformed!");
    }

    PhoneCall call = null;
    try{
      //Create a new PhoneCall (which includes checks of its own)
      call = new PhoneCall(caller, callee, start, end);
    }catch(Exception e){
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Encountered error when adding phone call: " + e.getMessage());
    }

    //Get the customer's Phone Bill. If none exist, create one and add the PhoneCall to it.
    PhoneBill bill = getPhoneBill(customer);
    if (bill == null) {
      bill = new PhoneBill(customer);
      addPhoneBill(bill);
    }
    bill.addPhoneCall(call);

    response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/plain");

      this.bills.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allPhoneBillsDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  /**
   * This method searches the bills HashMap to find the customer's phone bill
   * @param customer The customer's phone bill to search for
   * @return <code>PhoneBill</code> object if found, null otherwise
   */
  @VisibleForTesting
  PhoneBill getPhoneBill(String customer) {
    return this.bills.get(customer);
  }

  /**
   * This method puts a <code>PhoneBill</code> object into the HashMap
   * @param bill The <code>PhoneBill</code> to put in the HashMap using the customer's name as key
   */
  @VisibleForTesting
  void addPhoneBill(PhoneBill bill) {
    this.bills.put(bill.getCustomer(), bill);
  }
}
