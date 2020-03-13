//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis.http;

import java.io.IOException;

/**
 * Exception thrown when an HttpRequestPostMultipart is cancelled by the user.
 * @author Robert Fromont robert@fromont.net.nz
 */

public class RequestCancelledException
   extends IOException {
   
   private static final long serialVersionUID = 1;
   /**
    * The request that was cancelled
    * @see #getRequest()
    * @see #setRequest(HttpRequestPostMultipart)
    */
   protected HttpRequestPostMultipart rRequest;
   /**
    * Getter for {@link #rRequest}: The request that was cancelled
    * @return The request that was cancelled
    */
   public HttpRequestPostMultipart getRequest() { return rRequest; }
   /**
    * Setter for {@link #rRequest}: The request that was cancelled
    * @param rNewRequest The request that was cancelled
    */
   public RequestCancelledException setRequest(HttpRequestPostMultipart rNewRequest) { rRequest = rNewRequest;  return this;}
   
   /**
    * Default constructor
    */
   public RequestCancelledException() {
   } // end of constructor
   
   /**
    * Constructor
    */
   public RequestCancelledException(String s) {
      super(s);
   } // end of constructor
   
   /**
    * Default constructor
    */
   public RequestCancelledException(HttpRequestPostMultipart request) {	 
      setRequest(request);
   } // end of constructor

} // end of class RequestCancelledException
