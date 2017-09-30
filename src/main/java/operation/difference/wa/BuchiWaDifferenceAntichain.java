package operation.difference.wa;

import automata.wa.IBuchiWa;
import operation.exploration.wa.AntichainDifferenceExploration;


public class BuchiWaDifferenceAntichain extends AbstractBuchiWaDifference {
    
    private final AntichainDifferenceExploration mAntichain;

    public BuchiWaDifferenceAntichain(IBuchiWa fstOp, IBuchiWa sndOp) {
        super(fstOp, sndOp);
        this.mAntichain = new AntichainDifferenceExploration(fstOp, mSndComplement);
    }

    @Override
    public IBuchiWa getResult() {
        if(mDifference == null) {
            mDifference = mAntichain.getDifference();
        }
        return mDifference;
    }

    @Override
    public String getOperationName() {
        return "DifferenceAntichain";
    }


    @Override
    public Boolean isIncluded() {
        if(mDifference == null) {
            mDifference = mAntichain.getDifference();
        }
        return mAntichain.isEmpty();
    }

}
