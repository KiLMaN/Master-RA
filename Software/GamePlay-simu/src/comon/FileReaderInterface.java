package comon;

import java.io.InputStream;

/**
 * A standard file reader interface that can be used on Android and Windows
 * 
 * @author Brice
 * 
 */
public interface FileReaderInterface {

	/**
	 * Set the name of the file to open the stream from
	 * 
	 * @param fileName
	 *            : name of the file ex : "folder/file.ext"
	 */
	public void setFile(String fileName);

	/**
	 * Get the input stream, the file must be set first
	 * 
	 * @return A standard inputstream
	 */
	public InputStream getStream();

	/**
	 * Close the input stream
	 */
	public void close();

}
