package operation.difference.wa;

import automata.wa.IBuchiWa;
import operation.exploration.wa.AntichainDifferenceExploration;


public class BuchiWaDifferenceAntichain extends AbstractBuchiWaDifference {
    
    private final IBuchiWa mDifference;
    private final AntichainDifferenceExploration mAntichain;

    public BuchiWaDifferenceAntichain(IBuchiWa fstOp, IBuchiWa sndOp) {
        super(fstOp, sndOp);
        this.mAntichain = new AntichainDifferenceExploration(fstOp, mSndComplement);
        mDifference = mAntichain.getDifference();
    }

    @Override
    public IBuchiWa getResult() {
        return mDifference;
    }

    @Override
    public String getOperationName() {
        return "DifferenceAntichain";
    }


    @Override
    public Boolean isIncluded() {
        return mAntichain.isEmpty();
    }

}
