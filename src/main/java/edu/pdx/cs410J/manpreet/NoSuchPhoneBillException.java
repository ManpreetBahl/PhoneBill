package edu.pdx.cs410J.manpreet;

public class NoSuchPhoneBillException extends RuntimeException{
    private final String customerName;
    public NoSuchPhoneBillException(String customerName) {
      super(customerName);
      this.customerName = customerName;
    }
}
