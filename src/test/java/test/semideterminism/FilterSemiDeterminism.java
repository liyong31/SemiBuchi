package test.semideterminism;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import automata.IBuchi;
import util.PairXX;
import util.parser.ATSFileParser;

public class FilterSemiDeterminism {
	
	private static final String EXT = ".ats";
	
	public static void main(String[] args) {
		
//		String dir = "/home/liyong/workspace-neon/SemiBuchi/src/main/resources/benchmarks/easy";
//		
//		File fileDir = new File(dir);
//		List<String> nonSemiDetFiles = new ArrayList<>();
//		List<IBuchi> buchis = new ArrayList<>();
//		int numBA = 0;
//		int numSemiDets = 0;
//		for(File f : fileDir.listFiles( )) {
//			if(! f.getName().contains(EXT)) continue;
//			numBA ++;
//			if(!isSemiDeterministic(f)) {
//				nonSemiDetFiles.add(f.getName());
//			}else {
//				numSemiDets ++;
//			}
//		}
//		System.out.println("#BAFile=" + numBA);
//		System.out.println("#NonSemiDet=" + nonSemiDetFiles.size());
//		System.out.println("#SemiDet=" + numSemiDets);
//		for(int i = 0; i < buchis.size(); i ++) {
//			System.out.println(nonSemiDetFiles.get(i));
//			System.out.println(buchis.get(i).toDot());
//		}
		File file = new File("/home/liyong/workspace-neon/SemiBuchi/target/classes/benchmarks/easy/AliasDarteFeautrierGonnord-SAS2010-Fig1_true-termination_true-no-overflow.c_Iteration3.ats");
		System.out.println(isSemiDeterministic(file));
		
		
	}
	
	private static boolean isSemiDeterministic(File file) {
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(file.getAbsolutePath());
		List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
		boolean isSemiDet = false;
		for(PairXX<IBuchi> pair : pairs) {
			IBuchi buchi = pair.getSndElement();
			System.out.println(buchi.toDot());

			isSemiDet = buchi.isSemiDeterministic();
		}
		return isSemiDet;
	}

}
