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

import util.PairXX;
import util.parser.ATSFileParser;

public class Main {
	
	private static final String FILE_EXT = "ats";
	
	public static void main(String[] args) throws IOException {
		
		File fileDir = new File("src/main/resources/benchmarks/bug/");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
		long time = 40*1000;
		generator.start();
		int numFile = 0;
		if( fileDir.listFiles() == null) return ;
		for(File f : fileDir.listFiles()) {
			System.out.println(f.getName());
			if(! f.getName().equals("bist_cell_true-unreach-call_false-termination.cil.c_Iteration17.ats")) continue;
			if(f.getName().endsWith(FILE_EXT)) {
				numFile ++;
				ATSFileParser atsParser =  new ATSFileParser();
				atsParser.parse(f.getAbsolutePath());
				List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
				
				for(PairXX<IBuchi> pair : pairs) {
					List<TaskInfo> tasks = new ArrayList<TaskInfo>();
					TaskInfo taskRabit = new TaskInfo(f.getName(), time);
					taskRabit.setOperation(new BuchiInclusionRABIT(taskRabit, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskRabit);
					
					TaskInfo taskAscc = new TaskInfo(f.getName(), time);
					taskAscc.setOperation(new BuchiInclusionASCC(taskAscc, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskAscc);
					
					TaskInfo taskAsccAnti = new TaskInfo(f.getName(), time);
					taskAsccAnti.setOperation(new BuchiInclusionASCCAntichain(taskAsccAnti, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskAsccAnti);

					TaskInfo taskNscb = new TaskInfo(f.getName(), time);
					taskNscb.setOperation(new BuchiInclusionComplement(taskNscb, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskNscb);

					
					for(TaskInfo task : tasks) {
						System.out.println("\n\n" + task.getOperation().getName() + " is running....");
						try {
							task.runTask();
						}catch(Exception e) {
							e.printStackTrace();
						}catch(OutOfMemoryError e)	{
						    e.printStackTrace();
						}finally {
//							generator.end();
						}
						System.out.println("result: " + task.getResult());
						generator.addRows(task);
					}
					
				}
				
			}
			
		}
		generator.end();
		
		
		
	}
	

}
