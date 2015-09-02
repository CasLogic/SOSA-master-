package edu.gsu.psych.sosa.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.ardor3d.image.Image;
import com.ardor3d.image.util.ImageLoaderUtil;
import com.ardor3d.util.resource.ResourceSource;

public class SOSATexture implements Serializable{

	private static final long serialVersionUID = 2735340484306856748L;
	private String originalPath;
	private byte[] texture;
	
	private transient File _file;

	/**
	 * sets the texture original path to the string path
	 * @param path
	 */
	public SOSATexture(String path) {
		originalPath = path;
	}
	/**
	 * sets _file to the file input and sets the texture original path
	 * to the absolute path of the file and passes itself to sosa file handler
	 * @param file
	 * @throws IOException
	 */
	public SOSATexture(File file) throws IOException {
		_file = file;
		originalPath = file.getAbsolutePath();
		SOSAFileHandler.readTextureFile(this);
	}
	/**
	 * sets texture equal to the byte[] input
	 * @param bytes
	 * @return SOSATexture
	 */
	public SOSATexture setBytes(byte[] bytes) {
		texture = bytes;
		return this;
	}
	/**
	 * gets the original path of the texture
	 * @return String originalPath
	 */
	public String getPath() {
		return originalPath;
	}
	/**
	 * gets the parent path of the texture file
	 * @return String getFile().getParent()
	 */
	public String getParentPath() {
		return getFile().getParent();
	}
	/**
	 * gets the file name of the texture file
	 * @return getFile().getName()
	 */
	public String getFileName() {
		return getFile().getName();
	}
	/**
	 * if file is null create a new file from the original path
	 * @return File _file
	 */
	private File getFile() {
		if(_file == null) {
			_file = new File(originalPath);
		}
		return _file;		
	}
	/**
	 * An image of the texture
	 * @param flipped
	 * @return Image
	 */
	public Image getArdorImage(boolean flipped) {
		return ImageLoaderUtil.loadImage(getType(), getStream(), flipped);		
	}
	/**
	 * gets the file type by getting the last “.” and getting the substring of the
	 * file name after it
	 * @return String type
	 */
	private String getType() {
		final int dot = originalPath.lastIndexOf('.');
		String type;
		if (dot >= 0) {
			type = originalPath.substring(dot);
		} else {
			type = ResourceSource.UNKNOWN_TYPE;
		}
		return type;
	}
	/**
	 * a byte array input stream of the texture
	 * @return InputStream
	 */
	private InputStream getStream(){
		return new ByteArrayInputStream(texture);
	}

}
