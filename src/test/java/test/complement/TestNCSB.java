package test.complement;

import java.util.List;

import automata.BuchiGeneral;
import automata.IBuchi;
import automata.IState;
import complement.BuchiComplementSDBA;
import main.Options;
import util.PairXX;
import util.parser.ats.ATSFileParser;

public class TestNCSB {
	
	private static IBuchi getA() {
		IBuchi buchi = new BuchiGeneral(2);
		IState a = buchi.addState();
		IState b = buchi.addState();
		
		a.addSuccessor(0, 0);
		a.addSuccessor(1, 1);
		
		b.addSuccessor(1, 0);
		buchi.setInitial(0);
		buchi.setFinal(1);
		return buchi;
	}
	
	private static IBuchi getB() {
		IBuchi buchi = new BuchiGeneral(2);
		IState a = buchi.addState();
		IState b = buchi.addState();
		
//		a.addSuccessor(0, 0);
		a.addSuccessor(1, 1);
		
		b.addSuccessor(1, 0);
		buchi.setInitial(0);
		buchi.setFinal(0);
		
		// other part
		IState c = buchi.addState();
		IState d = buchi.addState();
		IState e = buchi.addState();
		
		c.addSuccessor(1, 3);
		d.addSuccessor(0, 4);
		buchi.setFinal(d);
		buchi.setInitial(c);
		return buchi;
	}
	
	private static IBuchi getBuchiFromDir() {
		// PodelskiRybalchenko-LICS2004-Fig2-TACAS2011-Fig3_true-termination.c_Iteration3.ats
		// PodelskiRybalchenko-LICS2004-Fig2_true-termination.c_Iteration3.ats
		// PodelskiRybalchenko-TACAS2011-Fig3_true-termination.c_Iteration3.ats
		String dir = "/home/liyong/workspace-neon/SemiBuchi/target/AliasDarteFeautrierGonnord-SAS2010-Fig1_true-termination_true-no-overflow.c_Iteration3.ats";
		ATSFileParser atsParser =  new ATSFileParser();
		atsParser.parse(dir);
		
		List<PairXX<IBuchi>> pairs = atsParser.getBuchiPairs();
		return pairs.iterator().next().getSndElement();
		
	}

	public static void main(String []args) {
		
		IBuchi buchi = getBuchiFromDir();
		System.out.println(buchi.toDot());
		System.out.println(buchi.getFinalStates());
		BuchiComplementSDBA c = new BuchiComplementSDBA(buchi);
		c.explore();
		System.out.println("complement:\n" + c.toDot());
		
		Options.optNCSB = true;
		c = new BuchiComplementSDBA(buchi);
		c.explore();
		System.out.println("complement opt:\n" + c.toDot());
		
	}
}
