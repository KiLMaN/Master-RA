package comon;

import java.io.FileOutputStream;

/* The file Writing ONLY */
public interface FileWriterInterface {

	/* Set the fileName to open */
	public void setFile(String fileName);

	/* Get The Output Stream */
	public FileOutputStream getStream();

	/* Close the file */
	public void close();

}
