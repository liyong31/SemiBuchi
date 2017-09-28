package operation.difference.wa;

import java.util.List;

import automata.wa.IBuchiWa;
import automata.wa.IStateWa;
import complement.wa.BuchiWaComplement;
import complement.wa.IBuchiWaComplement;

import util.IPair;

public abstract class AbstractBuchiWaDifference implements IBuchiWaDifference {

    protected final IBuchiWa mFstOperand;
    protected final IBuchiWa mSndOperand;
    protected final BuchiWaComplement mSndComplement;
    protected IBuchiWa mDifference;
    
    public AbstractBuchiWaDifference(IBuchiWa fstOp, IBuchiWa sndOp) {
        mFstOperand = fstOp;
        mSndOperand = sndOp;
        assert fstOp.getAlphabetSize() == sndOp.getAlphabetSize();
        mSndComplement = new BuchiWaComplement(sndOp);
    }
    
    @Override
    public IBuchiWa getFirstOperand() {
        return mFstOperand;
    }

    @Override
    public IBuchiWa getSecondOperand() {
        return mSndOperand;
    }

    @Override
    public IBuchiWa getResult() {
        return mDifference;
    }

    @Override
    public IBuchiWaComplement getSecondBuchiComplement() {
        return mSndComplement;
    }

    @Override
    public IPair<List<Integer>, List<Integer>> getCounterexampleWord() {
        return null;
    }

    @Override
    public IPair<List<IStateWa>, List<IStateWa>> getCounterexampleRun() {
        return null;
    }

}
