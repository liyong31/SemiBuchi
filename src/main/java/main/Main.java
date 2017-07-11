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
import util.Timer;
import util.parser.ATSFileParser;

public class Main {
	
	private static final String FILE_EXT = "ats";
	
	public static void main(String[] args) throws IOException {
		
		checkInclusion(args);
//		return ;
//		File fileDir = new File("src/main/resources/benchmarks/normal/");
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
//		long time = 10*1_000;
//		generator.start();
//		int numFile = 0;
//		if( fileDir.listFiles() == null) return ;
//		for(File f : fileDir.listFiles()) {
//			System.out.println(f.getName());
//			if(! f.getName().equals("bist_cell_true-unreach-call_false-termination.cil.c_Iteration21.ats")) continue;
//			if(f.getName().endsWith(FILE_EXT)) {
//				numFile ++;
//				ATSFileParser atsParser =  new ATSFileParser();
//				atsParser.parse(f.getAbsolutePath());
//				List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
//				
//				for(PairXX<IBuchi> pair : pairs) {
//					List<TaskInclusion> tasks = new ArrayList<TaskInclusion>();
//
//					
//
//					TaskInclusion taskNscb = new TaskInclusion(f.getName(), time);
//					taskNscb.setOperation(new BuchiInclusionComplement(taskNscb, pair.getFstElement(), pair.getSndElement()));
//					tasks.add(taskNscb);
//					
//					TaskInclusion taskAsccAnti = new TaskInclusion(f.getName(), time);
//					taskAsccAnti.setOperation(new BuchiInclusionASCCAntichain(taskAsccAnti, pair.getFstElement(), pair.getSndElement()));
//					tasks.add(taskAsccAnti);
//					
//					
//					TaskInclusion taskAscc = new TaskInclusion(f.getName(), time);
//					taskAscc.setOperation(new BuchiInclusionASCC(taskAscc, pair.getFstElement(), pair.getSndElement()));
//					tasks.add(taskAscc);
//
//					TaskInclusion taskRabit = new TaskInclusion(f.getName(), time);
//					taskRabit.setOperation(new BuchiInclusionRABIT(taskRabit, pair.getFstElement(), pair.getSndElement()));
//					tasks.add(taskRabit);
//					
//					for(TaskInclusion task : tasks) {
//						System.out.println("\n\n" + task.getOperation().getName() + " is running....");
//						try {
//							System.gc();
//							Thread.sleep(2000);
//							task.runTask();
//						}catch(Exception e) {
//							e.printStackTrace();
//						}catch(OutOfMemoryError e)	{
//						    e.printStackTrace();
//						}finally {
////							generator.end();
//						}
//						System.out.println("result: " + task.getResult() + ", " + task.getRuntime());
//						generator.addRows(task);
//					}
//					
//				}
//				
//			}
//			
//		}
//		generator.end();
//		System.gc();
		
	}
	
	
	private static void printUsage() {
		
		System.out.println("SemiBuchi v1: Library for Semi-deterministic Buchi automata");
		System.out.println("\nUsage:\n     <PROP> [options] <ATS-FILE> \n");
		System.out.println("Recommended use: java -jar SemiBuchi.jar -ascc -ac aut.ats");
		System.out.println("\nOptions:");
		System.out.println("-h: Show this page");
		System.out.println("-v: Verbose mode");
		System.out.println("-tarjan: Use Tarjan algorithm");
		System.out.println("-ascc: Use ASCC algorithm (Default)");
		System.out.println("-ac: Use Antichain optimization");
		System.out.println("-dfs: Use Double DFS algorithm");
		System.out.println("-to k: Check inclusion in k seconds (20 secs by default)");
		
	}
	
	private static void checkInclusion(String[] args) {
		
		if(args.length < 2) {
			printUsage();
			System.exit(0);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("Time stamp: " + dateFormat.format(new Date()));
		
		File file = null;
		TaskInclusion task = null;
		int timeSec = 20;             // default time 20 seconds
		
		boolean tarjan = false, antichain = false, dfs = false;
		for(int i = 0; i < args.length; i ++) {
			
			if(args[i].equals("-h")) {
				printUsage();
				System.exit(0);
			}else if(args[i].endsWith(FILE_EXT)) {
				file = new File(args[i]);
			}else if(args[i].equals("-to")) {
				timeSec = Integer.parseInt(args[i + 1]);
				++ i;
			}else if(args[i].equals("-tarjan")) {
				tarjan = true;
			}else if(args[i].equals("-ac")) {
				antichain = true;
			}else if(args[i].equals("-dfs")) {
				dfs = true;
			}
		}
		
		assert file != null;
		
		System.out.println("Parsing file " + file.getName() + " ....");
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(file.getAbsolutePath());
		List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
		
		timeSec = timeSec * 1_000; // miliseconds
		
		for(PairXX<IBuchi> pair : pairs) {
			task = new TaskInclusion(file.getName(), timeSec);
			if(tarjan) {
				task.setOperation(new BuchiInclusionComplement(task, pair.getFstElement(), pair.getSndElement()));
			}else if(dfs) {
				System.err.println("Not support yet");
				System.exit(-1);
			}else {
				if(antichain) {
					task.setOperation(new BuchiInclusionASCCAntichain(task, pair.getFstElement(), pair.getSndElement()));
				}else {
					task.setOperation(new BuchiInclusionASCC(task, pair.getFstElement(), pair.getSndElement()));
				}
			}
			System.out.println("Checking inclusion by ALGORITHM " + task.getOperation().getName() + " ...");
			Timer timer = new Timer();
			timer.start();
			task.runTask();
			timer.stop();
			System.out.println("Task completed by ALGORITHM " + task.getOperation().getName() + " ...");
			System.out.println("Included = " + task.getResult()
			                + " TotalTime = " + timer.getTimeElapsed() + " (ms)"
			                + " ChecingTime = " + task.getRuntime() + " (ms)");

		}
		
	}
	

}
