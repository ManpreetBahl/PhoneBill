package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.*;

public class TextDumper implements PhoneBillDumper {
  File toDump;

  TextDumper(String filepath) throws NullPointerException, IOException, SecurityException {
    //Create a FILE instance
    try{
      this.toDump = new File(filepath);
    }catch (NullPointerException ne){
      throw new NullPointerException("File path cannot be null!");
    }

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

  @Override
  public void dump(AbstractPhoneBill bill)throws IOException{
    try{
      BufferedWriter bw = new BufferedWriter(new FileWriter(this.toDump, true));
      if (bill.getCustomer() != null || bill.getCustomer() != ""){
        if(this.toDump.exists() && this.toDump.length() == 0){
          bw.write(bill.getCustomer());
          bw.newLine();
        }
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
