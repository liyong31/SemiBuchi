package test.inclusion;

import java.util.List;

import automata.IBuchi;
import operation.inclusion.BuchiInclusionComplement;
import util.PairXX;
import util.parser.ATSFileParser;

public class TestBuchiInclusionTarjan {
	
	public static void main(String[] args) {
		String dir = "/home/liyong/projects/liyong/git-repo/ultimate/trunk/examples/"
				+ "automata-benchmarks/20170611-TerminationAnalysis-BuchiIsIncluded/"
				+ "bist_cell_true-unreach-call_false-termination.cil.c_Iteration17.ats";
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(dir);
		List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
		
		for(PairXX<IBuchi> pair : pairs) {
			BuchiInclusionComplement complement = new BuchiInclusionComplement(pair.getFstElement(), pair.getSndElement());
			System.out.println("IsIncluded: " + complement.isIncluded(20*1000) + "");
			//System.out.println(complement.getBuchiDifference().toDot());
		}
		
	}

}
