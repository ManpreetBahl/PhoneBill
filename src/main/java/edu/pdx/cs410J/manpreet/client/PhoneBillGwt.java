package edu.pdx.cs410J.manpreet.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {
  private final Alerter alerter;
  private final PhoneBillServiceAsync phoneBillService;
  private final Logger logger;

  private FlexTable addResults;
  private FlexTable searchResults;

  DeckPanel deck = new DeckPanel();

  @VisibleForTesting
  Button showPhoneBillButton;

  @VisibleForTesting
  Button showUndeclaredExceptionButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;
  
  public PhoneBillGwt() {
    this(Window::alert);
  }

  @VisibleForTesting
  PhoneBillGwt(Alerter alerter) {
    this.alerter = alerter;
    this.phoneBillService = GWT.create(PhoneBillService.class);
    this.logger = Logger.getLogger("phoneBill");
    Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
  }

  private void alertOnException(Throwable throwable) {
    Throwable unwrapped = unwrapUmbrellaException(throwable);
    StringBuilder sb = new StringBuilder();
    sb.append(unwrapped.toString());
    sb.append('\n');

    for (StackTraceElement element : unwrapped.getStackTrace()) {
      sb.append("  at ");
      sb.append(element.toString());
      sb.append('\n');
    }

    this.alerter.alert(sb.toString());
  }

  private Throwable unwrapUmbrellaException(Throwable throwable) {
    if (throwable instanceof UmbrellaException) {
      UmbrellaException umbrella = (UmbrellaException) throwable;
      if (umbrella.getCauses().size() == 1) {
        return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
      }

    }

    return throwable;
  }

  private void checkPhoneNumber(String phoneNum){
    RegExp pattern = RegExp.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    com.google.gwt.regexp.shared.MatchResult result = pattern.exec(phoneNum);

    if (result == null){
      throw new IllegalArgumentException();
    }
  }

  private FlexTable newTable(){
    FlexTable table = new FlexTable();
    table.setBorderWidth(3);
    table.setText(0,0, "No phone calls to display");
    table.setWidth("100%");
    return table;
  }

  private void displayResult(FlexTable table, String customer, PhoneBill bill){
    table.removeAllRows();
    table.setText(0,0, "Customer: " + customer);

    FlexCellFormatter fcf = table.getFlexCellFormatter();
    fcf.setColSpan(0,0,5);

    if(bill != null){
      table.setText(1,0, "Caller");
      table.setText(1,1, "Callee");
      table.setText(1,2, "Start Time");
      table.setText(1,3, "End Time");
      table.setText(1,4, "Duration");

      int index = 2;
      for(PhoneCall pc : bill.getPhoneCalls()){
        table.setText(index, 0, pc.getCaller());
        table.setText(index, 1, pc.getCallee());
        table.setText(index, 2, pc.getStartTimeString());
        table.setText(index, 3, pc.getEndTimeString());
        table.setText(index, 4, Long.toString(pc.duration()));
        ++index;
      }
    }
    else{
      table.setText(1,0, "No phone calls to display!");
    }
  }

  /**
   * This creates the navigation bar for the application. Currently, the user can view
   * the README information in an alert box when clicked.
   * @return MenuBar panel
   */
  private MenuBar navbar(){
    //README command
    Command README = () -> this.deck.showWidget(0);
    MenuBar help = new MenuBar();
    help.addItem("README", README);

    //Add a phone call command
    Command ADD = () -> this.deck.showWidget(1);

    //Search phone calls command
    Command SEARCH = () -> this.deck.showWidget(2);

    // Make a new menu bar, adding a few cascading menus to it.
    MenuBar menu = new MenuBar();
    menu.addItem("Help", help);
    menu.addItem("Add Phone Call", ADD);
    menu.addItem("Search Phone Calls", SEARCH);

    return menu;
  }

  private VerticalPanel showREADME(){
    Label header = new Label("README");
    header.getElement().getStyle().setMarginTop(1, Unit.PCT);
    header.getElement().getStyle().setMarginBottom(0.5, Unit.PCT);
    header.getElement().getStyle().setTextAlign(TextAlign.CENTER);
    header.getElement().getStyle().setFontSize(2, Unit.EM);
    header.getElement().getStyle().setFontWeight(FontWeight.BOLD);

    FlexTable README = newTable();
    README.removeAllRows();
    README.setText(0,0,"Assignment");
    README.setText(0,1,"Project 5");
    README.setText(1,0,"Author");
    README.setText(1,1,"Manpreet Bahl");
    README.setText(2,0,"Description");
    README.setText(2,1, "This project expands the phonebill application that has been developed throughout"+
        "the term into a Rich Internet Application (RIA). This project utilizes Google Web"+
        "Toolkit (GWT) to develop the application in Java and having it compile to HTML,"+
        "JavaScript, CSS while allowing the developer to utilize the pros of Java."
    );

    VerticalPanel vp = new VerticalPanel();
    vp.getElement().setAttribute("style", "width:100%;padding-left:10%;padding-right:10%");

    vp.add(header);
    vp.add(README);
    return vp;
  }

  private VerticalPanel addPhoneCallForm(){
    TextBox customer;
    TextBox callee;
    TextBox caller;
    TextBox startTime;
    TextBox endTime;

    Label header = new Label("Add a new phone call");
    header.getElement().getStyle().setMarginTop(1, Unit.PCT);
    header.getElement().getStyle().setMarginBottom(0.5, Unit.PCT);
    header.getElement().getStyle().setTextAlign(TextAlign.CENTER);
    header.getElement().getStyle().setFontSize(2, Unit.EM);
    header.getElement().getStyle().setFontWeight(FontWeight.BOLD);

    //Customer Name
    Label customerLabel = new Label("Name");
    customer = new TextBox();
    customer.getElement().setAttribute("placeholder", "Please enter your name");
    customer.getElement().setAttribute("style", "width:100%;");

    //Callee Phone Number
    Label calleeLabel = new Label("Callee Phone Number");
    calleeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    callee = new TextBox();
    callee.getElement().setAttribute("placeholder", "Please enter callee number (XXX-XXX-XXXX)");
    callee.getElement().getStyle().setWidth(100, Unit.PCT);

    //Caller Phone Number
    Label callerLabel = new Label("Caller Phone Number");
    callerLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    caller = new TextBox();
    caller.getElement().setAttribute("placeholder", "Please enter caller number (XXX-XXX-XXXX)");
    caller.getElement().getStyle().setWidth(100, Unit.PCT);

    //Start Time
    Label startTimeLabel = new Label("Start Time");
    startTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    startTime = new TextBox();
    startTime.getElement().setAttribute("placeholder", "Please enter start time in MM/dd/yyyy hh:mm a");
    startTime.getElement().getStyle().setWidth(100, Unit.PCT);

    //End Time
    Label endTimeLabel = new Label("End Time");
    endTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    endTime = new TextBox();
    endTime.getElement().setAttribute("placeholder", "Please enter end time in MM/dd/yyyy hh:mm a");
    endTime.getElement().getStyle().setWidth(100, Unit.PCT);

    //Submit Button Form
    Button submit = new Button("Add Phone Call");
    submit.getElement().getStyle().setMarginTop(1, Unit.PCT);
    submit.addClickHandler(changeEvent ->{
      //Check callee phone number
      try{
        checkPhoneNumber(callee.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Callee phone number is malformed! Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9.");
        return;
      }

      //Check caller phone number
      try{
        checkPhoneNumber(caller.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Caller phone number is malformed! Phone numbers have the form nnn-nnn-nnnn where n is a number 0-9.");
        return;
      }

      //Check Start Date
      DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
      Date start;
      try{
        start = dtf.parse(startTime.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Invalid start date! Date format must be in: MM/dd/yyyy hh:mm a");
        return;
      }

      //Check End Date
      Date end;
      try{
        end = dtf.parse(endTime.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Invalid end date! Date format must be in: MM/dd/yyyy hh:mm a");
        return;
      }

      //Create PhoneCall object from user input
      PhoneCall toAdd;
      try{
        toAdd = new PhoneCall(caller.getText(), callee.getText(), start, end);
      }catch(Exception e){
        this.alerter.alert(e.getMessage());
        return;
      }

      //Call the service and pass the information
      this.phoneBillService.addPhoneCall(customer.getText(), toAdd, new AsyncCallback<PhoneBill>(){

        @Override
        public void onFailure(Throwable ex) {
          alertOnException(ex);
          customer.setText("");
          caller.setText("");
          callee.setText("");
          startTime.setText("");
          endTime.setText("");
        }

        @Override
        public void onSuccess(PhoneBill phoneBill) {
          displayResult(addResults, customer.getText(), phoneBill);
          customer.setText("");
          caller.setText("");
          callee.setText("");
          startTime.setText("");
          endTime.setText("");
        }
      });
    });

    Label tableResults = new Label("Results: ");
    tableResults.getElement().getStyle().setMarginTop(1, Unit.PCT);
    this.addResults = newTable();

    VerticalPanel vp = new VerticalPanel();
    vp.getElement().setAttribute("style", "width:100%;padding-left:10%;padding-right:10%");

    vp.add(header);
    vp.add(customerLabel);
    vp.add(customer);
    vp.add(calleeLabel);
    vp.add(callee);
    vp.add(callerLabel);
    vp.add(caller);
    vp.add(startTimeLabel);
    vp.add(startTime);
    vp.add(endTimeLabel);
    vp.add(endTime);
    vp.add(submit);
    vp.add(tableResults);
    vp.add(addResults);

    return vp;
  }

  private VerticalPanel searchDates(){
    TextBox customer;
    TextBox startTime;
    TextBox endTime;

    Label header = new Label("Search Phone Calls");
    header.getElement().getStyle().setMarginTop(1, Unit.PCT);
    header.getElement().getStyle().setMarginBottom(0.5, Unit.PCT);
    header.getElement().getStyle().setTextAlign(TextAlign.CENTER);
    header.getElement().getStyle().setFontSize(2, Unit.EM);
    header.getElement().getStyle().setFontWeight(FontWeight.BOLD);

    //Customer Name
    Label customerLabel = new Label("Name");
    customer = new TextBox();
    customer.getElement().setAttribute("placeholder", "Please enter your name");
    customer.getElement().setAttribute("style", "width:100%;");

    //Start Time
    Label startTimeLabel = new Label("Start Time");
    startTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    startTime = new TextBox();
    startTime.getElement().setAttribute("placeholder", "Please enter start time in MM/dd/yyyy hh:mm a");
    startTime.getElement().getStyle().setWidth(100, Unit.PCT);

    //End Time
    Label endTimeLabel = new Label("End Time");
    endTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    endTime = new TextBox();
    endTime.getElement().setAttribute("placeholder", "Please enter end time in MM/dd/yyyy hh:mm a");
    endTime.getElement().getStyle().setWidth(100, Unit.PCT);

    //Submit Button Form
    Button submit = new Button("Search");
    submit.getElement().getStyle().setMarginTop(1, Unit.PCT);
    submit.addClickHandler(changeEvent ->{

      //Check Start Date
      DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
      Date start;
      try{
        start = dtf.parse(startTime.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Invalid start date! Date format must be in: MM/dd/yyyy hh:mm a");
        return;
      }

      //Check End Date
      Date end;
      try{
        end = dtf.parse(endTime.getText());
      }catch (IllegalArgumentException ie){
        this.alerter.alert("Invalid end date! Date format must be in: MM/dd/yyyy hh:mm a");
        return;
      }

      //Call the service and pass the information
      this.phoneBillService.searchPhoneBill(customer.getText(), start, end, new AsyncCallback<PhoneBill>(){

        @Override
        public void onFailure(Throwable ex) {
          alertOnException(ex);
          customer.setText("");
          startTime.setText("");
          endTime.setText("");
        }

        @Override
        public void onSuccess(PhoneBill phoneBill) {
          displayResult(searchResults, customer.getText(), phoneBill);
          customer.setText("");
          startTime.setText("");
          endTime.setText("");
        }
      });
    });


    //Search Results table
    Label tableResults = new Label("Search Results: ");
    tableResults.getElement().getStyle().setMarginTop(1, Unit.PCT);
    this.searchResults = newTable();

    VerticalPanel vp = new VerticalPanel();
    vp.getElement().setAttribute("style", "width:100%;padding-left:10%;padding-right:10%");

    vp.add(header);
    vp.add(customerLabel);
    vp.add(customer);
    vp.add(startTimeLabel);
    vp.add(startTime);
    vp.add(endTimeLabel);
    vp.add(endTime);
    vp.add(submit);
    vp.add(tableResults);
    vp.add(searchResults);

    return vp;
  }

  private void throwClientSideException() {
    logger.info("About to throw a client-side exception");
    throw new IllegalStateException("Expected exception on the client side");
  }

  private void showUndeclaredException() {
    logger.info("Calling throwUndeclaredException");
    phoneBillService.throwUndeclaredException(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Void aVoid) {
        alerter.alert("This shouldn't happen");
      }
    });
  }

  private void showDeclaredException() {
    logger.info("Calling throwDeclaredException");
    phoneBillService.throwDeclaredException(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Void aVoid) {
        alerter.alert("This shouldn't happen");
      }
    });
  }

  @Override
  public void onModuleLoad() {
    setUpUncaughtExceptionHandler();

    // The UncaughtExceptionHandler won't catch exceptions during module load
    // So, you have to set up the UI after module load...
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setupUI();
      }
    });
  }

  private void setupUI() {
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(this.navbar());
    rootPanel.add(deck);

    deck.add(showREADME());
    deck.add(addPhoneCallForm());
    deck.add(searchDates());

    deck.showWidget(0);
    /*
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(this.navbar());
    rootPanel.add(addPhoneCallForm());
    rootPanel.add(new HTML("<hr  style=\"width:100%;\" />"));
    rootPanel.add(searchDates());
    */
  }

  private void setUpUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        alertOnException(throwable);
      }
    });
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
