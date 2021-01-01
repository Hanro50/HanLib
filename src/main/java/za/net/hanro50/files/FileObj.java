package za.net.hanro50.files;

import java.io.File;
import java.io.IOException;

import za.net.hanro50.internal.FileHandlingBase;

/**
 * A simple single threaded file-handling Library. Maps to
 * {@link za.net.hanro50.internal.FileHandlingBase FileHandlingBase} internally
 * 
 * @author hanro
 */
public class FileObj extends FileHandlingBase {
	private static final long serialVersionUID = -5582601174144921228L;

	public FileObj(String ClassPath, String Path, String FileName, String FileExtension,FIleUtil FU) {
		super(ClassPath, Path, FileName, FileExtension,FU);
	}

	public FileObj(String Path, String FileName, String FileExtension,FIleUtil FU) {
		super(Path, FileName, FileExtension,FU);
	}

	public FileObj(File file,FIleUtil FU) {
		super(file,FU);
	}

	@Override
	public void write(String... in) throws IOException {
		super.write(in);
	}

	@Override
	public String[] read() throws IOException {
		return super.read();
	}

	@Override
	public String read(String Separator) throws IOException {
		return super.read(Separator);
	}

}
