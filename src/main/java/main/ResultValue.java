package main;

public class ResultValue {
	
	public long mTimeElapsed; // milliseconds
	public Boolean mResult;    // null means time out or other things
	
	public ResultValue(long time, Boolean result) {
		this.mTimeElapsed = time;
		this.mResult = result;
	}

}
