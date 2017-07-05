package main;

public interface ITask {
	
	long getTimeBound();
	
	long getRuntime();
	
	void runTask();
	
	Boolean getResult();

}
