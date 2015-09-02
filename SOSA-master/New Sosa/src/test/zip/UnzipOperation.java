package test.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import edu.gsu.psych.sosa.main.SOSAMain;
import edu.gsu.psych.sosa.util.background.Operation;
import edu.gsu.psych.sosa.util.background.OperationRunnable;

public class UnzipOperation extends OperationRunnable{
	final static int BUFFER = 2048;

	private List<ZipEntry> entryList = new ArrayList<ZipEntry>(2000);
	private File directory;
	private ZipFile zip;
	private File target;
	
//	public UnzipOperation(ZipFile zip, File directory) {
//		Enumeration<? extends ZipEntry> entries = zip.entries();
//		
//		while(entries.hasMoreElements())
//			entryList.add(entries.nextElement());
//		
//		this.directory = directory;
//		this.zip = zip;
//		operation = Operation.Extract;
//		operation.initProgress(0, 0, entryList.size());
//	}
	
	public UnzipOperation(File origin, File target){
		try {
			this.zip = new ZipFile(origin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Enumeration<? extends ZipEntry> entries = zip.entries();		
		while(entries.hasMoreElements())
			entryList.add(entries.nextElement());
		
		this.target = target;
		operation = Operation.Extract;
		operation.initProgress(0, 0, entryList.size());
	}
	
	private void init() throws IOException{
		if(!target.createNewFile()){
			if(target.delete())
				target.createNewFile();
			else
				throw new IOException("Unable to overwrite existing file: " + target.getName());
		}
	}
	
	protected boolean prereqTask(){
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	//	operation.initProgress(0, 0, zipEntries.size());
		return true;
	}
	
	private void createFileAndPath(File file) throws IOException{
		mkdir(file.getParentFile());
		
		if(!file.createNewFile())
			if(file.exists())
				throw new IOException("Attempting to extract file: " + file + "\n\tFile already exists!");
//		} catch (IOException e){
//			if(e.getMessage().indexOf("cannot find the path") > -1){
//				mkdir(file.getParentFile());
//				if(!file.createNewFile())
//					if(file.exists())
//						throw new IOException("Attempting to extract file: " + file + "\n\tFile already exists!");
//			}else 
//				throw e;
//		}
	}
	
	private boolean mkdir(File directory){
		//make sure this directory is within the path of this.directory
		if(directory.getPath().indexOf(this.directory.getPath()) < 0)
			return false;
		if(!directory.getParentFile().exists())
			mkdir(directory.getParentFile());
		return directory.mkdir();
	}
	
	private void removeDirectoryAndContents(File directory){
		if(directory.isDirectory())
			for(File file : directory.listFiles())
				removeDirectoryAndContents(file);
		directory.delete();
	}

	@Override
	public void task() {
		byte data[] = new byte[BUFFER];
		
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
			
			for(ZipEntry entry : entryList){
				if(!operation.isStop()){
					operation.setSubOperationLabel(entry.getName());
					try{
						zos.putNextEntry(entry);
						
						//if(!entry.isDirectory()){
							BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
							while(bis.available() > 0)
								zos.write(data, 0, bis.read(data));
						//} else
						//	System.out.println(entry);
						zos.closeEntry();
					} catch (ZipException e){
						e.printStackTrace();
					}
					operation.advance();
				} else{
					//TODO: delete target file (we're being prematurely stopped, file is no good)
				}
			}
			zos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void task2() {
		byte data[] = new byte[BUFFER];
		if(directory.exists())
			if(directory.isDirectory()){
				operation.setSubOperationLabel("Preparing for extraction...");
				operation.progress.indeterminate = true;
				removeDirectoryAndContents(directory);
				operation.progress.indeterminate = false;
			}
		directory.mkdir();
		
		for(ZipEntry entry : entryList){
			if(!operation.isStop()){
				operation.setSubOperationLabel(entry.getName());
				if(SOSAMain.DEBUG){
					System.out.println(operation);
				}
				File output = new File(directory.getAbsolutePath() + File.separator + entry.getName());
				if(entry.isDirectory())
					output.mkdir();
				else {
					try {
						createFileAndPath(output);
//						currentEntrySize = entry.getSize();
						InputStream is = zip.getInputStream(entry);
						FileOutputStream fos = new FileOutputStream(output);
						while(is.available() > 0){
							fos.write(data,0,is.read(data));
						}
						fos.flush();
						fos.close();
						is.close();
					} catch (IOException e){
						e.printStackTrace();
					}
				}
				operation.advance();
			}
		}
	}
}
