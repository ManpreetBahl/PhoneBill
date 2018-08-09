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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {
  private final Alerter alerter;
  private final PhoneBillServiceAsync phoneBillService;
  private final Logger logger;

  private TextBox customer;
  private TextBox callee;
  private TextBox caller;
  private TextBox startTime;
  private TextBox endTime;

  @VisibleForTesting
  Button showPhoneBillButton;

  @VisibleForTesting
  Button showUndeclaredExceptionButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;
  
  public PhoneBillGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
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

  /**
   * This creates the navigation bar for the application. Currently, the user can view
   * the README information in an alert box when clicked.
   * @return MenuBar panel
   */
  private MenuBar navbar(){
    // Make a command that we will execute from all leaves.
    Command README = () -> Window.alert("Assignment: Project 5\n"+
        "Author: Manpreet Bahl\n"+
        "This project expands the phonebill application that has been developed throughout"+
        "the term into a Rich Internet Application (RIA). This project utilizes Google Web"+
        "Toolkit (GWT) to develop the application in Java and having it compile to HTML,"+
        "JavaScript, CSS while allowing the developer to utilize the pros of Java."
    );
    MenuBar help = new MenuBar();
    help.addItem("README", README);

    // Make a new menu bar, adding a few cascading menus to it.
    MenuBar menu = new MenuBar();
    menu.addItem("Help", help);

    return menu;
  }

  private VerticalPanel addPhoneCallForm(){
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
    callee.getElement().setAttribute("placeholder", "Please enter callee number");
    callee.getElement().getStyle().setWidth(100, Unit.PCT);


    //Caller Phone Number
    Label callerLabel = new Label("Caller Phone Number");
    callerLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    caller = new TextBox();
    caller.getElement().setAttribute("placeholder", "Please enter caller number");
    caller.getElement().getStyle().setWidth(100, Unit.PCT);

    //Start Time
    Label startTimeLabel = new Label("Start Time");
    startTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    startTime = new TextBox();
    startTime.getElement().setAttribute("placeholder", "Please enter start time in MM/dd/yyyy hh:mm a");
    startTime.getElement().getStyle().setWidth(100, Unit.PCT);
    startTime.addChangeHandler(changeEvent -> {
      String text = startTime.getText();
      DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
      try{
        dtf.parse(text);
      }catch (IllegalArgumentException ie){
        alerter.alert("Invalid start date: " + text);
      }
    });

    //End Time
    Label endTimeLabel = new Label("End Time");
    endTimeLabel.getElement().getStyle().setMarginTop(1, Unit.PCT);
    endTime = new TextBox();
    endTime.getElement().setAttribute("placeholder", "Please enter end time in MM/dd/yyyy hh:mm a");
    endTime.getElement().getStyle().setWidth(100, Unit.PCT);
    endTime.addChangeHandler(changeEvent -> {
      String text = endTime.getText();
      DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
      try{
        dtf.parse(text);
      }catch (IllegalArgumentException ie){
        alerter.alert("Invalid end date: " + text);
      }
    });

    //Submit Button Form
    Button submit = new Button("Add Phone Call");
    submit.getElement().getStyle().setMarginTop(1, Unit.PCT);

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

  private void showPhoneBill() {
    logger.info("Calling getPhoneBill");
    phoneBillService.getPhoneBill(new AsyncCallback<PhoneBill>() {

      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(PhoneBill phoneBill) {
        StringBuilder sb = new StringBuilder(phoneBill.toString());
        Collection<PhoneCall> calls = phoneBill.getPhoneCalls();
        for (PhoneCall call : calls) {
          sb.append(call);
          sb.append("\n");
        }
        alerter.alert(sb.toString());
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
    rootPanel.add(addPhoneCallForm());
    rootPanel.add(new HTML("<hr  style=\"width:100%;\" />"));
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
