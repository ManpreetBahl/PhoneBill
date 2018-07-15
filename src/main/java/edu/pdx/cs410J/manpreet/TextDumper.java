package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.*;

/**
 * This class describes the text dumper of a phone bill for a customer. Specifically, this class
 * will dump the contents of a <code>PhoneBill</code> object to a text file specified by the filepath
 * provided by the user.
 */
public class TextDumper implements PhoneBillDumper {
  //File to dump at
  File toDump;

  /**
   * This is the constructor for TextDumper which takes a filepath and performs some checks to
   * make sure it exists and whether it's valid.
   * @param filepath The path where the file resides
   * @throws NullPointerException
   * @throws IOException
   * @throws SecurityException
   */
  TextDumper(String filepath) throws NullPointerException, IOException, SecurityException {
    /*Try to create the File object based on path. NullPointerException is thrown when the filepath
      string is null
    */
    try{
      this.toDump = new File(filepath);
    }catch (NullPointerException ne){
      throw new NullPointerException("File path cannot be null!");
    }

    /* If the file doesn't exist, create a new file. If the directories specified don't exist, the
       program will exit gracefully with an error message. The application does not handle the
       creation of directories if they don't exist.
     */
    try{
      if(!this.toDump.exists()){
        this.toDump.createNewFile();
      }
    }catch (IOException ie){
      throw new IOException("Unable to create a file!");
    }catch (SecurityException se){
      throw new SecurityException("Unable to create a file! Please delegate read and write permissions to this application");
    }
  }

  /**
   * This method dumps the contents of the <code>PhoneBill</code> object to the text file specified.
   * This method will overwrite the contents of the file with the contents of <code>PhoneBill</code>
   * object
   * @param bill The <code>PhoneBill</code> object to write to file
   * @throws IOException
   */
  @Override
  public void dump(AbstractPhoneBill bill)throws IOException{
    try{
      BufferedWriter bw = new BufferedWriter(new FileWriter(this.toDump, false));
      if (!bill.getCustomer().equals("")){
        bw.write(bill.getCustomer());
        bw.newLine();
        for(Object call: bill.getPhoneCalls()){
          AbstractPhoneCall c = (AbstractPhoneCall)call;
          bw.write(c.getCallee() + "," + c.getCaller() + "," + c.getStartTimeString() + "," + c.getEndTimeString());
          bw.newLine();
        }
      }
      bw.close();
    }catch (IOException ie){
      throw new IOException("Unable to dump phone bill contents for" + bill.getCustomer());
    }
  }
}
