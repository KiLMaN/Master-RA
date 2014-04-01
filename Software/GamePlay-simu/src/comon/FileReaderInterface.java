package comon;

import java.io.FileInputStream;

/* The file Reading ONLY */
public interface FileReaderInterface {

	/* Set the fileName to open */
	public void setFile(String fileName);

	/* Get The Output Stream */
	public FileInputStream getStream();

	/* Close the file */
	public void close();

}
