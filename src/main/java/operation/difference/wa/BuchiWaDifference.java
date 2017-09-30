package operation.difference.wa;

import automata.wa.IBuchiWa;
import main.Options;
import operation.exploration.wa.EmptinessExploration;
import operation.intersection.wa.BuchiWaIntersection;
import operation.intersection.wa.GeneralizedBuchiIntersection;

public abstract class BuchiWaDifference extends AbstractBuchiWaDifference {

    protected EmptinessExploration mExplorer;
    
    public BuchiWaDifference(IBuchiWa fstOp, IBuchiWa sndOp) {
        super(fstOp, sndOp);
        if(Options.useGBA) {
            mDifference = new GeneralizedBuchiIntersection(fstOp, mSndComplement);
        }else {
            mDifference = new BuchiWaIntersection(fstOp, mSndComplement);
        }
    }
    
    abstract protected void initializeExplorer();
    
    @Override
    public Boolean isIncluded() {
        if(mExplorer == null)
            initializeExplorer();
        return mExplorer.isEmpty();
    }

}
