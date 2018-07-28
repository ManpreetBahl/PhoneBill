package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.web.HttpRequestHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link PhoneBillRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhoneBillRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private PhoneBillRestClient newPhoneBillRestClient() {
    int port = Integer.parseInt(PORT);
    return new PhoneBillRestClient(HOSTNAME, port);
  }

  @Test
  public void test0RemoveAllDictionaryEntries() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    client.removeAllPhoneBills();
  }

  @Test (expected = NoSuchPhoneBillException.class)
  public void test1EmptyServerContainsNoPhoneBills() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    client.getPrettyPhoneBill("No such customer");
  }

  @Test
  public void test2AddOnePhoneCall() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    String callerNumber = "123-456-7890";
    String calleeNumber = "234-567-8901";
    Date startTime = new Date(System.currentTimeMillis());
    Date endTime = new Date(System.currentTimeMillis() + 100000);
    PhoneCall phoneCall = new PhoneCall(callerNumber, calleeNumber, startTime, endTime);

    String customer = "Customer";
    client.addPhoneCall(customer, phoneCall);

    String pretty = client.getPrettyPhoneBill(customer);
    assertThat(pretty, containsString(customer));
    assertThat(pretty, containsString(callerNumber));
    assertThat(pretty, containsString(calleeNumber));

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    assertThat(pretty, containsString(sdf.format(startTime)));
    assertThat(pretty, containsString(sdf.format(endTime)));
  }


  @Test
  public void test4MissingRequiredParameterReturnsPreconditionFailed() throws IOException {
    PhoneBillRestClient client = newPhoneBillRestClient();
    HttpRequestHelper.Response response = client.postToMyURL();
    assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("customer")));
    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
  }

}
