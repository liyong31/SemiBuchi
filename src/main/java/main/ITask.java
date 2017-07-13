package main;

public interface ITask {
	
	long getTimeBound();
	
	long getRuntime();
	
	void runTask();
	
	Boolean getResult();
	
	
	// for Antichain
	void increaseRejPairByAntichain();
	
	void increaseDelPairInAntichain();
	
	void increaseIngPairByAntichain();
	
	void setNumPairInAntichain(int num);
	
	String toString();
	String toStringVerbose();

}
