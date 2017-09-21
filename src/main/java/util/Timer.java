package util;

// TODO
public class Timer {
	
	private long mTime;
	
	public Timer() {
	}
	
	public void start() {
		mTime = System.currentTimeMillis();
	}
	
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	public long tick() {
		return System.currentTimeMillis() - mTime;
	}
	
	public void stop() {
		mTime = System.currentTimeMillis() - mTime;
	}
	
	public long getTimeElapsed() {
		return mTime;
	}

}
