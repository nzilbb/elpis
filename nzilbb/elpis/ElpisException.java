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

   /**
    * The response.
    * @see #getResponse()
    */
   protected Response response;
   /**
    * Getter for {@link #response}: The response.
    * @return The response.
    */
   public Response getResponse() { return response; }

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
   
   /**
    * Constructor from failure response.
    * @param response The response.
    */
   public ElpisException(Response response) {
      super(GenerateMessage(response));
      this.response = response;      
   } // end of constructor

   /**
    * Generates the exception message.
    * @param response
    * @return The message.
    */
   static private String GenerateMessage(Response response) {
      
      if (response.getStatus() != 200) {
         if (response.getMessage() != null) {
            return response.getMessage() + " ("+response.getMessage()+")";
         } else {         
            return "Status " + response.getStatus();
         }
      }
      if (response.getHttpStatus() > 0) {
         return "HTTP status " + response.getHttpStatus();
      }
      return null;
   } // end of generateMessage()

} // end of class ElpisException
