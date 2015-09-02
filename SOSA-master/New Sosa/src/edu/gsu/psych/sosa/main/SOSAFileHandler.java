package edu.gsu.psych.sosa.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.gsu.psych.sosa.experiment.Experiment;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;

public class SOSAFileHandler {
	/**
	 * creates a file output stream and an object output stream from that,
	 * then uses the object output stream to write the sosa object,
	 * then closes the object output stream
	 * @param sosaObj
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(Serializable sosaObj, String path) throws IOException{
		if(path != null){
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sosaObj);
			oos.close();
		}
	}
	/**
	 * creates a file writer and writes the output string to that file
	 * @param output
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(String output, String path) throws IOException{
		if(path != null){
			FileWriter fw = new FileWriter(path);
			fw.write(output);
			fw.close();
		}			
	}
	/**
	 * creates a file input stream and an object input stream from that,
	 * then creates an output object  and closes the object input stream
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static Object readFile(String path) throws IOException, ClassNotFoundException{
		if(path != null){
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object output = ois.readObject();
			ois.close();
			return output;
		}
		return null;
	}
	/**
	 * calls readfile on the path and casts it to a Stimulus object
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Stimulus readStimFile(String path) throws IOException, ClassNotFoundException{
		return (Stimulus)readFile(path);
	}
	/**
	 * calls readfile on the path and casts it to an experiment object
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Experiment readExperimentFile(String path) throws IOException, ClassNotFoundException{
		return (Experiment)readFile(path);
	}
	/**
	 * creates a new SOSATexture using the path and calls the overrode readTextureFile() on it
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static SOSATexture readTextureFile(String path) throws IOException{
		SOSATexture output = new SOSATexture(path);
		return readTextureFile(output);
	}
	/**
	 * reads in the input file and gets a byte array for it, then reads the file and closes it
	 * @param output
	 * @return
	 * @throws IOException
	 */
	public static SOSATexture readTextureFile(SOSATexture output) throws IOException{
		File inputFile = new File(output.getPath());
		byte[] file = new byte[(int) inputFile.length()];
		FileInputStream fis = new FileInputStream(inputFile);
		fis.read(file);
		fis.close();
		
		return output.setBytes(file);
	}
}
