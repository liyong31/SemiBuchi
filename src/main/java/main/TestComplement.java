package main;

import java.io.File;

import automata.wa.IBuchiWa;
import complement.wa.BuchiWaComplement;
import util.parser.ParserType;
import util.parser.SingleParser;
import util.parser.UtilParser;

public class TestComplement {
	
	public static void main(String [] args) {
		
		if(args.length < 1) {
			printUsage();
			System.exit(0);
		}
		SingleParser parser = null;

		File fileIn = null;
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
			}else if(args[i].equals("-lazys")) {
				Options.lazyS = true;
			}else if(args[i].contains(".ats")) {
				fileIn = new File(args[i]);
				parser = UtilParser.getSinleParser(ParserType.ATS);
			}else if(args[i].contains(".ba")) {
				fileIn = new File(args[i]);
				parser = UtilParser.getSinleParser(ParserType.BA);
			}else if(args[i].equals("-lazyb")) {
				Options.lazyB = true;
			}
		}
		
		parser.parse(fileIn.getAbsolutePath());
		IBuchiWa buchi = parser.getBuchi();
//		if(Options.optNCSB) buchi.makeComplete();
		System.out.println("original dot: \n" + buchi.toDot());
//		System.out.println("original BA: \n" + buchi.toBA());
		System.out.println("isSemiDeterministic: " + buchi.isSemiDeterministic());
		BuchiWaComplement buchiComplement = new BuchiWaComplement(buchi);
		buchiComplement.explore();
		System.out.println("complement dot: \n" + buchiComplement.toDot());
//		System.out.println("complement BA: \n" + buchiComplement.toBA());
		System.out.println("complement ATS: \n");
		buchiComplement.toATS(System.out, parser.getAlphabet());
		System.out.println("\n\nNCSB" + (!Options.lazyB ? "+dc": "") + (Options.lazyS? "+opt" : "") + "," + buchiComplement.getStateSize() + "," + buchiComplement.getNumTransition());
		
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
		System.out.println("-lazys: Delay word distribution to S");
		System.out.println("-lazyb: Delay word distribution to B");

	}
}
