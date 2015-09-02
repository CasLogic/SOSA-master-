package edu.gsu.psych.sosa.util.watermark;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class WatermarkFile {
	
	public final static String FILENAME = "REGDAT";
	
	public final static String NULL_INDICATOR = "NOT SET";
	
	private String UUID = "";
	private String AdditionalLine1 = "";
	private String AdditionalLine2 = "";
	
	private boolean isNull = false;
	
	private File file;
	
	public WatermarkFile(){}
	
	public WatermarkFile(File file){
		this.file = file;
	}
	
	public WatermarkFile(InputStream stream) throws IOException{
		readFrom(stream);
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	private void checkFile(){
		if(file == null)
			throw new NullPointerException("file pointer not set");
	}
	
	public void writeTo(OutputStreamWriter output) throws IOException{
		BufferedWriter bw = new BufferedWriter(output);
		bw.write(UUID);
		bw.newLine();
		bw.write(AdditionalLine1);
		bw.newLine();
		bw.write(AdditionalLine2);
		
		bw.close();				
	}

	public void writeToFile() throws IOException {
		checkFile();
		
		//starting with a fresh file
		if(file.exists())
			file.delete();
		file.createNewFile();
		
		writeTo(new FileWriter(file));
	}
	
	public void readFrom(InputStream stream) throws IOException{
		read(new BufferedReader(new InputStreamReader(stream)));
	}
	
	private void read(BufferedReader br) throws IOException{
		UUID = AdditionalLine1 = AdditionalLine2 = "";
		String temp = br.readLine();
		
		if(temp.equals(NULL_INDICATOR))
			isNull = true;
		else{
			isNull = false;
			UUID = temp;
			AdditionalLine1 = br.readLine();
			AdditionalLine2 = br.readLine();
		}
		br.close();
	}
	
	public void read() throws IOException{
		checkFile();
		
		if(!file.exists())
			throw new FileNotFoundException();
		
		read(new BufferedReader(new FileReader(file)));
	}
	
	private String clean(String test){
		if(test == null)
			test = "";
		return test;
	}

	public void set(WatermarkFile input) {
		UUID = input.UUID;
		AdditionalLine1 = input.AdditionalLine1;
		AdditionalLine2 = input.AdditionalLine2;
		file = input.file;
	}
	
	public String getUUID() {
		return clean(UUID);
	}

	public String getAdditionalLine1() {
		return clean(AdditionalLine1);
	}

	public String getAdditionalLine2() {
		return clean(AdditionalLine2);
	}

	public void set(String UUID, String additionalLine1, String additionalLine2) {
		this.UUID = UUID;
		this.AdditionalLine1 = additionalLine1;
		this.AdditionalLine2 = additionalLine2;		
	}

	public BufferedInputStream getStreamForWriting() {
		return new BufferedInputStream(new ByteArrayInputStream(getBytes()));
	}
	
	public byte[] getBytes(){
		return toString().getBytes();
	}
	
	public String toString(){
		return UUID + "\n" + AdditionalLine1 + "\n" + AdditionalLine2;
	}

	public List<String> toStringList() {
		ArrayList<String> output = new ArrayList<String>();
		output.add(UUID);
		output.add(AdditionalLine1);
		output.add(AdditionalLine2);
		return output;
	}
}
