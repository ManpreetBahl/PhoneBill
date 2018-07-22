package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class PrettyPrinter implements PhoneBillDumper {
  private File toDump = null;

  PrettyPrinter(String filepath) throws IOException{
    /*Try to create the File object based on path. NullPointerException is thrown when the filepath
      string is null
    */
    try{
      this.toDump = new File(filepath);
    }catch (NullPointerException ne){
      throw new NullPointerException("File path cannot be null!");
    }

    /* If the file doesn't exist, create a new file. If the directories specified don't exist, the
       program will exit gracefully with an error message.
     */
    try{
      if(!this.toDump.exists()){
        File parent = this.toDump.getParentFile();
        if (parent != null){
          parent.mkdirs();
        }
        this.toDump.createNewFile();
      }
    }catch (IOException ie){
      throw new IOException("Unable to create a file!");
    }catch (SecurityException se){
      throw new SecurityException("Unable to create a file! Please delegate read and write permissions to this application");
    }
  }

  @Override
  public void dump(AbstractPhoneBill bill) throws IOException{

  }
}
