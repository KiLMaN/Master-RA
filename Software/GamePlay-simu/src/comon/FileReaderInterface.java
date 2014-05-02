package comon;

import java.io.InputStream;

/* The file Reading ONLY */
public interface FileReaderInterface {

	/* Set the fileName to open */
	public void setFile(String fileName);

	/* Get The Output Stream */
	public InputStream getStream();

	/* Close the file */
	public void close();

}
