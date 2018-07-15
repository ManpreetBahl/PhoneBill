package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;
import java.io.*;

public class TextParser implements PhoneBillParser {
  File toParse;

  TextParser(String filepath) throws NullPointerException, IOException, SecurityException{
    //Create a FILE instance
    try{
      this.toParse = new File(filepath);
    }catch (NullPointerException ne){
      throw new NullPointerException("File path cannot be null!");
    }

    try{
      if(!this.toParse.exists()){
        throw new FileNotFoundException();
      }
    }catch (IOException ie){
      throw new IOException("Unable to read text file!");
    }catch (SecurityException se){
      throw new SecurityException("Unable to read text file! Please delegate read and write permissions to this application");
    }

  }

  @Override
  public PhoneBill parse() throws ParserException {
    PhoneBill bill = null;
    PhoneCall call;
    try{
      BufferedReader bw = new BufferedReader(new FileReader(this.toParse));
      String customer = bw.readLine();
      bill = new PhoneBill(customer);

      String callsInText;
      while((callsInText = bw.readLine()) != null){
        String[]phoneCallData = callsInText.split(",");
        try{
          call = new PhoneCall(phoneCallData[0], phoneCallData[1], phoneCallData[2], phoneCallData[3]);
        }catch (IllegalArgumentException ie){
          throw new IllegalArgumentException(ie.getMessage());
        }catch (ArrayIndexOutOfBoundsException ae){
          throw new ArrayIndexOutOfBoundsException("The phone calls data is malformed!");
        }
        bill.addPhoneCall(call);
      }
    }catch(IOException ie){
      System.err.println("Something went wrong when reading phone bill");
    }
    return bill;
  }
}
