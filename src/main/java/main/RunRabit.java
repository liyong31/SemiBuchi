package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import automata.IBuchi;
import automata.wa.IBuchiWa;
import util.Timer;

public class RunRabit {

	/**
	 * Specifying the directory for RABIT executable, should later put in resource 
	 */
	public static final File ENVIRONMENT =
			new File("classes/rabit");
	/**
	 * The maximal heap size in gigabyte to use for the Rabit tool.
	 */
	public static final int MAX_HEAP_SIZE_GB = 3;
	/**
	 * The minimal heap size in gigabyte to use for the Rabit tool.
	 */
	public static final int MIN_HEAP_SIZE_GB = 3;
	/**
	 * Name of the tool to use.
	 */
	public static final String TOOL = ENVIRONMENT.getAbsolutePath() + "/rabit.jar";
	
	public static final String NOT_INCLUDED = "Not included";
	
	public static final String INCLUDED = "Included";
	
	public static final String A_FILE = ENVIRONMENT.getAbsolutePath() + "/A.ba";

	public static final String B_FILE = ENVIRONMENT.getAbsolutePath() + "/B.ba";

	private RunRabit() {
	}


	/**
	 * Executes RABIT within time limit
	 */
	public static Boolean executeRabit(long timeLimit) throws Exception {
		
		final Runtime rt = Runtime.getRuntime();
		String command = "java";
		command += " -Xms" + MIN_HEAP_SIZE_GB + "g -Xms" + MIN_HEAP_SIZE_GB + "G";
		command += " -Xmx" + MAX_HEAP_SIZE_GB + "g -Xmx" + MAX_HEAP_SIZE_GB + "G";
		command += " -jar";
		command += " " + TOOL;
		command += " " + A_FILE + " " + B_FILE;
		command += " -fast";
		final Process proc = rt.exec(command);
//		System.out.println(command);
		
//		proc.waitFor();
//		System.out.println("while loop");
		Timer timer = new Timer();
		timer.start();
		while(true) {
			if(! proc.isAlive()) {
//				System.out.println("Rabit exit normally");
				break;
			}
			if(timer.tick() > timeLimit) {
//				System.err.println("Rabit Time out exception");
				proc.destroyForcibly();
				return null;
			}
		}
		
		final BufferedReader reader = new  BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = null;
		Boolean result = null;
		while((line = reader.readLine()) != null) {
			if(line.contains(NOT_INCLUDED)) {
				result = false;
			}else if(line.contains(INCLUDED)) {
				result = true;
			}
//			System.out.println(line);
		}

		return result;

	}
	/**
	 * Execute RABIT in command line 
	 * */
	public static Boolean executeRabit(
			final IBuchiWa fstOp
		  , final IBuchiWa sndOp, long timeLimit) throws Exception {
		
		File inFile = new File(A_FILE);
		FileWriter writer = new FileWriter(inFile);
	    writer.write(fstOp.toBA());
        writer.close();
        
        inFile = new File(B_FILE);
        writer = new FileWriter(inFile);
	    writer.write(sndOp.toBA());
        writer.close();
        
	    // file
		return executeRabit(timeLimit);
	}

}
