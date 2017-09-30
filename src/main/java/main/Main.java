package main;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import automata.wa.IBuchiWa;
import complement.wa.BuchiWaComplement;
import operation.difference.wa.BuchiWaDifferenceTarjan;
import operation.difference.wa.BuchiWaDifferenceAntichain;
import operation.difference.wa.BuchiWaDifferenceAscc;
import operation.inclusion.wa.BuchiInclusionASCC;
import operation.inclusion.wa.BuchiInclusionASCCAntichain;
import operation.inclusion.wa.BuchiInclusionComplement;
import operation.inclusion.wa.BuchiInclusionRABIT;
import util.PairXX;

import util.parser.ParserType;
import util.parser.SingleParser;
import util.parser.UtilParser;
import util.parser.ats.ATSFileParser;

public class Main {
	
	private static final String FILE_EXT = "ats";
	private static final long TIME_LIMIT = 20;
	public static void main(String[] args) throws IOException {
		
		if(args.length < 1) {
			printUsage();
			System.exit(0);
		}
		
		System.gc();
		
		long time = TIME_LIMIT;
		boolean test = false;
		boolean complement = false;
		boolean inclusion = false;
		boolean difference = false;
		String fileOut = null;
		for(int i = 0; i < args.length; i ++) {
			if(args[i].equals("-test")) {
				test = true;
			}else if(args[i].equals("-to")) {
				time = Integer.parseInt(args[i + 1]);
				++ i;
			}else if(args[i].equals("-h")) {
				printUsage();
				System.exit(0);
			}else if(args[i].equals("-v")) {
				Options.verbose = true;
			}else if(args[i].equals("-set")) {
				int n = Integer.parseInt(args[i + 1]);
				if(n >= 0 && n <= 4) Options.setChoice = n;
				++ i;
			}else if(args[i].equals("-lazys")) {
				Options.lazyS = true;
			}else if(args[i].equals("-complement")) {
				complement = true;
				fileOut = args[i + 1];
				++ i;	
			}else if(args[i].equals("-lazyb")) {
				Options.lazyB = true;
			}else if(args[i].equals("-diff")) {
				difference = true;
			}else if(args[i].equals("-incl")) {
				inclusion = true;
			}else if(args[i].equals("-gba")) {
				Options.useGBA = true;
			}
		}
		time = time * 1_000; // miliseconds
		if(test) {
			testBenchmarks(time);
		}else if(complement){
			complementBuchi(args, fileOut, time);
		}else if(inclusion){
			checkInclusion(args, time);
		}else if(difference) {
			computeDifference(args, time);
		}
		
		// force to exit
		System.exit(-1);
	}


	private static void printUsage() {
		
		System.out.println("SemiBuchi v1: Library for Semi-deterministic Buchi automata");
		System.out.println("\nUsage:\n     <PROP> [options] <ATS-FILE> \n");
		System.out.println("Recommended use: java -jar SemiBuchi.jar -ascc -ac aut.ats");
		System.out.println("\nOptions:");
		System.out.println("-h: Show this page");
		System.out.println("-v: Verbose mode");
		System.out.println("-set k: 0 for BitSet, 1 for SparseBitSet\n"
				          + "       2 for TInSet, 3 for TreeSet and 4 for HashSet");
		System.out.println("-test: Test all benchmarks");
		System.out.println("-lazys: Delay word distribution to S");
		System.out.println("-lazyb: Delay word distribution to B");
		System.out.println("-tarjan: Use Tarjan algorithm");
		System.out.println("-rabit: Use RABIT tool");
		System.out.println("-ascc: Use ASCC algorithm (Default)");
		System.out.println("-ac: Use Antichain optimization");
		System.out.println("-dfs: Use Double DFS algorithm");
		System.out.println("-diff: Compute Difference of two automata");
		System.out.println("-ncs: Prefer smaller N\\/C\\/S ");
		System.out.println("-incl: Check inclusion of two automata");
		System.out.println("-gba: Use generalized Buchi automata");
		System.out.println("-to k: Limit execution in k seconds (20 secs by default)");
		System.out.println("-complement <file-out>: Output complement of the last automaton");
		
	}
	
	private static void computeDifference(String[] args, long time) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(Options.verbose) System.out.println("Time stamp: " + dateFormat.format(new Date()));
		
		File file = null;
		TaskDifference task = null;
		
		boolean tarjan = false, antichain = false, ascc = false;
		for(int i = 0; i < args.length; i ++) {
			
			if(args[i].endsWith(FILE_EXT)) {
				file = new File(args[i]);
			}if(args[i].equals("-tarjan")) {
				tarjan = true;
			}else if(args[i].equals("-ac")) {
			    antichain = true;
			}else if(args[i].equals("-ncs")) {
                Options.smallerNCS = true;
            }else if(args[i].equals("-ascc")) {
                ascc = true;
            }
		}
		
		assert file != null;
		
		if(Options.verbose) System.out.println("Parsing file " + file.getName() + " ....");
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(file.getAbsolutePath());
		List<PairXX<IBuchiWa>> pairs = atsParser.getBuchiPairs();
				
		for(PairXX<IBuchiWa> pair : pairs) {
			task = new TaskDifference(file.getName());
			if(tarjan) {
				task.setOperation(new BuchiWaDifferenceTarjan(pair.getFstElement(), pair.getSndElement()));
			}else if(antichain) {
			    task.setOperation(new BuchiWaDifferenceAntichain(pair.getFstElement(), pair.getSndElement()));
			}else if(ascc){
			    task.setOperation(new BuchiWaDifferenceAscc(pair.getFstElement(), pair.getSndElement()));
			}else{
	             System.err.println("Other algorithms not support yet");
	             System.exit(-1);
			}
			
            final String opName = "Computing Difference";
            printTaskBegin(opName, task);
			RunTask runTask = new RunTask(task, time);
            runTask.execute();
            printTaskEnd(task);
		}
		
	}
	
	private static void checkInclusion(String[] args, long time) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(Options.verbose) System.out.println("Time stamp: " + dateFormat.format(new Date()));
		
		File file = null;
		TaskInclusion task = null;
		
		boolean tarjan = false, antichain = false, dfs = false, rabit = false;
		for(int i = 0; i < args.length; i ++) {
			
			if(args[i].endsWith(FILE_EXT)) {
				file = new File(args[i]);
			}if(args[i].equals("-tarjan")) {
				tarjan = true;
			}else if(args[i].equals("-ac")) {
				antichain = true;
			}else if(args[i].equals("-dfs")) {
				dfs = true;
			}else if(args[i].equals("-rabit")) {
				rabit = true;
			}
		}
		
		assert file != null;
		
		if(Options.verbose) System.out.println("Parsing file " + file.getName() + " ....");
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(file.getAbsolutePath());
		List<PairXX<IBuchiWa>> pairs = atsParser.getBuchiPairs();
				
		for(PairXX<IBuchiWa> pair : pairs) {
			task = new TaskInclusion(file.getName());
			if(tarjan) {
				task.setOperation(new BuchiInclusionComplement(pair.getFstElement(), pair.getSndElement()));
			}else if(dfs) {
				System.err.println("Not support yet");
				System.exit(-1);
			}else if(rabit){
				task.setOperation(new BuchiInclusionRABIT(pair.getFstElement(), pair.getSndElement()));
			}else {
				if(antichain) {
					task.setOperation(new BuchiInclusionASCCAntichain(pair.getFstElement(), pair.getSndElement()));
				}else {
					task.setOperation(new BuchiInclusionASCC(pair.getFstElement(), pair.getSndElement()));
				}
			}
			
			RunTask runTask = new RunTask(task, time);
	        final String opName = "Checking inclusion";
	        printTaskBegin(opName, task);
	        runTask.execute();
	        printTaskEnd(task);

		}
		
	}
	
	
	private static void complementBuchi(String[] args, String fileOut, long time) {
	    
		File fileIn = null;
		SingleParser parser = null;
		for(int i = 0; i < args.length; i ++) {
			if(args[i].endsWith(FILE_EXT)) {
				fileIn = new File(args[i]);
				parser = UtilParser.getSinleParser(ParserType.ATS);
			}
			if(args[i].endsWith(".ba")) {
				fileIn = new File(args[i]);
				parser = UtilParser.getSinleParser(ParserType.BA);
			}
		}
		assert parser != null;
		
		parser.parse(fileIn.getAbsolutePath());
		IBuchiWa buchi = parser.getBuchi();
//		buchi.makeComplete();
		BuchiWaComplement buchiComplement = new BuchiWaComplement(buchi);

		TaskComplement task = new TaskComplement(fileIn.getName());
		task.setOperation(buchiComplement);
		RunTask runTask = new RunTask(task, time);
		final String opName = "Computing complement";
		printTaskBegin(opName, task);
		runTask.execute();
		printTaskEnd(task);

		if(fileOut == null || !(task.getResultValue().isNormal())) return;
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fileOut));
			buchiComplement.toBA(out, parser.getAlphabet());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
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
				System.out.println(f.getAbsolutePath());

				if(f.getName().endsWith(FILE_EXT)) {
					ATSFileParser atsParser =  new ATSFileParser();
					atsParser.parse(f.getAbsolutePath());
					List<PairXX<IBuchiWa>> pairs = atsParser.getBuchiPairs();
					
					for(PairXX<IBuchiWa> pair : pairs) {
						List<TaskInclusion> tasks = new ArrayList<TaskInclusion>();

						TaskInclusion taskNscb = new TaskInclusion(f.getName());
						taskNscb.setOperation(new BuchiInclusionComplement(pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskNscb);
						
						TaskInclusion taskAsccAnti = new TaskInclusion(f.getName());
						taskAsccAnti.setOperation(new BuchiInclusionASCCAntichain(pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskAsccAnti);
						
						
						TaskInclusion taskAscc = new TaskInclusion(f.getName());
						taskAscc.setOperation(new BuchiInclusionASCC(pair.getFstElement(), pair.getSndElement()));
						tasks.add(taskAscc);

						TaskInclusion taskRabit = new TaskInclusion(f.getName());
						taskRabit.setOperation(new BuchiInclusionRABIT(pair.getFstElement(), pair.getSndElement()));
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
							System.out.println("result: " + task.getResultValue() + ", " + task.getRuntime());
							generator.addRows(task);
						}
						
					}
					
				}
				
			}
			
		}
		generator.end();
		System.gc();
		
	}
	
	
	private static void printTaskBegin(String op, ITask task) {
        if(Options.verbose)  System.out.println(op + " by ALGORITHM " + task.getOperationName() + " ...");
	}
	
	private static void printTaskEnd(ITask task) {
        if (Options.verbose) {
            if (task.getResultValue().isNormal()) {
                System.out.println("Task completed by ALGORITHM " + task.getOperationName() + " ...");
            } else {
                System.out.println("Task failed by ALGORITHM " + task.getOperationName() + " ...");
            }
            System.out.println("\n" + task.toStringVerbose());
        }else {
            System.out.println(task.toString());
        }
	}

}
