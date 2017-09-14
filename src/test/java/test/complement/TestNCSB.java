package test.complement;

import java.util.List;

import automata.BuchiWa;
import automata.IBuchiWa;
import automata.IStateWa;
import complement.BuchiWaComplement;
import main.Options;
import util.PairXX;
import util.parser.ats.ATSFileParser;

public class TestNCSB {
	
	private static IBuchiWa getA() {
		IBuchiWa buchi = new BuchiWa(2);
		IStateWa a = buchi.addState();
		IStateWa b = buchi.addState();
		
		a.addSuccessor(0, 0);
		a.addSuccessor(1, 1);
		
		b.addSuccessor(1, 0);
		buchi.setInitial(0);
		buchi.setFinal(1);
		return buchi;
	}
	
	private static IBuchiWa getB() {
		IBuchiWa buchi = new BuchiWa(2);
		IStateWa a = buchi.addState();
		IStateWa b = buchi.addState();
		
//		a.addSuccessor(0, 0);
		a.addSuccessor(1, 1);
		
		b.addSuccessor(1, 0);
		buchi.setInitial(0);
		buchi.setFinal(0);
		
		// other part
		IStateWa c = buchi.addState();
		IStateWa d = buchi.addState();
		IStateWa e = buchi.addState();
		
		c.addSuccessor(1, 3);
		d.addSuccessor(0, 4);
		buchi.setFinal(d);
		buchi.setInitial(c);
		return buchi;
	}
	
	private static IBuchiWa getBuchiFromDir() {
		// PodelskiRybalchenko-LICS2004-Fig2-TACAS2011-Fig3_true-termination.c_Iteration3.ats
		// PodelskiRybalchenko-LICS2004-Fig2_true-termination.c_Iteration3.ats
		// PodelskiRybalchenko-TACAS2011-Fig3_true-termination.c_Iteration3.ats
		String dir = "/home/liyong/workspace-neon/SemiBuchi/target/AliasDarteFeautrierGonnord-SAS2010-Fig1_true-termination_true-no-overflow.c_Iteration3.ats";
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(dir);
		
		List<PairXX<IBuchiWa>> pairs = atsParser.getBuchiPairs();
		return pairs.iterator().next().getSndElement();
		
	}

	public static void main(String []args) {
		
		IBuchiWa buchi = getBuchiFromDir();
		System.out.println(buchi.toDot());
		System.out.println(buchi.getFinalStates());
		BuchiWaComplement c = new BuchiWaComplement(buchi);
		c.explore();
		System.out.println("complement:\n" + c.toDot());
		
		Options.lazyS = true;
		c = new BuchiWaComplement(buchi);
		c.explore();
		System.out.println("complement opt:\n" + c.toDot());
		
	}
}
