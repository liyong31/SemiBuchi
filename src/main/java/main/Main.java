package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import automata.IBuchi;
import operation.inclusion.BuchiInclusionASCC;
import operation.inclusion.BuchiInclusionASCCAntichain;
import operation.inclusion.BuchiInclusionComplement;
import operation.inclusion.BuchiInclusionRABIT;

import util.PairXX;
import util.Timer;
import util.parser.ATSFileParser;

public class Main {
	
	private static final String FILE_EXT = "ats";
	private static final long TIME_LIMIT = 20;
	
	public static void main(String[] args) throws IOException {
		
		if(args.length < 1) {
			printUsage();
			System.exit(0);
		}
		
		long time = TIME_LIMIT;
		boolean test = false;
		for(int i = 0; i < args.length; i ++) {
			if(args[i].equals("-test")) {
				test = true;
			}else if(args[i].equals("-to")) {
				time = Integer.parseInt(args[i + 1]);
				++ i;
			}else if(args[i].equals("-h")) {
				printUsage();
				System.exit(0);
			} 
		}
		time = time * 1_000; // miliseconds
		if(test) {
			testBenchmarks(time);
		}else {
			checkInclusion(args, time);
		}
		
	}
	
	
	private static void printUsage() {
		
		System.out.println("SemiBuchi v1: Library for Semi-deterministic Buchi automata");
		System.out.println("\nUsage:\n     <PROP> [options] <ATS-FILE> \n");
		System.out.println("Recommended use: java -jar SemiBuchi.jar -ascc -ac aut.ats");
		System.out.println("\nOptions:");
		System.out.println("-h: Show this page");
		System.out.println("-v: Verbose mode");
		System.out.println("-test: Test all benchmarks");
		System.out.println("-tarjan: Use Tarjan algorithm");
		System.out.println("-ascc: Use ASCC algorithm (Default)");
		System.out.println("-ac: Use Antichain optimization");
		System.out.println("-dfs: Use Double DFS algorithm");
		System.out.println("-to k: Check inclusion in k seconds (20 secs by default)");
		
	}
	
	private static void checkInclusion(String[] args, long time) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("Time stamp: " + dateFormat.format(new Date()));
		
		File file = null;
		TaskInclusion task = null;
		
		boolean tarjan = false, antichain = false, dfs = false;
		for(int i = 0; i < args.length; i ++) {
			
			if(args[i].endsWith(FILE_EXT)) {
				file = new File(args[i]);
			}if(args[i].equals("-tarjan")) {
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
				
		for(PairXX<IBuchi> pair : pairs) {
			task = new TaskInclusion(file.getName(), time);
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
			                + " Result_States = " + task.getOperation().getBuchiDifference().getStateSize()
			                + " TotalTime = " + timer.getTimeElapsed() + " (ms)"
			                + " CheckingTime = " + task.getRuntime() + " (ms)");

		}
		
	}
	
	private static void testBenchmarks(long time) throws IOException {
		
		// please be careful also for RABIT directory
		String[] dirs = {
//				Main.class.getClassLoader().getResource("classes/benchmarks/easy").getPath()
//				, Main.class.getClassLoader().getResource("benchmarks/normal").getPath()
//				, Main.class.getClassLoader().getResource("benchmarks/difficult").getPath() 
				"classes/benchmarks/easy"
			  , "classes/benchmarks/normal"
			  , "classes/benchmarks/difficult"
				};
		
		System.out.println("Please donot change your current path...");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
		generator.start();
		
		for(String dir : dirs) {
			System.out.println(dir);
			File fileDir = new File(dir);
			if( fileDir.listFiles() == null) return ;
			
			for(File f : fileDir.listFiles()) {
				System.out.println(f.getName());

				if(f.getName().endsWith(FILE_EXT)) {
					ATSFileParser atsParser =  new ATSFileParser();
					atsParser.parse(f.getAbsolutePath());
					List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
					
					for(PairXX<IBuchi> pair : pairs) {
						List<TaskInclusion> tasks = new ArrayList<TaskInclusion>();

						TaskInclusion taskNscb = new TaskInclusion(f.getName(), time);
						taskNscb.setOperation(new BuchiInclusionComplement(taskNscb, pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskNscb);
						
						TaskInclusion taskAsccAnti = new TaskInclusion(f.getName(), time);
						taskAsccAnti.setOperation(new BuchiInclusionASCCAntichain(taskAsccAnti, pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskAsccAnti);
						
						
						TaskInclusion taskAscc = new TaskInclusion(f.getName(), time);
						taskAscc.setOperation(new BuchiInclusionASCC(taskAscc, pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskAscc);

						TaskInclusion taskRabit = new TaskInclusion(f.getName(), time);
						taskRabit.setOperation(new BuchiInclusionRABIT(taskRabit, pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskRabit);
						
						for(TaskInclusion task : tasks) {
							System.out.println("\n\n" + task.getOperation().getName() + " is running....");
							try {
//								System.gc();
//								Thread.sleep(2000);
								task.runTask();
							}catch(Exception e) {
								e.printStackTrace();
							}catch(OutOfMemoryError e)	{
							    e.printStackTrace();
							}finally {
//								generator.end();
							}
							System.out.println("result: " + task.getResult() + ", " + task.getRuntime());
							generator.addRows(task);
						}
						
					}
					
				}
				
			}
			
		}
		generator.end();
		System.gc();
		
	}
	

}
