package simulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import comon.FileReaderInterface;

public class FileReaderPC implements FileReaderInterface {

	private String _fileName;
	private boolean _opened;
	private FileInputStream _file;

	public FileReaderPC() {
		this._fileName = "default.txt";
		this._opened = false;
	}

	@Override
	public void setFile(String fileName) {
		this._fileName = fileName;
	}

	@Override
	public FileInputStream getStream() {
		if (this._opened)
			return this._file;
		try {
			this._file = new FileInputStream(this._fileName);
			if (this._file != null)
				this._opened = true;
			return this._file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		if (this._opened) {
			try {
				this._file.close();
				this._opened = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
