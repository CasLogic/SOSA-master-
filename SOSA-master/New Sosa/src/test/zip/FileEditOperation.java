package test.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import edu.gsu.psych.sosa.util.background.OperationRunnable;
import edu.gsu.psych.sosa.util.watermark.WatermarkFile;
import edu.gsu.psych.sosa.util.watermark.distro.Distributable;

public class FileEditOperation extends OperationRunnable {
	
	final static int BUFFER = 2048;
	
	ZipFile distroFile;
	File jarFile;
	ZipFile jar;
	ZipEntry wmEntry;
	
	WatermarkFile file;
	
	List<Basic> subOperations = new ArrayList<Basic>();
	
	private boolean failure = false;
	
	public FileEditOperation(File zip){
		jarFile = zip;
		 try {
			jar = new ZipFile(zip);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//throw new FileNotFoundException("Given file '" + filename + "' does not exist in " + zip.getName());
		 
//		 subOperations.add(validateDistroFile2());
//		 subOperations.add(openSOSAJar2());
//		 subOperations.add(getWMFile2());
		 
	}
	
//	private void validateDistroFile(){
//		Distributable distro = new Distributable(distroFile);
//		if(!distro.validate(distroFile))
//			failure = true;
//	}
	
	private void getWMFile() {
		wmEntry = jar.getEntry(WatermarkFile.FILENAME);
		
		if(wmEntry != null){
			try {
				file = new WatermarkFile(jar.getInputStream(wmEntry));				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void testWriteExistingWM(){
		byte data[] = new byte[BUFFER];
		file.set("something", "something", "something");
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(jarFile));
			zos.putNextEntry(wmEntry);
			BufferedInputStream bis = file.getStreamForWriting();
			while(bis.available() > 0)
				zos.write(data, 0, bis.read(data));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	                           
	
	private void openSOSAJar(){
		
	}
	
	private Basic validateDistroFile2(){
		return new Basic() {
			@Override
			protected void task() {
//				validateDistroFile();
			}
		};
	}
	
	private Basic openSOSAJar2(){
		return new Basic() {
			@Override
			protected void task() {
				openSOSAJar();
			}
		};
	}
	
	private Basic getWMFile2(){
		return new Basic() {
			@Override
			protected void task() {
				getWMFile();
			}
		};
	}

	@Override
	protected void task() {
//		validateDistroFile();
//		openSOSAJar();
		
//		for (OperationRunnable subOperation : subOperations){
//			subOperation.run();
//			if(failure){
//				System.out.println("'Failure' flag set... aborting");
//				break;
//			}
//		}
	}
	
	public static void main(String args[]){
		String filePath = "";
		for (String segment : args)
			filePath += segment + " ";
		
		if(!filePath.equals("")){
			FileEditOperation me = new FileEditOperation(new File(filePath));
			me.getWMFile();
			me.testWriteExistingWM();
		}
	}

}
