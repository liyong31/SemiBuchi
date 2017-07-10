package test.semideterminism;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import automata.IBuchi;
import util.PairXX;
import util.parser.ATSFileParser;

public class FilterSemiDeterminism {
	
	private static final String EXT = "ats";
	
	public static void main(String[] args) {
		
		String dir = "src/main/resources/benchmarks/normal";
		
		File fileDir = new File(dir);
		List<String> nonSemiDetFiles = new ArrayList<>();
		List<IBuchi> buchis = new ArrayList<>();
		int numBA = 0;
		for(File f : fileDir.listFiles( )) {
			if(! f.getName().contains(EXT)) continue;
			System.out.println(f.getName());
			ATSFileParser atsParser =  new ATSFileParser();
			atsParser.parse(f.getAbsolutePath());
			List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
			numBA ++;
			for(PairXX<IBuchi> pair : pairs) {
				IBuchi buchi = pair.getSndElement();
				if(! buchi.isSemiDeterministic()) {
					nonSemiDetFiles.add(f.getName());
					buchis.add(buchi);
				}
			}
		}
		System.out.println("#BAFile=" + numBA);
		System.out.println("#NonSemiDet=" + nonSemiDetFiles.size());
		for(int i = 0; i < buchis.size(); i ++) {
			System.out.println(nonSemiDetFiles.get(i));
			System.out.println(buchis.get(i).toDot());
		}
		
		
		
	}

}
