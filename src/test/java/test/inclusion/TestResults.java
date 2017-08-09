package test.inclusion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import automata.IBuchi;
import util.Timer;

public class TestResults {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String dir = "/home/liyong/workspace-neon/SemiBuchi/target/";
		List<String> nonIncluded = new ArrayList<>();

		int numCases = 0;
		int numOK = 0;
		for(int i = 1; i <= 376; i ++) {
			File f1 = new File(dir + "easy" + i + ".ba");
			if(! f1.exists()) continue; 
			File f2 = new File(dir + "easy" + i + "-opt.ba");
			if(! f2.exists()) continue; 
			numCases ++;
			
			final Runtime rt = Runtime.getRuntime();
			String command = "java";
			command += " -Xms" + 3 + "g -Xms" + 3 + "G";
			command += " -Xmx" + 3 + "g -Xmx" + 3 + "G";
			command += " -jar";
			command += " " + "/home/liyong/workspace-neon/SemiBuchi/target/rabit.jar";
			command += " " + f1.getAbsolutePath() + " " + f2.getAbsolutePath();
			command += " -fast";
			final Process proc = rt.exec(command);
			System.out.println(command);
			
//			proc.wait();
			while(true) {
				if(! proc.isAlive()) {
//					System.out.println("Rabit exit normally");
					break;
				}
			}
			
			final BufferedReader reader = new  BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;

			while((line = reader.readLine()) != null) {
				if(line.contains("Included")) {
					numOK ++;
					System.out.println("case " + i + " OK");
				}else if(line.contains("Not included")) {
					System.out.println("case " + i + " not OK");
					nonIncluded.add("" + i);
				}
//				System.out.println(line);
			}

		}
		System.out.println(numOK + " out of " + numCases + " are OK");
		for(String name : nonIncluded) {
			System.out.println(name);
		}
	}

}
