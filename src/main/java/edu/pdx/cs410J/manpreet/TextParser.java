package edu.pdx.cs410J.manpreet;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;
import java.io.*;

/**
 * This class describes the text parer of a phone bill for a customer. Specifically, this class
 * will read the file specified by file path and create a <code>PhoneBill</code> object from it.
 */
public class TextParser implements PhoneBillParser {
  //File to parse
  File toParse;

  /**
   * This is the constructor for TextParser which takes a filepath and performs some checks to
   * make sure it exists and whether it's valid.
   * @param filepath The path where the file resides
   * @throws NullPointerException
   * @throws IOException
   * @throws SecurityException
   */
  TextParser(String filepath) throws NullPointerException, IOException, SecurityException{
    /*Try to create the File object based on path. NullPointerException is thrown when the filepath
      string is null
    */
    try{
      this.toParse = new File(filepath);
    }catch (NullPointerException ne){
      throw new NullPointerException("File path cannot be null!");
    }

    //If the file doesn't exist, throw a FileNotFoundException
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

  /**
   * This method parses the contents of the text file and creates a <code>PhoneBill</code> object
   * from it. There are some checks implemented here: one checks for the empty file which throws
   * a ParserException, one checks for missing customer name, and the phone call data is validated
   * by the <code>PhoneCall</code> class
   * @return bill The <code>PhoneBill</code> object created from the contents of the text file
   * @throws ParserException
   * @throws IllegalArgumentException
   * @throws ArrayIndexOutOfBoundsException
   */
  @Override
  public PhoneBill parse() throws ParserException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
    PhoneBill bill = null;
    PhoneCall call;
    try{
      //Check for empty file
      if(this.toParse.exists() && this.toParse.length() == 0){
        throw new ParserException("Empty file detected!");
      }
      BufferedReader bw = new BufferedReader(new FileReader(this.toParse));
      String customer = bw.readLine();
      //Check whether this is a customer name
      if (customer == null || customer.equals("")){
        throw new ParserException("No customer name detected!");
      }
      bill = new PhoneBill(customer);

      /*Read the remaining lines, create PhoneCall objects, and add it to the PhoneBill object to be
        returned.
      */
      String callsInText;
      //Keep reading file line by line till a null character is encountered or empty string
      while((callsInText = bw.readLine()) != null && (!callsInText.isEmpty())){
        String[]phoneCallData = callsInText.split(","); //Split by comma to get array of Strings
        //Attempt to create PhoneCall object (which also validates the input)
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
