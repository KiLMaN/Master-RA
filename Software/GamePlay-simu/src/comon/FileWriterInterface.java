package comon;

import java.io.OutputStream;

/* The file Writing ONLY */
public interface FileWriterInterface {

	/* Set the fileName to open */
	public void setFile(String fileName);

	/* Get The Output Stream */
	public OutputStream getStream();

	/* Close the file */
	public void close();

}
