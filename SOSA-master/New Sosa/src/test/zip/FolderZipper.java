package test.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderZipper {
	final static int BUFFER = 1000;
	
	final static boolean method = false;
	
	public static void main(String a[]) {
		if(method){
			try {
				File inFolder = new File("out");
				File outFolder = new File("Out.zip");
				ZipOutputStream out = new ZipOutputStream(
						new BufferedOutputStream(new FileOutputStream(outFolder)));
				BufferedInputStream in = null;
				byte[] data = new byte[BUFFER];
				String files[] = inFolder.list();
				for (int i = 0; i < files.length; i++) {
					in = new BufferedInputStream(new FileInputStream(inFolder
							.getPath() + "/" + files[i]), BUFFER);
					out.putNextEntry(new ZipEntry(files[i]));
					int count;
					while ((count = in.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					out.closeEntry();
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				File outputFile = new File("test.zip");
				if(!outputFile.createNewFile())
					if(outputFile.delete())
						outputFile.createNewFile();
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFile));
				zos.putNextEntry(new ZipEntry("test1"));
//				zos.putNextEntry(new ZipEntry("test7/"));
				zos.putNextEntry(new ZipEntry("test7/test2"));
				zos.finish();
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
}

