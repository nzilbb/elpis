//
// Copyright 2020 New Zealand Institute of Language, Brain and Behaviour, 
// University of Canterbury
// Written by Robert Fromont - robert.fromont@canterbury.ac.nz
//
package nzilbb.elpis.test;
	      
import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import nzilbb.elpis.*;

/**
 * Unit tests for Elpis client.
 * <p>These tests are general in nature. They assume the existence of a valid, running Elpis
 * instance (configured by elpisUrl). 
 * <p> <em> NB: </em> The unit test calles configReset during it's executing, which will
 * delete all pre-existing datasets and models.
 */
public class TestElpis {
   // YOU MUST ENSURE THE FOLLOWING SETTINGS ARE VALID FOR YOU TEST LABBCAT SERVER:
   static String elpisUrl = "http://0.0.0.0:5000/";
   static Elpis elpis;

   @BeforeClass public static void createStore() {
      try {
         elpis = new Elpis(elpisUrl);
      } catch(MalformedURLException exception) {
         fail("Could not create Elpis object: " + exception);
      }
      try {
         elpis.configReset();
      } catch(Exception exception) {}
   }

   @After public void notVerbose() {
      elpis.setVerbose(false);
   }
      
   @Test(expected = MalformedURLException.class) public void malformedURLException()
      throws Exception {
      new Elpis("xxx");
   }

   @Test public void pipeline() throws Exception {

      // create dataset
      
      elpis.datasetNew("unit-test-ds");

      List<String> list = elpis.datasetList();
      Set<String> set = list.stream().collect(Collectors.toSet());
      //for (String item : list) System.out.println(item);
      assertTrue("dataset exists", set.contains("unit-test-ds"));

      elpis.datasetLoad("unit-test-ds");      

      elpis.datasetSettings("test");      

      // upload transcribed files
      
      Vector<File> dataset = new Vector<File>();
      System.out.println(getDir().getPath());
      dataset.add(new File(getDir(), "transcribed.eaf"));
      dataset.add(new File(getDir(), "transcribed.wav"));

      list = elpis.datasetFiles(dataset);
      set = list.stream().collect(Collectors.toSet());
      assertTrue("eaf uploaded", set.contains("transcribed.eaf"));
      assertTrue("wav uploaded", set.contains("transcribed.wav"));
      assertEquals("no other files present", 2, list.size());

      elpis.datasetSettings("test");

      // pronunciation dictionary

      Map<String,Integer> frequencies = elpis.datasetPrepare();
      assertEquals("frequency of 'one' correct",
                   Integer.valueOf(3), frequencies.get("one"));
      assertEquals("correct vocabulary size",
                   184, frequencies.size());

      elpis.pronDictNew("unit-test-pd", "unit-test-ds");

      list = elpis.pronDictList();
      set = list.stream().collect(Collectors.toSet());
      //for (String item : list) System.out.println(item);
      assertTrue("pron dict exists", set.contains("unit-test-pd"));

      elpis.pronDictLoad("unit-test-pd");

      File l2s = new File(getDir(), "l2s.txt");
      elpis.pronDictL2S(l2s);
      Map<String,String> lexicon = elpis.pronDictGenerateLexicon();
      assertEquals("pronunciation of 'one' what we'd expect",
                   "ɒ n ɛ", lexicon.get("one"));

      lexicon.put("one", "w ʌ n");
      elpis.pronDictSaveLexicon(lexicon);

      // model training

      elpis.modelNew("unit-test-m", "unit-test-pd");

      list = elpis.modelList();
      set = list.stream().collect(Collectors.toSet());
      // for (String item : list) System.out.println(item);
      assertTrue("model exists", set.contains("unit-test-m"));

      elpis.modelLoad("unit-test-m");

      elpis.modelSettings(2);

      String status = elpis.modelTrain();
      int maxSeconds = 60;
      System.out.print("Training");
      while(status.equals("training") && --maxSeconds > 0) {
         System.out.print(".");
         Thread.sleep(1000);
         status = elpis.modelStatus();
      }
      System.out.println();
      assertEquals("Final status is 'trained'",
                   "trained", status);

      Map<String,String> results = elpis.modelResults();
      assertTrue("results have count_val: "+results,
                 results.containsKey("count_val"));
      assertTrue("results have wer: "+results,
                 results.containsKey("count_val"));
      assertTrue("results have ins_val: "+results,
                 results.containsKey("ins_val"));
      assertTrue("results have sub_val: "+results,
                 results.containsKey("sub_val"));
      assertTrue("results have del_val: "+results,
                 results.containsKey("del_val"));

      // transcription

      // we're not testing that the ELPIS server does a good job, just that this client
      // works with the API, so senf the same recording that we trained on

      File toTranscribe = new File(getDir(), "transcribed.wav");

      elpis.transcriptionNew(toTranscribe);

      status = elpis.transcriptionTranscribe();
      maxSeconds = 60;
      System.out.print("Transcribing");
      // call transcriptionStatus immediately, to ensure we call it at least once.
      status = elpis.transcriptionStatus();
      while(status.equals("transcribing") && --maxSeconds > 0) {
         System.out.print(".");
         Thread.sleep(1000);
         status = elpis.transcriptionStatus();
      }
      System.out.println();
      assertEquals("Final status is 'transcribed'",
                   "transcribed", status);

      String transcript = elpis.transcriptionText();
      System.out.println(transcript);
      
      File eaf = elpis.transcriptionElan();
      System.out.println(eaf.getPath());
      eaf.delete();

      // tidy up
      
      elpis.configReset();

      list = elpis.datasetList();
      assertFalse("reset deletes dataset",
                  list.stream().collect(Collectors.toSet()).contains("unit-test-ds"));
      list = elpis.pronDictList();
      assertFalse("reset deletes pron dict",
                  list.stream().collect(Collectors.toSet()).contains("unit-test-pd"));
      list = elpis.modelList();
      assertFalse("reset deletes model",
                  list.stream().collect(Collectors.toSet()).contains("unit-test-m"));
   }

   /**
    * Directory for text files.
    * @see #getDir()
    * @see #setDir(File)
    */
   protected File fDir;
   /**
    * Getter for {@link #fDir}: Directory for text files.
    * @return Directory for text files.
    */
   public File getDir() { 
      if (fDir == null) {
	 try {
	    URL urlThisClass = getClass().getResource(getClass().getSimpleName() + ".class");
	    File fThisClass = new File(urlThisClass.toURI());
	    fDir = fThisClass.getParentFile();
	 } catch(Throwable t) {
	    System.out.println("" + t);
	 }
      }
      return fDir; 
   }
   /**
    * Setter for {@link #fDir}: Directory for text files.
    * @param fNewDir Directory for text files.
    */
   public void setDir(File fNewDir) { fDir = fNewDir; }

   public static void main(String args[]) {
      org.junit.runner.JUnitCore.main("nzilbb.elpis.test.TestElpis");
   }
}
