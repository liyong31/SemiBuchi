package operation.universality;


import automata.IBuchiWa;
import complement.BuchiWaComplement;

public abstract class BuchiUniversality implements IBuchiUniversality {
	
	protected IBuchiWa mBuchi;
	protected BuchiWaComplement mBuchiComplement;
	
	public BuchiUniversality(IBuchiWa buchi) {
		this.mBuchi = buchi;
		this.mBuchiComplement = new BuchiWaComplement(buchi);
	}

	@Override
	public IBuchiWa getBuchi() {
		// TODO Auto-generated method stub
		return mBuchi;
	}

	@Override
	public IBuchiWa getBuchiComplement() {
		// TODO Auto-generated method stub
		return mBuchiComplement;
	}

}
