package operation.universality;


import automata.wa.IBuchiWa;
import operation.emptiness.BuchiIsEmptyTarjanOriginal;

public class BuchiUniversalityTarjanOriginal extends BuchiUniversality {

	public BuchiUniversalityTarjanOriginal(IBuchiWa buchi) {
		super(buchi);
	}

	@Override
	protected void initializeEmptinessChecker() {
		// TODO Auto-generated method stub
		this.mEmptinessChecker = new BuchiIsEmptyTarjanOriginal(mBuchiComplement, 10 * 1000);
	}
	
	@Override
	public String getOperationName() {
		return "UniversalityTarjan";
	}

}
