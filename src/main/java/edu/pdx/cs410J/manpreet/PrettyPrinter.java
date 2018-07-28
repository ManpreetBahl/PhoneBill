package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * This class pretty prints a <code>PhoneBill</code> object in a table format to either a file
 * or to standard output.
 */
public class PrettyPrinter implements PhoneBillDumper {
  private BufferedWriter bw;
  private PrintWriter pw;

  PrettyPrinter(PrintWriter pw){
    this.pw = pw;
  }

  public void prettyDump(AbstractPhoneBill toDump){
    if(toDump != null){
      StringBuffer sf = new StringBuffer();
      for(int i = 0; i < 131; i++){
        sf.append("-");
      }
      pw.println("PHONE BILL: " + toDump.getCustomer());
      pw.println(sf.toString());
      pw.println(String.format("|%25s|%25s|%25s|%25s|%25s|", "Caller", "Callee","Start Time", "End Time", "Duration(minutes)"));
      pw.println(sf.toString());
      for(Object c: toDump.getPhoneCalls()){
        PhoneCall call = (PhoneCall) c;
        pw.println(String.format("|%25s|%25s|%25s|%25s|%25s|", call.getCaller(),call.getCallee(),
            call.getStartTimeString(), call.getEndTimeString(),
            call.duration()));
        pw.println(sf.toString());
      }
      pw.flush();
      pw.close();
    }
  }

  /**
   * This is the constructor which initializes a BufferedWriter to write to either a text file
   * or to standard output
   * @param filepath The path of the text file. "-" for standard output
   * @throws IOException If an error occurred initializing BufferedWriter or creating the text file
   */
  PrettyPrinter(String filepath) throws IOException{
    if (filepath.equals("-")) {
      bw = new BufferedWriter(new OutputStreamWriter(System.out));
    }
    else{
      /*Try to create the File object based on path. NullPointerException is thrown when the filepath
        string is null
      */
      File toDump;
      try{
        toDump = new File(filepath);
      }catch (NullPointerException ne){
        throw new NullPointerException("File path cannot be null!");
      }

      /*If the file doesn't exist, create a new file. If the directories specified don't exist, the
         program will exit gracefully with an error message.
       */
      try{
        if(!toDump.exists()){
          File parent = toDump.getParentFile();
          if (parent != null){
            parent.mkdirs();
          }
          toDump.createNewFile();
        }
        bw = new BufferedWriter(new FileWriter(toDump));
      }catch (IOException ie){
        throw new IOException("Unable to create a file!");
      }catch (SecurityException se){
        throw new SecurityException("Unable to create a file! Please delegate read and write permissions to this application");
      }
    }
  }

  /**
   * This method dumps the contents of a <code>PhoneBill</code> in a nice tabular format.
   * @param bill The <code>PhoneBill</code> object to dump the contents of.
   * @throws IOException If an error occurred when attempting to write the data.
   */
  @Override
  public void dump(AbstractPhoneBill bill) throws IOException{
    try{
      if(bill != null){
        StringBuffer sf = new StringBuffer();
        for(int i = 0; i < 131; i++){
          sf.append("-");
        }
        bw.write("PHONE BILL: " + bill.getCustomer());
        bw.newLine();
        bw.write(sf.toString());
        bw.newLine();
        bw.write(String.format("|%25s|%25s|%25s|%25s|%25s|", "Caller", "Callee","Start Time", "End Time", "Duration(minutes)"));
        bw.newLine();
        bw.write(sf.toString());
        bw.newLine();
        for(Object c: bill.getPhoneCalls()){
          PhoneCall call = (PhoneCall) c;
          bw.write(String.format("|%25s|%25s|%25s|%25s|%25s|", call.getCaller(),call.getCallee(),
              call.getStartTimeString(), call.getEndTimeString(),
              call.duration()));
          bw.newLine();
          bw.write(sf.toString());
          bw.newLine();
        }
        bw.flush();
        bw.close();
      }
    }catch(IOException ie){
      throw new IOException("Unable to pretty print this file!");
    }
  }
}
