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
		
		File fileDir = new File("src/main/resources/benchmarks/normal/");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
		long time = 10*1_000;
		generator.start();
		int numFile = 0;
		if( fileDir.listFiles() == null) return ;
		for(File f : fileDir.listFiles()) {
			System.out.println(f.getName());
			if(! f.getName().equals("bist_cell_true-unreach-call_false-termination.cil.c_Iteration21.ats")) continue;
			if(f.getName().endsWith(FILE_EXT)) {
				numFile ++;
				ATSFileParser atsParser =  new ATSFileParser();
				atsParser.parse(f.getAbsolutePath());
				List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
				
				for(PairXX<IBuchi> pair : pairs) {
					List<TaskInclusion> tasks = new ArrayList<TaskInclusion>();
					TaskInclusion taskRabit = new TaskInclusion(f.getName(), time);
					taskRabit.setOperation(new BuchiInclusionRABIT(taskRabit, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskRabit);
					
					TaskInclusion taskAscc = new TaskInclusion(f.getName(), time);
					taskAscc.setOperation(new BuchiInclusionASCC(taskAscc, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskAscc);
					
					TaskInclusion taskAsccAnti = new TaskInclusion(f.getName(), time);
					taskAsccAnti.setOperation(new BuchiInclusionASCCAntichain(taskAsccAnti, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskAsccAnti);

					TaskInclusion taskNscb = new TaskInclusion(f.getName(), time);
					taskNscb.setOperation(new BuchiInclusionComplement(taskNscb, pair.getFstElement(), pair.getSndElement()));
					tasks.add(taskNscb);

					
					for(TaskInclusion task : tasks) {
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
