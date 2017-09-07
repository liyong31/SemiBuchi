package operation.universality;

import automata.IBuchiWa;
import operation.emptiness.BuchiIsEmptyASCC;

public class BuchiUniversalityASCC extends BuchiUniversality {

	public BuchiUniversalityASCC(IBuchiWa buchi) {
		super(buchi);
	}

	@Override
	protected void initializeEmptinessChecker() {
		this.mEmptinessChecker = new BuchiIsEmptyASCC(mBuchiComplement, 10 * 1000);
	}

}
