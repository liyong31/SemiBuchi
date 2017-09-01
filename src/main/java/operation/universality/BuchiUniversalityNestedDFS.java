package operation.universality;

import java.util.List;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmpty;
import operation.emptiness.BuchiIsEmptyNestedDFS;
import util.IPair;

public class BuchiUniversalityNestedDFS extends BuchiUniversality {

	public BuchiUniversalityNestedDFS(IBuchiWa buchi) {
		super(buchi);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean isUniversal() {
		// TODO Auto-generated method stub
		BuchiIsEmpty checker = new BuchiIsEmptyNestedDFS(mBuchiComplement, 10 * 1000);
		return checker.isEmpty();
	}

	@Override
	public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
		// TODO Auto-generated method stub
		return null;
	}

}
