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
//		String file = "/home/liyong/projects/liyong/git-repo/ultimate/trunk/"
//				+ "examples/automata-benchmarks/20170611-TerminationAnalysis-BuchiIsIncluded/"
//				+ "4BitCounterPointer_true-termination_true-valid-memsafety.c_Iteration3.ats";
//		//4BitCounterPointer_true-termination_true-valid-memsafety.c_Iteration3.ats
//		//LarrazOliverasRodriguez-CarbonellRubio-FMCAD2013-Fig1_true-termination_true-no-overflow.c_Iteration3.ats
//		ATSFileParser parser =  new ATSFileParser();
//		parser.parse(file);
//		List<PairXX<IBuchi>> buchiPairs = parser.getBuchiPairs();
//		System.out.println(parser.getAlphabet().toString());
//		for(PairXX<IBuchi> pair : buchiPairs) {
//			IBuchi fst = pair.getFstElement();
//			IBuchi snd = pair.getSndElement();
//			System.out.println("pair --------------");
//			System.out.println("LHS_STATES: " + fst.getStateSize() + " \n\n");
////			System.out.println(fst.toDot() + " \n\n");
////			System.out.println(fst.toBA() + " \n");
//			System.out.println("is semideterministic: "+ fst.isSemiDeterministic() + "\n\n");
////			System.out.println(snd.toDot() + " \n\n");
////			System.out.println(snd.toBA() + " \n");
//			System.out.println("RHS_STATES: " + snd.getStateSize() + " \n\n");
//			System.out.println("is semideterministic: "+ snd.isSemiDeterministic() + "\n\n");
//			try {
//				System.out.println("IsIncluded: " + RunRabit.executeRabit(fst, snd, 10*1000));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			IBuchiInclusion checker = new BuchiInclusionComplement(fst, snd);
//			System.out.println("IsIncluded(complement): " + checker.isIncluded());
//			
//			BuchiUniversalityNestedDFS uni = new BuchiUniversalityNestedDFS(fst);
//			System.out.println("fst universality: " + uni.isUniversal());
//			
//			BuchiUniversalityNestedDFSAntichain uniAnti = new BuchiUniversalityNestedDFSAntichain(fst);
//			System.out.println("fst-Anti universality: " + uniAnti.isUniversal());
//			
//			BuchiUniversalityTarjan uniTr = new BuchiUniversalityTarjan(fst);
//			System.out.println("fst universality-tr: " + uniTr.isUniversal());
//			
//			BuchiUniversalityTarjanOriginal uniTrS = new BuchiUniversalityTarjanOriginal(fst);
//			System.out.println("fst universality-trs: " + uniTrS.isUniversal());
//		}
////		
//		IBuchi buchi = new BuchiGeneral(2);
//		IState a = buchi.addState();
//		IState b = buchi.addState();
//		
//		a.addSuccessor(0, a.getId());
//		a.addSuccessor(1, b.getId());
//		
//		b.addSuccessor(0, a.getId());
//		b.addSuccessor(1, b.getId());
//		
//		buchi.setFinal(0);
//		buchi.setFinal(1);
//		
//		buchi.setInitial(0);
//		
//		BuchiUniversalityNestedDFS uni = new BuchiUniversalityNestedDFS(buchi);
//		System.out.println("DFS universality: " + uni.isUniversal());
//		
//		BuchiUniversalityNestedDFSAntichain uniAnti = new BuchiUniversalityNestedDFSAntichain(buchi);
//		System.out.println("DFS-Anti universality: " + uniAnti.isUniversal());
//		
//		BuchiUniversalityTarjan uniTr = new BuchiUniversalityTarjan(buchi);
//		System.out.println("FST universality-tr: " + uniTr.isUniversal());
//		
//		BuchiUniversalityTarjanOriginal uniTrS = new BuchiUniversalityTarjanOriginal(buchi);
//		System.out.println("fst universality-trs: " + uniTrS.isUniversal());
//		
//		System.out.println("complement: " + uniTr.getBuchiComplement().toDot());
		
		
		String dir = "/home/liyong/projects/liyong/git-repo/ultimate/trunk/examples/automata-benchmarks/20170611-TerminationAnalysis-BuchiIsIncluded/";
		if(args.length <= 0) {
			System.out.println("<program> <directory-with-ATS-files>");
			System.exit(-1);
		}
		
		File fileDir = new File(args[0]);
		
//		Class<IBuchiInclusion>[] clazzes = {BuchiInclusionRABIT.class,BuchiInclusionComplement.class
//				, BuchiInclusionASCC.class, BuchiInclusionASCCAntichain.class};
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		CSVGenerator generator = new CSVGenerator("./result-" + dateFormat.format(new Date()) + ".csv");
		long time = 40*1000;
		generator.start();
		int numFile = 0;
		for(File f : fileDir.listFiles()) {
			System.out.println(f.getName());
			
			if(f.getName().endsWith(FILE_EXT)) {
				if(!f.getName().equals("bist_cell_true-unreach-call_false-termination.cil.c_Iteration17.ats")) continue;
				if(numFile > 2) break;
				numFile ++;
				ATSFileParser atsParser =  new ATSFileParser();
				atsParser.parse(f.getAbsolutePath());
				List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
				int num = 0;
				
				for(PairXX<IBuchi> pair : pairs) {
					List<TaskInfo> tasks = new ArrayList<TaskInfo>();
					tasks.add(new TaskInfo(
							new BuchiInclusionRABIT(pair.getFstElement(), pair.getSndElement())
						, f.getAbsolutePath()
						, time));
					
					
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
					
					num ++;
					
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
