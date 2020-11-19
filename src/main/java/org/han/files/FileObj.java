package org.han.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.han.internal.FileHandlingBase;

/**
 * A simple single threaded file-handling Library. Maps to
 * {@link org.han.internal.FileHandlingBase FileHandlingBase} internally
 * 
 * @author hanro
 */
public class FileObj extends FileHandlingBase {
	private static final long serialVersionUID = -5582601174144921228L;

	public FileObj(String ClassPath, String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(ClassPath, Path, FileName, FileExtention,FU);
	}

	public FileObj(String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(Path, FileName, FileExtention,FU);
	}

	public FileObj(File file,FIleUtil FU) {
		super(file,FU);
	}

	@Override
	public void write(String... in) throws IOException {
		super.write(in);
	}

	@Override
	public String[] read() throws FileNotFoundException, IOException {
		return super.read();
	}

	@Override
	public String read(String Seperator) throws FileNotFoundException, IOException {
		return super.read(Seperator);
	}

}
