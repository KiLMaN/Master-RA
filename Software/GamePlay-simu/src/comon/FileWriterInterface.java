package comon;

import java.io.OutputStream;

/**
 * A standard file writer interface that can be used on Android and Windows
 * 
 * @author Brice
 * 
 */
public interface FileWriterInterface {

	/**
	 * Set the name of the file to open the stream from
	 * 
	 * @param fileName
	 *            : name of the file ex : "folder/file.ext"
	 */
	public void setFile(String fileName);

	/**
	 * Get the output stream, the file must be set first
	 * 
	 * @return A standard inputstream
	 */
	public OutputStream getStream();

	/**
	 * Close the input stream
	 */
	public void close();

}
