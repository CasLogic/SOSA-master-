package test.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import edu.gsu.psych.sosa.util.background.Operation;
import edu.gsu.psych.sosa.util.background.OperationRunnable;


public class RezipOperation extends OperationRunnable.WithPrereq{
	final static int BUFFER = 2048;
	
	private File target;
	private File directory;
	private Map<ZipEntry, File> zipEntries = new HashMap<ZipEntry, File>();
	
	public RezipOperation(File target, File directory){
		this.target = target;
		this.directory = directory;
		operation = Operation.Compress;
	}
	
	private void init() throws IOException{
		if(directory.exists() && directory.isDirectory())
			processDirectory("", directory);
		else
			throw new IOException("Given argument '" + directory.getName() + "' is not a directory");
		
		if(!target.createNewFile()){
			if(target.delete())
				target.createNewFile();
			else
				throw new IOException("Unable to overwrite existing file: " + target.getName());
		}
	}

	private void processDirectory(String prefix, File directory) {
		for(File file : directory.listFiles()){
			if(file.isDirectory()){
		//		zipEntries.put(new ZipEntry(prefix + file.getName() + "/"), file);
				processDirectory(prefix + file.getName() + "/", file);
			} else
				zipEntries.put(new ZipEntry(prefix + file.getName()), file);			
		}
	}
	
	@Override
	protected boolean prereqTask(){
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		operation.initProgress(0, 0, zipEntries.size());
		return true;
	}

	@Override
	public void task() {
		byte data[] = new byte[BUFFER];
		
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
			
			for(Map.Entry<ZipEntry, File> entry : zipEntries.entrySet()){
				if(!operation.isStop()){
					operation.setSubOperationLabel(entry.getKey().getName());
					try{
						zos.putNextEntry(entry.getKey());
						
						if(!entry.getValue().isDirectory()){
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(entry.getValue()));
							while(bis.available() > 0)
								zos.write(data, 0, bis.read(data));
						} else
							System.out.println(entry);
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
}