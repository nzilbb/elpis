/**
 * Command line utilities.
 * <p>In particular, the {@link CommandLine} utility is the <q>main class</q> for
 * <i>nzilbb.elpis.jar</i>, so is easy to invoke from the command line for performing
 * ad-hoc API requests: e.g.
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
 */
package nzilbb.elpis.util;
