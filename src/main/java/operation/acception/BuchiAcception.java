package operation.acception;

import java.util.List;

import automata.wa.IBuchiWa;
import operation.IUnaryOperation;
import operation.emptiness.BuchiIsEmptyASCC;
import operation.intersection.wa.BuchiWaIntersection;

public class BuchiAcception implements IUnaryOperation<IBuchiWa, Boolean>{
    
    private final IBuchiWa mOperand;
    private final IBuchiWa mBuchiLassoWord;
    private final IBuchiWa mIntersect;
    private Boolean mResult;
    
    public BuchiAcception(IBuchiWa operand, List<Integer> stem, List<Integer> loop) {
        mOperand = operand;
        mBuchiLassoWord = new BuchiLassoWord(operand.getAlphabetSize(), stem, loop);
        mIntersect = new BuchiWaIntersection(mOperand, mBuchiLassoWord);
    }

    @Override
    public Boolean getResult() {
        if(mResult == null) {
            BuchiIsEmptyASCC checkEmptiness = new BuchiIsEmptyASCC(mIntersect, Integer.MAX_VALUE);
            mResult = checkEmptiness.getResult();
        }
        return mResult;
    }

    @Override
    public String getOperationName() {
        return "BuchiAcception";
    }

    @Override
    public IBuchiWa getOperand() {
        return mOperand;
    }

}
