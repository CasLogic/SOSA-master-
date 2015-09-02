package edu.gsu.psych.sosa.main;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * LibraryLoader is a class to dynamically pick and load operating system and architecture
 * specific libraries (i.e. swt, lwjgl, anything that is system specific)
 * 
 * Code was found online at
 * https://www.chrisnewland.com/select-correct-swt-jar-for-your-os-and-jvm-at-runtime-191
 * and altered slightly to fit path and optimized
 * 
 * @author scavedo
 * @date 04/09/2015
 */
public class LibraryLoader {	
	/**
	 * Gets the swt file location for the given architecture
	 * Uses the format 'lib/swt_OS_xArch/swt.jar'
	 * @return swt path for current architecture
	 */
	public static String getArchSwt() {
		return "lib/swt_" + getOSName() + "_x" + getArchName() + "/swt.jar";
	}
	
	/**
	 * Gets the lwjgl native path for the given OS
	 * Uses the format 'lib/lwjgl/native/OS'
	 * @return lwjgl native path
	 */
	public static String getArchLwjglNative() {
		return "lib/lwjgl/native/" + getOSName();
	}

	/**
	 * Gets the os name of the architecture that the program is running on
	 * @return OS name
	 */
	private static String getOSName() { 
	   String osNameProperty = System.getProperty("os.name"); 

	   if (osNameProperty == null) { 
	       throw new RuntimeException("os.name property is not set"); 
	   } 
	   else { 
	       osNameProperty = osNameProperty.toLowerCase(); 
	   } 

	   if (osNameProperty.contains("win")) { 
	       return "win"; 
	   } 
	   else if (osNameProperty.contains("mac")) { 
	       return "osx"; 
	   } 
	   else if (osNameProperty.contains("linux") || osNameProperty.contains("nix")) { 
	       return "linux"; 
	   } 
	   else { 
	       throw new RuntimeException("Unknown OS name: " + osNameProperty);
	   }
	} 

	/**
	 * Gets the Architecture name from the current system
	 * @return osArch
	 */
	private static String getArchName() { 
	   String osArch = System.getProperty("os.arch");
	   return (osArch != null && osArch.contains("64")) ? "64" : "32";
	}
	
	public static void addJarToClasspath(File jarFile) { 
	   try { 
	       URL url = jarFile.toURI().toURL(); 
	       URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader(); 
	       Class<?> urlClass = URLClassLoader.class; 
	       Method method = urlClass.getDeclaredMethod("addURL", new Class<?>[] { URL.class }); 
	       method.setAccessible(true);         
	       method.invoke(urlClassLoader, new Object[] { url });             
	   } 
	   catch (Throwable t) { 
	       t.printStackTrace(); 
	   } 
	}
	
	public static void addNativeToClasspath(String nativeFolder) {
		
	}
}
