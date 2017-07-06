package main;

public interface ITask {
	
	long getTimeBound();
	
	long getRuntime();
	
	void runTask();
	
	Boolean getResult();
	
	
	// for Antichain
	void increaseRejPairByAntichain();
	
	void increaseDelPairInAntichain();
	
	void setNumPairInAntichain(int num);

}
