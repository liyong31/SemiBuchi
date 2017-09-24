package main;

public interface ITask {
	
	
	String getOperationName();
		
	long getRuntime();
	
	void runTask();
	
	ResultValue getResultValue();
	
	void setResultValue(ResultValue resultValue);
	
	// for Antichain
	void increaseRejPairByAntichain();
	
	void increaseDelPairInAntichain();
	
	void increaseIngPairByAntichain();
	
	void setNumPairInAntichain(int num);
	
	String toString();
	String toStringVerbose();

}
