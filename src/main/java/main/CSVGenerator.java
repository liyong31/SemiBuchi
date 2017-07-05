package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVGenerator {
	
	private final String mFileName;
	private final FileWriter mFileWriter;
	
	public CSVGenerator(String name) throws IOException {
		this.mFileName = name;
		this.mFileWriter = new FileWriter(new File(name));
	}
	
	public void start() throws IOException {
		System.out.println("Writing results to " + mFileName);
		mFileWriter.write(TaskInclusion.getColumns() + "\n");
	}
	
	public void addRows(TaskInclusion info) throws IOException {
		mFileWriter.write(info + "\n");
	}
	
	public void end() throws IOException {
		mFileWriter.close();
		System.out.println("Done for writing results to " + mFileName);
	}

}
