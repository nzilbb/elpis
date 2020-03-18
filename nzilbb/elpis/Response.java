//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;

/**
 * A class representing the JSON response of an Elpis request.
 * @author Robert Fromont robert@fromont.net.nz
 */

public class Response {
   
   // Attributes:
   
   /**
    * The HTTP status code, or -1 if not known.
    * @see #getHttpStatus()
    */
   protected int httpStatus = -1;
   /**
    * Getter for {@link #httpStatus}: The HTTP status code, or -1 if not known.
    * @return The HTTP status code, or -1 if not known.
    */
   public int getHttpStatus() { return httpStatus; }

   /**
    * The data or result returned.
    * @see #getData()
    */
   protected JsonObject data;
   /**
    * Getter for {@link #data}: The data or result returned.
    * @return The data or result returned.
    */
   public JsonObject getData() { return data; }

   /**
    * A message returned by the server.
    * @see #getMessage()
    * @see #setMessage(String)
    */
   protected String message;
   /**
    * Getter for {@link #message}: A message returned by the server.
    * @return A message returned by the server.
    */
   public String getMessage() { return message; }

   /**
    * The value of the "status" attribute in the JSON response, or -1 if unknown.
    * @see #getStatus()
    * @see #setStatus(int)
    */
   protected int status = -1;
   /**
    * Getter for {@link #status}: The value of the "status" attribute in the JSON
    * response, or -1 if unknown. 
    * @return The value of the "status" attribute in the JSON response, or -1 if unknown.
    */
   public int getStatus() { return status; }
   
   /**
    * Raw response text.
    * @see #getRaw()
    */
   protected String raw;
   /**
    * Getter for {@link #raw}: Raw response text.
    * @return Raw response text.
    */
   public String getRaw() { return raw; }   

   /**
    * Whether to print verbose output or not.
    * @see #getVerbose()
    */
   protected boolean verbose;
   /**
    * Getter for {@link #verbose}: Whether to print verbose output or not.
    * @return Whether to print verbose output or not.
    */
   public boolean getVerbose() { return verbose; }
   
   /**
    * Whether the response content should be parsed as a JSON object.
    * @see #getExpectJson()
    * @see #setExpectJson(boolean)
    */
   protected boolean expectJson = true;
   /**
    * Getter for {@link #expectJson}: Whether the response content should be parsed as a
    * JSON object. 
    * @return Whether the response content should be parsed as a JSON object.
    */
   public boolean getExpectJson() { return expectJson; }
   /**
    * Setter for {@link #expectJson}: Whether the response content should be parsed as a
    * JSON object. 
    * @param newExpectJson Whether the response content should be parsed as a JSON object.
    */
   public Response setExpectJson(boolean newExpectJson) { expectJson = newExpectJson; return this; }
   // Methods:
   
   /**
    * Default constructor.
    */
   public Response() {
   } // end of constructor

   /**
    * Constructor from InputStream.
    * @param input The stream to read from.
    */
   public Response(InputStream input) throws IOException {      
      load(input);
   } // end of constructor
   
   /**
    * Constructor from InputStream.
    * @param input The stream to read from.
    * @param verbose The verbosity setting to use.
    */
   public Response(InputStream input, boolean verbose) throws IOException {      
      this.verbose = verbose;
      load(input);
   } // end of constructor
   
   /**
    * Constructor from HttpURLConnection.
    * @param connection The connection to read from.
    * @param verbose The verbosity setting to use.
    */
   public Response(HttpURLConnection connection, boolean verbose) throws IOException {
      this(connection, verbose, true);
   }
   
   /**
    * Constructor from HttpURLConnection.
    * @param connection The connection to read from.
    * @param verbose The verbosity setting to use.
    * @param expectJson Whether the response content should be parsed as a JSON object.
    */
   public Response(HttpURLConnection connection, boolean verbose, boolean expectJson)
      throws IOException {
      
      this.expectJson = expectJson;
      this.verbose = verbose;

      httpStatus = connection.getResponseCode();
      if (verbose) System.out.println("HTTP status: " + connection.getResponseCode());

      if (httpStatus == HttpURLConnection.HTTP_OK)
      {
         load(connection.getInputStream());
      }
      else
      {
         if (verbose) {
            System.out.println(
               "HTTP error: " + httpStatus + ": " + connection.getResponseMessage());
         }
         load(connection.getErrorStream());
      }
   } // end of constructor
   
   /**
    * Loads the response from the given stream.
    * @param input The stream to read from.
    * @return A reference to this object,
    * @throws IOException, JSONException
    */
   public Response load(InputStream input) throws IOException {
      StringBuilder content = new StringBuilder();
      BufferedReader reader = new BufferedReader(
         new InputStreamReader(input, StandardCharsets.UTF_8));
      String line = reader.readLine();
      while (line != null)
      {
         if (content.length() > 0) content.append("\n");
         content.append(line);
      
         line = reader.readLine();
      } // next line
      reader.close();
      return load(content.toString());
   } // end of load()
   
   /**
    * Loads the response from the given stream.
    * @param text The raw text of the response.
    * @return A reference to this object,
    */
   public Response load(String text) {
      
      raw = text;
      if (verbose) System.out.println("raw: " + raw);
      if (raw != null && raw.length() > 0 && expectJson) {
         try {
            
            JsonObject json = Json.createReader(new StringReader(raw)).readObject();
            
            status = json.getInt("status");
            if (verbose) System.out.println("status: " + status);
            
            try {
               data = json.getJsonObject("data");      
               if (verbose) System.out.println("data: " + data);
            } catch(ClassCastException exception) {
               // maybe it's a string
               message = json.getString("data");      
               if (verbose) System.out.println("message: " + message);
            }
            
         } catch (JsonParsingException x) {
            // not JSON response
            if (verbose) {
               System.out.println("JSONException: " + x.getMessage());
               System.out.println(raw);
            }
            message = "Response not JSON: " + raw;
         }
      } // text not blank 
      return this;
   } // end of load()
   
   /**
    * Convenience method for checking whether the response any errors. If so, a
    * corresponding StoreException will be thrown.
    * @return A reference to this object,
    * @throws StoreException
    */
   public Response checkForErrors() throws ElpisException {
      if ((expectJson && status != 200)
          || (httpStatus > 0 && httpStatus != HttpURLConnection.HTTP_OK)) {
         throw new ElpisException(this);
      }
      return this;
   } // end of checkForErrors()
   
   /**
    * Expressess the response as a string.
    * @return A string representing the response.
    */
   public String toString() {
      if (raw != null && raw.length() > 0) return raw;
      if (message != null) return message;
      if (httpStatus > 0) return "HTTP " + httpStatus;
      return "";
   } // end of toString()

   
} // end of class Response
