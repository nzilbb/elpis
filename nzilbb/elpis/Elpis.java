//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import nzilbb.elpis.http.*;

/**
 * Client for accessing Elpis server functions programmatically.
 * <p>Functions are divided into five types, reflecting the four general steps for
 * transcription: 
 * <ol>
 *  <li>dataset&hellip;       &mdash; upload existing transcripts with their recording, to
 *                                    provide training data;</li> 
 *  <li>pronDict&hellip;      &mdash; specify a pronunciation dictionary;</li>
 *  <li>model&hellip;         &mdash; train language and acoustic models for automatic speech
 *                                    recognition;</li> 
 *  <li>transcription&hellip; &mdash; automatic transcription of new recordings</li>
 *  <li>config&hellip;        &mdash; clean up</li>
 * </ol>
 * @author Robert Fromont robert@fromont.net.nz
 */
public class Elpis {
   
   // Attributes:
   
   /**
    * Base URL for the Eplis server - e.g. http://0.0.0.0:5000/api/
    * @see #getBaseUrl()
    * @see #setBaseUrl(URL)
    */
   protected URL baseUrl;
   /**
    * Getter for {@link #baseUrl}: Base URL for the Eplis server - e.g. http://0.0.0.0:5000/api/
    * @return Base URL for the Eplis server - e.g. http://0.0.0.0:5000/api/
    */
   public URL getBaseUrl() { return baseUrl; }
   
   /**
    * Whether to print detailed logging to System.out or not.
    * @see #getVerbose()
    * @see #setVerbose(boolean)
    */
   protected boolean verbose = false;
   /**
    * Getter for {@link #verbose}: Whether to print detailed logging to System.out or not.
    * @return Whether to print detailed logging to System.out or not.
    */
   public boolean getVerbose() { return verbose; }
   /**
    * Setter for {@link #verbose}: Whether to print detailed logging to System.out or not.
    * @param newVerbose Whether to print detailed logging to System.out or not.
    */
   public Elpis setVerbose(boolean newVerbose) { verbose = newVerbose; return this; }
   
   /**
    * The last response received from the server.
    * @see #getResponse()
    * @see #setResponse(Response)
    */
   protected Response response;
   /**
    * Getter for {@link #response}: The last response received from the server.
    * @return The last response received from the server.
    */
   public Response getResponse() { return response; }
   
   // Methods:
   
   /**
    * Default constructor.
    */
   public Elpis() {
   } // end of constructor
   
   /**
    * Constructor from a URL string.
    * @param url The base URL for Elpis, e.g. "http://0.0.0.0:5000/" or "http://0.0.0.0:5000/api/"
    */
   public Elpis(String url) throws MalformedURLException {
      // ensure it ends in "/api/"
      if (!url.endsWith("/")) url += "/"; 
      if (!url.endsWith("api/")) url += "api/";
      baseUrl = new URL(url);
   } // end of constructor

   /**
    * Constructs a URL for the given resource.
    * @param resource
    * @return A URL for the given resource.
    * @throws StoreException If the URL is malformed.
    */
   protected URL makeUrl(String resource) throws IOException {
      try
      {
         return new URL(baseUrl, resource);
      }
      catch(Throwable t)
      {
         throw new IOException("Could not construct request URL.", t);
      }
   } // end of editUrl()
   
   // dataset functions
   
   /**
    * Create a new dataset.
    * @param name The name of the new dataset.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void datasetNew(String name) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of datasetNew()
   
   /**
    * List current dataset names.
    * @return A list of dataset names.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public List<String> datasetList() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("dataset/list"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("datasetList -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      JsonArray list = response.getData().getJsonArray("list");
      return list.stream().map(item->((JsonString)item).getString()).collect(Collectors.toList());
   } // end of datasetList()
   
   /**
    * Start using an existing dataset.
    * @param name The name of the existing dataset.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void datasetLoad(String name) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of datasetLoad()
   
   /**
    * Define dataset settings.
    * @param tier The name of the ELAN tier that contains the transcript.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void datasetSettings(String tier) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of datasetSettings()
   
   /**
    * Upload transcript/audio files into the dataset.
    * @param file Files to upload, which may be wav audio files and/or an ELAN .eaf transcripts.
    * @return A list of all dataset files uploaded so far.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public List<String> datasetFiles(List<File> file) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of datasetFiles()
   
   /**
    * Process the transcripts to create word/frequency lists.
    * @return A map of word types to frequencies in the uploaded transcripts.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public Map<String,Integer> datasetPrepare() throws IOException, ElpisException {
      HttpRequestPost request = new HttpRequestPost(
         makeUrl("dataset/prepare"))
         .setHeader("Accept", "dataset/prepare");
      if (verbose) System.out.println("datasetPrepare -> " + request);
      response = new Response(request.post(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      String wordlistString = response.getData().getString("wordlist");
      JsonObject wordlistJson = Json.createReader(new StringReader(wordlistString)).readObject();
      return wordlistJson.entrySet().stream()
         .collect(Collectors.toMap(Map.Entry::getKey,
                                   e -> Integer.valueOf(((JsonNumber)e.getValue()).intValue())));
   } // end of datasetPrepare()

   // pron-dict functions

   /**
    * Create a new pronunciation dictionary.
    * @param name The name of the new pronunciation dictionary.
    * @param datasetName The name of the dataset.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void pronDictNew(String name, String datasetName) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of pronDictNew()
   
   /**
    * Start using an existing pronunciation dictionary.
    * @param name The name of the existing pronunciation dictionary.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void pronDictLoad(String name) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of pronDictNew()
   
   /**
    * List the current pronunciation dictionaries.
    * @return A list of existing pronunciation dictionaries.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public List<String> pronDictList() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("pron-dict/list"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("pronDictList -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      JsonArray list = response.getData().getJsonArray("list");
      return list.stream().map(item->((JsonString)item).getString()).collect(Collectors.toList());
   } // end of pronDictList()
   
   /**
    * Specify the letter-to-sound mapping for creating a pronunciation dictionary.
    * <p>The letter to sound file is used to build a pronunciation dictionary for the
    * corpus. Make one by listing one column of all the characters in your corpus. Make a
    * second column (separated by a space) of a symbol representing how that character is
    * pronounced. You can use IPA, or SAMPA for the pronunciation symbols. You can include
    * comments in this file by beginning the comment line with <tt>#</tt>. For example:  
    * <pre># This is a comment 
    * n n
    * ng ŋ
    * r r
    * y j
    * </pre>
    * @param file The plain-text file to upload.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void pronDictL2S(File file) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of pronDictL2S()
   
   /**
    * Generate the pronunciation dictionary.
    * <p>This is a list of all words in the uploaded transcripts, followed by their
    * pronunciations as generated by combining the word spelling with the letter-to-sound
    * mapping uploaded with {@link #pronDictL2S(File)}.
    * @return A map of word types to pronunciations.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public Map<String,String> pronDictGenerateLexicon() throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of pronDictGenerateLexicon()
   
   /**
    * Update the pronunciation dictionary.
    * <p>This is a list of all words in the uploaded transcripts, mapped to their
    * pronunciations; i.e. an edited version of the pronunciation dictionary generated by 
    * {@link #pronDictGenerateLexicon()}.
    * @param lexicon A map of word types to pronunciations.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void pronDictSaveLexicon(Map<String,String> lexicon) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of pronDictSaveLexicon()
   
   // model functions

   /**
    * List the current models.
    * @return A list of existing models.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public List<String> modelList() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("model/list"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("modelList -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      JsonArray list = response.getData().getJsonArray("list");
      return list.stream().map(item->((JsonString)item).getString()).collect(Collectors.toList());
   } // end of modelList()
   
   /**
    * Create a new model for training.
    * @param name The name of the new model to create.
    * @param pronDictName The pronunciation dictionary to use.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void modelNew(String name, String pronDictName)
      throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of modelNew()
   
   /**
    * Start using an existing model.
    * @param name The name of the existing model to use.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void modelLoad(String name) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of modelLoad()
   
   /**
    * Specify model configuration.
    * @param ngram The n-gram setting (number of consecutive words to use) for the
    * language model.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void modelSettings(int ngram) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of modelSettings()      
   
   /**
    * Start the process of training models on the dataset.
    * @return The status of the training.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public String modelTrain() throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of modelTrain()      
   
   /**
    * Start the process of training models on the dataset.
    * @return The status of the training.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public String modelStatus() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("model/status"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("modelStatus -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      return response.getData().getString("status");
   } // end of modelStatus()
   
   /**
    * Get the training results - i.e. metrics for the final model performance after
    * training.
    * @return A map of metric names to their values.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public Map<String,String> modelResults() throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of modelResults()      

   // transcription functions
   
   /**
    * Upload an audio file to transcribe.
    * @param file The wav audio file to upload.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void transcriptionNew(File file) throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of transcriptionNew()
   
   /**
    * Begin the transcription process, with the last recording uploaded using
    * {@link #transcriptionNew(File)}.
    * @return The status of the transcription.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public String transcriptionTranscribe() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("transcription/transcribe"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("transcriptionTranscribe -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      return response.getData().getString("status");
   } // end of transcriptionNew()
   
   /**
    * Get the current status of the transcription process started using
    * {@link #transcriptionTranscribe()}.
    * @return The status of the transcription.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public String transcriptionStatus() throws IOException, ElpisException {
      HttpRequestGet request = new HttpRequestGet(
         makeUrl("transcription/status"))
         .setHeader("Accept", "application/json");
      if (verbose) System.out.println("transcriptionStatus -> " + request);
      response = new Response(request.get(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
      return response.getData().getString("status");
   } // end of transcriptionStatus()

   /**
    * Get the plain-text version of the last transcript created by
    * {@link #transcriptionTranscribe()}.
    * @return The transcript of the recording.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public String transcriptionText() throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of transcriptionText()

   /**
    * Get the ELAN (.eaf) version of the last transcript created by
    * {@link #transcriptionTranscribe()}, which has a tier that includes an aligned
    * annotation for each word token. 
    * @return A file containing the ELAN (.eaf) transcription of the recording, which
    * should be deleted by the caller when no longer needed.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public File transcriptionElan() throws IOException, ElpisException {
      throw new ElpisException("Not implemented");
   } // end of transcriptionText()

   // config functions

   /**
    * Reset the server - delete all uploads, datasets, pronunciation maps, etc.
    * @throws IOException if a communication error occurs.
    * @throws ElpisException if the server returns an error.
    */
   public void configReset() throws IOException, ElpisException {
      HttpRequestPost request = new HttpRequestPost(
         makeUrl("config/reset"))
         .setHeader("Accept", "dataset/prepare");
      if (verbose) System.out.println("datasetPrepare -> " + request);
      response = new Response(request.post(), verbose);
      response.checkForErrors(); // throws a ElpisException on error
   } // end of configReset()

} // end of class Elpis
