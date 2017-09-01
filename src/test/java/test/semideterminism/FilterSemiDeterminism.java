package test.semideterminism;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import automata.IBuchi;
import automata.IBuchiWa;
import util.PairXX;
import util.parser.ats.ATSFileParser;

public class FilterSemiDeterminism {
	
	private static final String EXT = ".ats";
	private static final String NAME = "difficult";
	private static String det = "/home/liyong/workspace-neon/SemiBuchi/target/"+ NAME + "/";
	private static int numBA = 0;
	public static void main(String[] args) {
		
		String dir = "/home/liyong/workspace-neon/SemiBuchi/src/main/resources/benchmarks/" + NAME;
		
		File fileDir = new File(dir);
		List<String> nonSemiDetFiles = new ArrayList<>();
		List<IBuchiWa> buchis = new ArrayList<>();
		
		int numSemiDets = 0;
		for(File f : fileDir.listFiles( )) {
			if(! f.getName().contains(EXT)) continue;
			numBA ++;
			if(!isSemiDeterministic(f)) {
				nonSemiDetFiles.add(f.getName());
			}else {
				numSemiDets ++;
			}
		}
		System.out.println("#BAFile=" + numBA);
		System.out.println("#NonSemiDet=" + nonSemiDetFiles.size());
		System.out.println("#SemiDet=" + numSemiDets);
		for(int i = 0; i < buchis.size(); i ++) {
			System.out.println(nonSemiDetFiles.get(i));
			System.out.println(buchis.get(i).toDot());
		}
		
		
	}
	
	private static boolean isSemiDeterministic(File file) {
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(file.getAbsolutePath());
		List<PairXX<IBuchiWa>> pairs = atsParser.getBuchiPairs();
		boolean isSemiDet = false;
		for(PairXX<IBuchiWa> pair : pairs) {
			IBuchiWa buchi = pair.getSndElement();
			System.out.println(buchi.toDot());

			isSemiDet = buchi.isSemiDeterministic();
			if(isSemiDet) {
				try {

					String name = det + NAME + numBA;
					File f = new File(name + ".ba");
					FileWriter writer = new FileWriter(f);
					buchi.makeComplete();
					writer.write(buchi.toBA());
					writer.close();
					// 
					f = new File(name + ".ats");
					writer = new FileWriter(f);
					Scanner lineReader = new Scanner(file);
					while(lineReader.hasNextLine()) {
						writer.write(lineReader.nextLine() + "\n");
					}
					lineReader.close();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		return isSemiDet;
	}

}
