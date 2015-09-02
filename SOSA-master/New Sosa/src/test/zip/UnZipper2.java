package test.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// found on:
// http://www.devx.com/tips/Tip/22124
public class UnZipper2 {
	private static String jarFile;
	private static String destDir;

	public static void main(String args[]) {
		try{
			JarFile jar = new JarFile(jarFile);
			Enumeration<JarEntry> enum1 = jar.entries();
			while (enum1.hasMoreElements()) {
				JarEntry file = enum1.nextElement();
				File f = new File(destDir + File.separator + file.getName());
				if (file.isDirectory()) { // if its a directory, create it
					f.mkdir();
					continue;
				}
				InputStream is = jar.getInputStream(file); // get the input stream
				FileOutputStream fos = new FileOutputStream(f);
				while (is.available() > 0) {  // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}

