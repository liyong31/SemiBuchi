package main;

import operation.difference.IBuchiWaDifference;
import operation.inclusion.BuchiInclusionRABIT;
import operation.inclusion.IBuchiInclusion;
import util.Timer;
import util.UtilIntSet;

public class TaskDifference implements ITask {

	private static final String[] COLUMN_NAMES = {
			"FILE"
			//, "TIME_LIMIT"
			, "LHS_SEMIDETERMINISTIC"
			, "RHS_SEMIDETERMINISTIC"
			, "LHS_STATES"
			, "RHS_STATES"
			, "LHS_TRANS"
			, "RHS_TRANS"
			, "ALPHABET_SIZE" // shoud be the same as RHS_ALPHABET
//			, "RHS_ALPHABET"
			, "PAIR_REJECT_ANTICHAIN"
			, "PAIR_DELETE_ANTICHAIN"
			, "PAIR_INGNORE_ANTICHAIN"
			, "PAIR_LEFT_ANTICHAIN"
			, "TRANS_USED_SND_BUCHI"
			, "RESULT_STATES"
			, "ALGORITHM"
			, "RUNTIME(ms)"
			, "RESULT"
			};
	
	private String mFileName;
	
//	private final long mTimeLimit;
	
	private boolean mIsLHSSemiDet;
	
	private boolean mIsRHSSemiDet;
	
	private int mLHSStateNum;
	
	private int mRHSStateNum;
	
	private int mLHSTransNum;
	
	private int mRHSTransNum;
	
	private String mOperation;
	
	private long mRunTime;
	
	private Boolean mResult;
	
	private IBuchiWaDifference mChecker;
	
	
	/**
	 *  some runtime data
	 *  */
	private int mNumPairsRejectedByAntichain; // number of pairs covered by some current pair
	
	private int mNumPairsDeletedInAntichain;  // number of pairs deleted since it is covered by a new pair
	
	private int mNumPairsIgnoredByAntichain; // number of pairs ignored by some current pair
	
	private int mNumPairsInAntichain;         // number of pairs left in Antichain
	
	private int mNumTransUsedInSndBuchi;      // number of transition used in the second Buchi (no duplicate)
	
	
	
	public TaskDifference(String fileName) {
		this.mFileName = fileName;
//		this.mTimeLimit = timeLimit;
	}
	
	
	public static String getColumns() {
		StringBuilder sb = new StringBuilder();
		sb.append(COLUMN_NAMES[0]);
		
		for(int i = 1; i < COLUMN_NAMES.length; i ++) {
			sb.append("," + COLUMN_NAMES[i]);
		}
		return sb.toString();
	}
	
	@Override
	public String toStringVerbose() {
		assert mChecker != null;
		return 
		COLUMN_NAMES[0] + " = "	+ mFileName + "\n"
//		+ "," + mTimeLimit
		+ COLUMN_NAMES[1] + " = "	+ mIsLHSSemiDet  + "\n"
		+ COLUMN_NAMES[2] + " = "	+ mIsRHSSemiDet + "\n"
		+ COLUMN_NAMES[3] + " = "	+ mLHSStateNum + "\n"
		+ COLUMN_NAMES[4] + " = "	+ mRHSStateNum + "\n"
		+ COLUMN_NAMES[5] + " = "	+ mLHSTransNum + "\n"
		+ COLUMN_NAMES[6] + " = "	+ mRHSTransNum + "\n"
		+ COLUMN_NAMES[7] + " = "	+ mChecker.getFirstOperand().getAlphabetSize() + "\n"
//		+ "," + mChecker.getSndBuchi().getAlphabetSize()
		+ COLUMN_NAMES[8] + " = "	+ mNumPairsRejectedByAntichain + "\n"
		+ COLUMN_NAMES[9] + " = "	+ mNumPairsDeletedInAntichain + "\n"
		+ COLUMN_NAMES[10] + " = "	+ mNumPairsIgnoredByAntichain + "\n"
		+ COLUMN_NAMES[11] + " = "	+ mNumPairsInAntichain + "\n"
		+ COLUMN_NAMES[12] + " = "	+ mNumTransUsedInSndBuchi + "\n"
		+ COLUMN_NAMES[13] + " = "	+ mChecker.getResult().getStateSize() + "\n"
		+ COLUMN_NAMES[14] + " = "	+ mOperation + "\n"
		+ COLUMN_NAMES[15] + " = "	+ mRunTime + "\n"
		+ COLUMN_NAMES[16] + " = "	+ mResult  + "\n";
		
	}
	@Override
	public String toString() {
		assert mChecker != null;
		return mFileName 
//		+ "," + mTimeLimit
		+ "," + mIsLHSSemiDet
		+ "," + mIsRHSSemiDet
		+ "," + mLHSStateNum
		+ "," + mRHSStateNum
		+ "," + mLHSTransNum
		+ "," + mRHSTransNum
		+ "," + mChecker.getFirstOperand().getAlphabetSize()
//		+ "," + mChecker.getSndBuchi().getAlphabetSize()
		+ "," + mNumPairsRejectedByAntichain
		+ "," + mNumPairsDeletedInAntichain
		+ "," + mNumPairsIgnoredByAntichain
		+ "," + mNumPairsInAntichain
		+ "," + mNumTransUsedInSndBuchi
		+ "," + mChecker.getResult().getStateSize()
		+ "," + mOperation
		+ "," + mRunTime
		+ "," + mResult;
		
	}
	
	@Override
	public void runTask() {
		Timer timer = new Timer();
		timer.start();
		mResult = mChecker.isIncluded();
		timer.stop();
		mRunTime = timer.getTimeElapsed();
		// first get the used transition in mSndOperation
		if(mChecker.getSecondBuchiComplement() != null)
			mNumTransUsedInSndBuchi = mChecker.getSecondBuchiComplement().getNumUsedOpTransition();
		// get sizes
		mLHSStateNum = mChecker.getFirstOperand().getStateSize();
		mRHSStateNum = mChecker.getSecondOperand().getStateSize();
		mLHSTransNum = mChecker.getFirstOperand().getNumTransition();
		mRHSTransNum = mChecker.getSecondOperand().getNumTransition();
//		mIsLHSSemiDet = mChecker.getFstBuchi().isSemiDeterministic();
//		mIsRHSSemiDet = mChecker.getSndBuchi().isSemiDeterministic();
	}
	
	public void setOperation(IBuchiWaDifference checker) {
		this.mChecker = checker;
		this.mOperation = checker.getOperationName();
		this.mOperation += "+" + UtilIntSet.getSetType() + (Options.lazyS ? "+lazyS" : "")
				                                         + (Options.lazyB ? "+lazyB" : "");
	}
	
	public IBuchiWaDifference getOperation() {
		return mChecker;
	}
	
	@Override
	public long getRuntime() {
		return mRunTime;
	}
	
	@Override
	public long getTimeBound() {
		return 0;
	}
	
	@Override
	public Boolean getResult() {
		return mResult;
	}
	
	@Override
	public void increaseRejPairByAntichain() {
		mNumPairsRejectedByAntichain ++;
	}
	
	@Override
	public void increaseDelPairInAntichain() {
		mNumPairsDeletedInAntichain ++;
	}
	
	@Override
	public void increaseIngPairByAntichain() {
		mNumPairsIgnoredByAntichain ++;
	}
	
	@Override
	public void setNumPairInAntichain(int num) {
		mNumPairsInAntichain = num;
	}
	
//	public void useTransition() {
//		mNumTransUsedInSndBuchi
//	}
	
}
