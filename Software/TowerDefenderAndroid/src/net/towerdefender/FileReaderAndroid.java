package net.towerdefender;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;

import comon.FileReaderInterface;

public class FileReaderAndroid implements FileReaderInterface {

	private String _fileName;
	private boolean _opened;
	private InputStream _file;
	private Activity context;

	public FileReaderAndroid(Activity context) {
		this._fileName = "default";
		this._opened = false;
		setContext(context);
	}

	@Override
	public void setFile(String fileName) {
		this._fileName = fileName.substring(0, fileName.lastIndexOf('.'));
	}

	@Override
	public InputStream getStream() {

		if (this._opened)
			return this._file;
		try {
			_file = context.getResources().openRawResource(
					context.getResources().getIdentifier("raw/" + _fileName,
							"raw", context.getPackageName()));
			// this._file = new FileInputStream(this._fileName);
			if (this._file != null)
				this._opened = true;
			return this._file;
		} catch (Exception e) {
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

	public Activity getContext() {
		return context;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

}
