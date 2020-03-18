//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Vector;
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

   /** Supported functions and parameters for validation */
   private static String[][] functions = {
      { "datasetList" },
      { "datasetNew", "<name>" }, 
      { "datasetLoad", "<name>" }, 
      { "datasetSettings", "<tier>" }, 
      { "datasetFiles", "<wav-or-eaf-file> [<wav-or-eaf-file> ...]" }, 
      { "datasetPrepare" }, 
      { "pronDictList" }, 
      { "pronDictNew", "<name>", "<dataset_name>" }, 
      { "pronDictLoad", "<name>" },
      { "pronDictL2S", "<file>" },
      { "pronDictGenerateLexicon" },
      { "pronDictSaveLexicon", "<file>" },
      { "modelList" }, 
      { "modelNew", "<name>", "<pron_dict_name>" }, 
      { "modelLoad", "<name>" }, 
      { "modelSettings", "<ngram>" },
      { "modelTrain" },
      { "modelStatus" },
      { "modelResults" },
      { "transcriptionNew", "<file>" },
      { "transcriptionTranscribe" },
      { "transcriptionStatus" },
      { "transcriptionText" },
      { "transcriptionElan" },
      { "configReset" }
   };

   /** Command-line entrypoint. */
   public static void main(String argv[]) {
      boolean verbose = false;
      if (argv.length > 0 && argv[0].equals("-v")) {
         verbose = true;
         argv = Arrays.copyOfRange(argv, 1, argv.length);
         System.out.println("New args:");
         for (String a : argv) System.out.println(a);
      }
      
      if (argv.length < 2) {
         printUsage(null);
         return;
      } else {
         // validate parameters
         for (int f = 0; f < functions.length; f++) {
            // is this the function?
            if (functions[f][0].equals(argv[1])) {
               if (argv.length - 1 < functions[f].length) {
                  System.err.println("Not enough parameters for " + argv[1]);
                  printUsage(argv[1]);
                  return;
               } else {
                  break;
               }
            } // found the function definition
         } // next function
         try {
            Elpis elpis = new Elpis(argv[0])
               .setVerbose(verbose);
            // elpis.setVerbose(true);
            if (argv[1].equalsIgnoreCase("datasetList")){
               elpis.datasetList();
            } else if (argv[1].equalsIgnoreCase("datasetNew")){
               elpis.datasetNew(argv[2]);
            } else if (argv[1].equalsIgnoreCase("datasetLoad")){
               elpis.datasetLoad(argv[2]);
            } else if (argv[1].equalsIgnoreCase("datasetSettings")){
               elpis.datasetSettings(argv[2]);
            } else if (argv[1].equalsIgnoreCase("datasetFiles")){
               // collect up the file arguments and validate them
               Vector<File> files = new Vector<File>();
               for (int f = 2; f < argv.length; f++) {
                  File file = new File(argv[f]);
                  if (file.exists()) {
                     files.add(file);
                  } else {
                     throw new Exception("File doesn't exist: " + argv[f]);
                  }
               } // next argument
               elpis.datasetFiles(files);
            } else if (argv[1].equalsIgnoreCase("datasetPrepare")){
               elpis.datasetPrepare();
            } else if (argv[1].equalsIgnoreCase("pronDictList")){
               elpis.pronDictList();
            } else if (argv[1].equalsIgnoreCase("pronDictNew")){
               elpis.pronDictNew(argv[2], argv[3]);
            } else if (argv[1].equalsIgnoreCase("pronDictLoad")){
               elpis.pronDictLoad(argv[2]);
            } else if (argv[1].equalsIgnoreCase("pronDictL2S")){
               // validate file
               File file = new File(argv[2]);
               if (!file.exists()) {
                  throw new Exception("File doesn't exist: " + argv[2]);
               }
               elpis.pronDictL2S(file);
            } else if (argv[1].equalsIgnoreCase("pronDictGenerateLexicon")){
               elpis.pronDictGenerateLexicon();
            } else if (argv[1].equalsIgnoreCase("pronDictSaveLexicon")){
               // validate file
               File file = new File(argv[2]);
               if (!file.exists()) {
                  throw new Exception("File doesn't exist: " + argv[2]);
               }
               elpis.pronDictSaveLexicon(file);
            } else if (argv[1].equalsIgnoreCase("modelList")){
               elpis.modelList();
            } else if (argv[1].equalsIgnoreCase("modelNew")){
               elpis.modelNew(argv[2], argv[3]);
            } else if (argv[1].equalsIgnoreCase("modelLoad")){
               elpis.modelLoad(argv[2]);
            } else if (argv[1].equalsIgnoreCase("modelSettings")){
               int ngram = Integer.parseInt(argv[2]);
               elpis.modelSettings(ngram);
            } else if (argv[1].equalsIgnoreCase("modelTrain")){
               elpis.modelTrain();
            } else if (argv[1].equalsIgnoreCase("modelStatus")){
               elpis.modelStatus();
            } else if (argv[1].equalsIgnoreCase("modelResults")){
               elpis.modelResults();
            } else if (argv[1].equalsIgnoreCase("transcriptionNew")){
               // validate file
               File file = new File(argv[2]);
               if (!file.exists()) {
                  throw new Exception("File doesn't exist: " + argv[2]);
               }
               elpis.transcriptionNew(file);
            } else if (argv[1].equalsIgnoreCase("transcriptionTranscribe")){
               elpis.transcriptionTranscribe();
            } else if (argv[1].equalsIgnoreCase("transcriptionStatus")){
               elpis.transcriptionStatus();
            } else if (argv[1].equalsIgnoreCase("transcriptionText")){
               elpis.transcriptionText();
            } else if (argv[1].equalsIgnoreCase("transcriptionElan")){
               File eaf = elpis.transcriptionElan();
               if (verbose) System.err.println("ELAN file: " + eaf.getPath());
               eaf.delete();
            } else if (argv[1].equalsIgnoreCase("configReset")){
               elpis.configReset();
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
         } catch(NumberFormatException exception) {
            System.err.println(argv[1] + ": Could not parse number - " + exception.getMessage());
         } catch(Exception exception) {
            System.err.println(argv[1] + ": " + exception.getMessage());
            if (!exception.getClass().equals(Exception.class))
            exception.printStackTrace(System.err);
         }
      }
   }

   private static void printUsage(String function) {
      System.err.println("Usage:");
      if (function == null) {
         System.err.println("java -jar nzilbb.elpis.jar [-v] elpis-url function [args...]");
         System.err.println(" -v : verbose output");
         System.err.println("functions:");
         for (int f = 0; f < functions.length; f++) {
            // is this the function?
            System.err.print("  " + functions[f][0]);
            for (int a = 1; a < functions[f].length; a++) {
               System.err.print(" " + functions[f][a]);
            }
            System.err.println();
         } // next function
      } else {
         for (int f = 0; f < functions.length; f++) {
            // is this the function?
            if (functions[f][0].equals(function)) {
               System.err.print("java -jar nzilbb.elpis.jar [-v] elpis-url ");
               System.err.print(function);
               for (int a = 1; a < functions[f].length; a++) {
                  System.err.print(" " + functions[f][a]);
               }
               System.err.println();
               System.err.println(" -v : verbose output");
               break;
            } // found the function definition
         } // next function
      }
   }

} // end of class CommandLine
