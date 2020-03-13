//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis;

/**
 * Elpis response exception.
 * @author Robert Fromont robert@fromont.net.nz
 */
@SuppressWarnings("serial")
public class ElpisException extends Exception {
   
   // Attributes:
   
   // Methods:
   
   /**
    * Default constructor.
    */
   public ElpisException() {
   } // end of constructor

   /**
    * Constructor with message.
    * @param message The error message.
    */
   public ElpisException(String message) {
      super(message);
   } // end of constructor

   /**
    * Constructor with cause.
    * @param cause The source exception.
    */
   public ElpisException(Throwable cause) {
      super(cause);
   } // end of constructor
   
} // end of class ElpisException
