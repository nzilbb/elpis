//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis.util;

import java.net.MalformedURLException;
import java.io.IOException;
import nzilbb.elpis.*;

/**
 * Provides access to the Elpis API from the command line.
 * <p>This is the <q>main class</q> for <i>nzilbb.elpis.jar</i>, so it's easy to invoke
 * from the command line for performing ad-hoc API requests: e.g. 
 * <p><tt>java -jar nzilbb.elpis.jar http://0.0.0.0:5000 <b>datasetList</b></tt>
 * <p> &hellip; might print:
 * <pre>{
 *   "data": {
 *     "list": [
 *       "ds"
 *     ]
 *   }, 
 *   "status": 200
 * }</pre>
 * @author Robert Fromont robert@fromont.net.nz
 */
public class CommandLine {

   /** Command-line entrypoint. */
   public static void main(String argv[]) {
      if (argv.length < 2) {
         printUsage(null);
         return;
      } else {
         try {
            Elpis elpis = new Elpis(argv[0]);
            if (argv[1].equalsIgnoreCase("datasetList")){
               elpis.datasetList();
            } else if (argv[1].equalsIgnoreCase("pronDictList")){
               elpis.pronDictList();
            } else if (argv[1].equalsIgnoreCase("modelList")){
               elpis.modelList();
            } else {
               System.err.println("Invalid function: " + argv[1]);
               printUsage(null);
               return;
            }
            System.out.println(""+elpis.getResponse());
         } catch(MalformedURLException exception) {
            System.err.println("Invalid URL: " + argv[0]);
         } catch(ElpisException exception) {
            System.err.println("Error: " + exception);
         } catch(IOException exception) {
            System.err.println("Communications Error: " + exception);
         }
      }
   }

   private static void printUsage(String function) {
      System.err.println("Usage:");
      System.err.println("java -jar nzilbb.elpis.jar elpis-url function [args...]");
      System.err.println("functions:");
      System.err.println("  datasetList");
   }

} // end of class CommandLine
