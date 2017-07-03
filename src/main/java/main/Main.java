package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import automata.BuchiGeneral;
import automata.IBuchi;
import automata.IState;
import operation.inclusion.BuchiInclusionASCC;
import operation.inclusion.BuchiInclusionASCCAntichain;
import operation.inclusion.BuchiInclusionComplement;
import operation.inclusion.BuchiInclusionRABIT;
import operation.inclusion.IBuchiInclusion;
import operation.universality.BuchiUniversalityNestedDFS;
import operation.universality.BuchiUniversalityNestedDFSAntichain;
import operation.universality.BuchiUniversalityTarjan;
import operation.universality.BuchiUniversalityTarjanOriginal;
import util.PairXX;
import util.parser.ATSFileParser;

public class Main {
	
	private static final String FILE_EXT = "ats";
	
	public static void main(String[] args) throws IOException {
		
		File fileDir = new File("src/main/resources/benchmarks");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
		long time = 20*1000;
		generator.start();
		int numFile = 0;
		for(File f : fileDir.listFiles()) {
			System.out.println(f.getName());
			
			if(f.getName().endsWith(FILE_EXT)) {
//				if(!f.getName().endsWith("token_ring.08_true-unreach-call_false-termination.cil.c_Iteration33.ats")) continue;
//				if(numFile > 20) break;
				numFile ++;
				ATSFileParser atsParser =  new ATSFileParser();
				atsParser.parse(f.getAbsolutePath());
				List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
				
				for(PairXX<IBuchi> pair : pairs) {
					List<TaskInfo> tasks = new ArrayList<TaskInfo>();
//					tasks.add(new TaskInfo(
//							new BuchiInclusionRABIT(pair.getFstElement(), pair.getSndElement())
//						, f.getAbsolutePath()
//						, time));
					
					
					tasks.add(new TaskInfo(
							new BuchiInclusionASCC(pair.getFstElement(), pair.getSndElement())
						, f.getAbsolutePath()
						, time));
					
					tasks.add(new TaskInfo(
							new BuchiInclusionASCCAntichain(pair.getFstElement(), pair.getSndElement())
						, f.getAbsolutePath()
						, time));
					
					tasks.add(new TaskInfo(
							new BuchiInclusionComplement(pair.getFstElement(), pair.getSndElement())
						, f.getAbsolutePath()
						, time));
					
					for(TaskInfo task : tasks) {
						System.out.println("\n\n" + task.getOperation().getName() + " is running....");
						try {
							task.runTask();
						}catch(Exception e) {
							e.printStackTrace();
						}finally {
//							generator.end();
						}
						generator.addRows(task);
					}
					
				}
				
			}
			
		}
		generator.end();
		
		
		
	}
	

}
