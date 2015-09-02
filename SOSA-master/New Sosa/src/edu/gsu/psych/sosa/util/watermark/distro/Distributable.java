package edu.gsu.psych.sosa.util.watermark.distro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import edu.gsu.psych.sosa.util.background.Operation;
import edu.gsu.psych.sosa.util.watermark.WatermarkFile;

public class Distributable {
	private final static int BUFFER_SIZE = 2048;
	private byte[] buffer = new byte[BUFFER_SIZE];
	
	private final static String MANIFEST_FILE = "distroManifest";
	private final static String SOSA_EXECUTABLE = "sosa.jar";
	private List<ManifestEntry> manifest = new ArrayList<ManifestEntry>();
	
	private boolean manifestLoaded = false;
	
	private ZipFile origDistroFile;

	private Operation operation;
	public boolean growOperationProgress = true;
	private boolean validated = false;
	
	public Distributable(String filePath){
		if(filePath != null && !filePath.equals("")){
			try {
				origDistroFile = new ZipFile(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			manifestLoaded = loadManifest();
		}
	}
	
	public boolean distroFileLoaded(){
		return origDistroFile != null && validated;
	}
	
	private boolean loadManifest(){
		String temp = getClass().getPackage().getName().replace('.', '/');
		InputStream is = getClass().getClassLoader().getResourceAsStream(temp+"/"+MANIFEST_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try {
			while((temp = br.readLine()) != null)
				manifest.add(new ManifestEntry(temp));			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean validate(){
		validated  = validate2();
		return validated;
	}
	
	private boolean validate2() {
		if(!manifestLoaded)
			return false;
		
		//build a listing of the given file
		List<String> contents = getEntryListing(origDistroFile);
		
		//compare it with the manifest
		for (ManifestEntry entry : manifest)
			if(!contents.remove(entry.getEntry()))	//removes items as they are found
				if(!entry.isOptional())				//returns false if they are not found (and are not marked as optional)
					return false;
		if(contents.size() > 0)	//if there are any extra files in the given zip, then return false
			return false;
		
		return true;
	}
	
	public String getName() {
		return origDistroFile.getName();
	}
	
	public InputStream getSOSAExecutable() throws IOException{
		ZipEntry temp = null;
		InputStream zip = null;
		
		temp = origDistroFile.getEntry(SOSA_EXECUTABLE);
		zip = origDistroFile.getInputStream(temp);
		
		return zip;
	}
	
	public WatermarkFile getWatermark() throws IOException {
		ZipInputStream zis = new ZipInputStream(getSOSAExecutable());
		ZipEntry wm = zis.getNextEntry();
		WatermarkFile output = null;
		while (wm != null && !wm.getName().equals(WatermarkFile.FILENAME))
			wm = zis.getNextEntry();
		if(wm != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = -1;
			while((len = zis.read(buffer)) > -1)
				baos.write(buffer,0,len);
			output = new WatermarkFile(new ByteArrayInputStream(baos.toByteArray()));
		}
		return output;
	}
	
	private List<String> getEntryListing(ZipFile zip){
		List<String> temp = new ArrayList<String>();
		
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		while(entries.hasMoreElements())
			temp.add(entries.nextElement().getName());
		
		return temp;
	}
	
	private List<String> getNonDistributableManifestEntries(){
		List<String> output = new ArrayList<String>();
		for (ManifestEntry entry : manifest)
			if(entry.isNotForDistribution())
				output.add(entry.getEntry());
		
		return output;
	}
	
	public void saveWatermark(File newDistroLocation, WatermarkFile newWM, Operation operation) throws IOException{
		setOperation(operation);
		
		//check to make sure we're not writing on top of our original file
		//this is a safeguard against potentially destroying the only copy
		if(newDistroLocation.compareTo(new File(origDistroFile.getName())) == 0)
			throw new IOException("Watermarking open file not supported");
		
		//write distro without SOSA_EXECUTABLE (and any nondistributable files noted in the manifest)
		List<String> exceptionList = getNonDistributableManifestEntries();
		exceptionList.add(SOSA_EXECUTABLE);
		ZipOutputStream zosDistro = new ZipOutputStream(new FileOutputStream(newDistroLocation));
		zipAllExcept(zosDistro,origDistroFile,exceptionList.toArray(new String[0]));
		
		//write SOSA_EXECUTABLE without watermark
		ByteArrayOutputStream sosaJar = new ByteArrayOutputStream();
		ZipOutputStream zosSOSA = new ZipOutputStream(sosaJar);
		zipAllExcept(zosSOSA, new ZipInputStream(getSOSAExecutable()), new String[]{WatermarkFile.FILENAME});
		
		//add watermark to SOSA_EXECUTABLE
		setSubOperationLabel(WatermarkFile.FILENAME);
		zosSOSA.putNextEntry(new ZipEntry(WatermarkFile.FILENAME));
		zosSOSA.write(newWM.getBytes());
		zosSOSA.closeEntry();
		zosSOSA.close();
		
		//add SOSA_EXECUTABLE to distro
		setSubOperationLabel(SOSA_EXECUTABLE);
		zosDistro.putNextEntry(new ZipEntry(SOSA_EXECUTABLE));
		zosDistro.write(sosaJar.toByteArray());
		zosDistro.closeEntry();
		zosDistro.close();
	}
	
	private void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	private void expandOperation(int size){
		if(operation != null && growOperationProgress)
			operation.expandProgressSize(size);
	}
	
	private void advanceOperation(){
		if(operation != null)
			operation.advance();
	}
	
	private void setSubOperationLabel(String label){
		if(operation != null)
			operation.setSubOperationLabel(label);
	}

	private void zipAllExcept(ZipOutputStream output, ZipFile input, String[] entriesToSkip) throws IOException{
		Enumeration<? extends ZipEntry> entries = input.entries();
		List<ZipEntry> entryList = new ArrayList<ZipEntry>();
		while(entries.hasMoreElements())
			entryList.add(entries.nextElement());
		
		expandOperation(entryList.size());
		
		for (ZipEntry next : entryList)
			addEntryToZip(output,input.getInputStream(next),next,entriesToSkip);
	}
	
	private void zipAllExcept(ZipOutputStream output, ZipInputStream input, String[] entriesToSkip) throws IOException{
		ZipEntry next = input.getNextEntry();
		while(next != null){
			expandOperation(1);	//don't know how many entries there actually are, so we'll just grow by one each time (this should have the effect of making the progress bar begin to creep slowly at this point)
			addEntryToZip(output, input, next, entriesToSkip);
			next = input.getNextEntry();
		}
	}
	
	private void addEntryToZip(ZipOutputStream output, InputStream input, ZipEntry entry, String[] entriesToSkip){
		boolean skip = false;
		for (String entryToSkip : entriesToSkip)	//compare to each of the given
			if(entry.getName().equals(entryToSkip))	//entries we're skipping
				skip = true;						//and skip if a match is found
		if(!skip)
			addEntryToZip(output,input,entry);	//otherwise add
		advanceOperation();						//either way, advance operation
	}
	
	private void addEntryToZip(ZipOutputStream output, InputStream input, ZipEntry entry){
		setSubOperationLabel(entry.getName());
		int len = -1;
		try{
			BufferedInputStream bis = new BufferedInputStream(input);
			output.putNextEntry(new ZipEntry(entry.getName()));
			if(!entry.isDirectory()){
				while((len = bis.read(buffer)) > -1)
					output.write(buffer, 0, len);
			}
			output.closeEntry();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	

	
	
	
	
	
////////////////////////////////////////////////////////////Internal Use Only	
	public static void main(String args[]){
		Distributable me = new Distributable("");
		String filePath = "";
		for (String segment : args)
			filePath += segment + " ";
		
		if(!filePath.equals("")){
			try {
				me.buildManifest(new ZipFile(new File(filePath)));
				me.exportManifestFile(new File("./"+MANIFEST_FILE));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void buildManifest(ZipFile distro){
		manifest = ManifestEntry.buildList(getEntryListing(distro));
	}
	
	private void exportManifestFile(File output) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		for (ManifestEntry entry : manifest) {
			bw.write(entry.toString());
			bw.newLine();
		}
		bw.close();
	}
}
