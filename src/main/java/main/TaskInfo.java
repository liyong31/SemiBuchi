package main;

import operation.inclusion.IBuchiInclusion;
import util.Timer;

public class TaskInfo {

	private static final String[] COLUMN_NAMES = {"FILE", "TIME_LIMIT", "LHS_SEMIDETERMINISTIC"
			, "RHS_SEMIDETERMINISTIC", "STATES_LHS", "STATES_RHS", "ALGORITHM", "RUNTIME(ms)", "RESULT"};
	
	private String mFileName;
	
	private long mTimeLimit;
	
	private boolean mIsLHSSemiDet;
	
	private boolean mIsRHSSemiDet;
	
	private int mLHSStateNum;
	
	private int mRHSStateNum;
	
	private String mOperation;
	
	private long mRunTime;
	
	private Boolean mResult;
	
	private final IBuchiInclusion mChecker;
	
	public TaskInfo(IBuchiInclusion checker, String fileName, long timeLimit) {
		this.mChecker = checker;
		this.mFileName = fileName;
		this.mTimeLimit = timeLimit;
		this.mOperation = checker.getName();
	}
	
	
	public static String getColumns() {
		StringBuilder sb = new StringBuilder();
		sb.append(COLUMN_NAMES[0]);
		
		for(int i = 1; i < COLUMN_NAMES.length; i ++) {
			sb.append("," + COLUMN_NAMES[i]);
		}
		return sb.toString();
	}
	
	public String toString() {
		
		return mFileName 
		+ "," + mTimeLimit
		+ "," + mIsLHSSemiDet
		+ "," + mIsRHSSemiDet
		+ "," + mLHSStateNum
		+ "," + mRHSStateNum
		+ "," + mOperation
		+ "," + mRunTime
		+ "," + mResult;
		
	}
	
	public void runTask() {
		Timer timer = new Timer();
		timer.start();
		mResult = mChecker.isIncluded(mTimeLimit);
		timer.stop();
		mRunTime = timer.getTimeElapsed();
		// get sizes
		mLHSStateNum = mChecker.getFstBuchi().getStateSize();
		mRHSStateNum = mChecker.getSndBuchi().getStateSize();
		mIsLHSSemiDet = mChecker.getFstBuchi().isSemiDeterministic();
		mIsRHSSemiDet = mChecker.getSndBuchi().isSemiDeterministic();
	}
	
	public IBuchiInclusion getOperation() {
		return mChecker;
	}
	
	public long getRuntime() {
		return mRunTime;
	}
	
	public Boolean getResult() {
		return mResult;
	}
	
}
