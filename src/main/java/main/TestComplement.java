package main;

import java.util.List;

import automata.IBuchi;
import complement.BuchiComplementSDBA;
import util.PairXX;
import util.parser.ATSFileParser;

public class TestComplement {
	
	public static void main(String [] args) {
		
		if(args.length < 1) {
			printUsage();
			System.exit(0);
		}
		String fileIn = args[0];
		for(int i = 0; i < args.length; i ++) {
			if(args[i].equals("-h")) {
				printUsage();
				System.exit(0);
			}else if(args[i].equals("-v")) {
				Options.verbose = true;
			}else if(args[i].equals("-set")) {
				int n = Integer.parseInt(args[i + 1]);
				if(n >= 0 && n <= 4) Options.setChoice = n;
				++ i;
			}else if(args[i].equals("-opt")) {
				Options.optNCSB = true;
			}else if(args[i].contains(".ats")) {
				fileIn = args[i];
			}
		}
		
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(fileIn);
		List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
		PairXX<IBuchi> buchiPair = pairs.get(pairs.size() - 1);
		IBuchi buchi = buchiPair.getSndElement();
//		if(Options.optNCSB) buchi.makeComplete();
		System.out.println("original: \n" + buchi.toDot());
		System.out.println("isSemiDeterministic: " + buchi.isSemiDeterministic());
		BuchiComplementSDBA buchiComplement = new BuchiComplementSDBA(buchi);
		buchiComplement.explore();
		System.out.println("complement: \n" + buchiComplement.toDot());
		
		
		
	}

	private static void printUsage() {
		// TODO Auto-generated method stub
		System.out.println("SemiBuchi v1: Library for Semi-deterministic Buchi automata");
		System.out.println("\nUsage:\n     <PROP> [options] <ATS-FILE> \n");
		System.out.println("Recommended use: java -jar SemiBuchi.jar -ascc -ac aut.ats");
		System.out.println("\nOptions:");
		System.out.println("-h: Show this page");
		System.out.println("-v: Verbose mode");
		System.out.println("-set k: 0 for BitSet, 1 for SparseBitSet\n"
				          + "       2 for TInSet, 3 for TreeSet and 4 for HashSet");
		System.out.println("-opt: Use Optimized NCSB complementation");
	}
}
