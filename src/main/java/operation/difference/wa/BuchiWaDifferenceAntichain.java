package operation.difference.wa;

import automata.wa.GeneralizedBuchiWa;
import automata.wa.IBuchiWa;


public class BuchiWaDifferenceAntichain extends AbstractBuchiWaDifference {
    
    private final IBuchiWa mDifference;

    public BuchiWaDifferenceAntichain(IBuchiWa fstOp, IBuchiWa sndOp) {
        super(fstOp, sndOp);
        this.mDifference = new GeneralizedBuchiWa(fstOp.getAlphabetSize());
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
        return null;
    }

}
