package operation.universality;

import java.util.List;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmptyNestedDFS;
import util.IPair;

public class BuchiUniversalityNestedDFS extends BuchiUniversality {

	public BuchiUniversalityNestedDFS(IBuchiWa buchi) {
		super(buchi);
	}

	@Override
	protected void initializeEmptinessChecker() {
		this.mEmptinessChecker = new BuchiIsEmptyNestedDFS(mBuchiComplement, 10 * 1000);
	}

}
