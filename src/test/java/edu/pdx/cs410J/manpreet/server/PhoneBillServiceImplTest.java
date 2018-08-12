package edu.pdx.cs410J.manpreet.server;

import edu.pdx.cs410J.manpreet.client.PhoneBill;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillServiceImplTest {

  @Ignore
  @Test
  public void serviceReturnsExpectedPhoneBill() {
    PhoneBillServiceImpl service = new PhoneBillServiceImpl();
    PhoneBill bill = service.getPhoneBill("Bob");
    assertThat(bill.getPhoneCalls().size(), equalTo(1));
  }
}
