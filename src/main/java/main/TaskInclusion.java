package main;

import operation.inclusion.IBuchiInclusion;
import util.Timer;
import util.UtilIntSet;

public class TaskInclusion implements ITask {

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
	
	private final long mTimeLimit;
	
	private boolean mIsLHSSemiDet;
	
	private boolean mIsRHSSemiDet;
	
	private int mLHSStateNum;
	
	private int mRHSStateNum;
	
	private int mLHSTransNum;
	
	private int mRHSTransNum;
	
	private String mOperation;
	
	private long mRunTime;
	
	private Boolean mResult;
	
	private IBuchiInclusion mChecker;
	
	
	/**
	 *  some runtime data
	 *  */
	private int mNumPairsRejectedByAntichain; // number of pairs covered by some current pair
	
	private int mNumPairsDeletedInAntichain;  // number of pairs deleted since it is covered by a new pair
	
	private int mNumPairsIgnoredByAntichain; // number of pairs ignored by some current pair
	
	private int mNumPairsInAntichain;         // number of pairs left in Antichain
	
	private int mNumTransUsedInSndBuchi;      // number of transition used in the second Buchi (no duplicate)
	
	
	
	public TaskInclusion(String fileName, final long timeLimit) {
		this.mFileName = fileName;
		this.mTimeLimit = timeLimit;
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
		+ COLUMN_NAMES[7] + " = "	+ mChecker.getFstBuchi().getAlphabetSize() + "\n"
//		+ "," + mChecker.getSndBuchi().getAlphabetSize()
		+ COLUMN_NAMES[8] + " = "	+ mNumPairsRejectedByAntichain + "\n"
		+ COLUMN_NAMES[9] + " = "	+ mNumPairsDeletedInAntichain + "\n"
		+ COLUMN_NAMES[10] + " = "	+ mNumPairsIgnoredByAntichain + "\n"
		+ COLUMN_NAMES[11] + " = "	+ mNumPairsInAntichain + "\n"
		+ COLUMN_NAMES[12] + " = "	+ mNumTransUsedInSndBuchi + "\n"
		+ COLUMN_NAMES[13] + " = "	+ mChecker.getBuchiDifference().getStateSize() + "\n"
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
		+ "," + mChecker.getFstBuchi().getAlphabetSize()
//		+ "," + mChecker.getSndBuchi().getAlphabetSize()
		+ "," + mNumPairsRejectedByAntichain
		+ "," + mNumPairsDeletedInAntichain
		+ "," + mNumPairsIgnoredByAntichain
		+ "," + mNumPairsInAntichain
		+ "," + mNumTransUsedInSndBuchi
		+ "," + mChecker.getBuchiDifference().getStateSize()
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
		if(mChecker.getSndBuchiComplement() != null)
			mNumTransUsedInSndBuchi = mChecker.getSndBuchiComplement().getNumUsedOpTransition();
		// get sizes
		mLHSStateNum = mChecker.getFstBuchi().getStateSize();
		mRHSStateNum = mChecker.getSndBuchi().getStateSize();
		mLHSTransNum = mChecker.getFstBuchi().getNumTransition();
		mRHSTransNum = mChecker.getSndBuchi().getNumTransition();
		mIsLHSSemiDet = mChecker.getFstBuchi().isSemiDeterministic();
		mIsRHSSemiDet = mChecker.getSndBuchi().isSemiDeterministic();
	}
	
	public void setOperation(IBuchiInclusion checker) {
		this.mChecker = checker;
		this.mOperation = checker.getName() + "+" + UtilIntSet.getSetType();
	}
	
	public IBuchiInclusion getOperation() {
		return mChecker;
	}
	
	@Override
	public long getRuntime() {
		return mRunTime;
	}
	
	@Override
	public long getTimeBound() {
		return mTimeLimit;
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
