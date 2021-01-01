package za.net.hanro50.files;

import java.io.File;
import java.io.FileFilter;

import za.net.hanro50.debug.Log;
import za.net.hanro50.internal.FileHandlingBase;

public class DirObj extends FileHandlingBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -380279491373912019L;

	public DirObj(String ClassPath, String Path, FIleUtil FU) {
		super(ClassPath, Path, "", "",FU);

	}

	public DirObj(String Path,FIleUtil FU) {
		super(Path, "", "",FU);

	}

	public DirObj(File file,FIleUtil FU) {
		super(file,FU);

	}

	/**
	 * Returns an array containing the file objects that reference the files within
	 * this directory with the given extension.
	 * 
	 * @param extension the extension to filter by
	 * @return An array containing the file objects that reference the files within
	 *         this directory with the given extension.
	 */
	public File[] listFiles(String extension) {
		final String ext = "." + FIleUtil.SafeStr(extension);

		FileFilter Ff = f -> f.getName().endsWith(ext);
		return this.listFiles(Ff);
	}

	/**
	 * Returns an array of files objects that reference files within this directory.
	 * 
	 * @return An array of files objects that reference files within this directory.
	 */
	public File[] listFiles() {

		FileFilter Ff = f -> true;
		return this.listFiles(Ff);
	}

	/**
	 * Returns a string array containing the files' file names that have the given
	 * extension within this directory.
	 * 
	 * @param extension the extension to filter by
	 * 
	 * @return A string array containing the files' file names that have the given
	 *         extension within this directory.
	 */
	public String[] listFilesString(String extension) {
		File[] FL = listFiles(extension);
		return FU.listFilesString(FL);
	}

	/**
	 * Returns a string array containing the files' file names that are within this
	 * directory.
	 * 
	 * @return A string array containing the files' file names that are within this
	 *         directory.
	 */
	public String[] listFilesString() {
		File[] FL = listFiles();
		return FU.listFilesString(FL);
	}

	/**
	 * Calls {@link #listFilesString()} and converts it into a string.
	 */
	public String toString() {
		StringBuilder out = new StringBuilder(this.getAbsolutePath() + ":");
		String[] SA = listFilesString();
		if (SA.length < 1)
			out.append("This directory is empty");
		else
			for (String entry : this.listFilesString()) {
				out.append("\n").append(entry);
			}
		return out.toString();
	}

	public boolean filechk(){
		Log.rep("Checking: " + this.getAbsolutePath());
		if (!this.exists()) {
			if (this.mkdir()){
				Log.rep("Created: " + this.getAbsolutePath());
			return true;
		}else{
				Log.rep("Failed to create file");
			return false;}
		}
		return false;
	}
	
	public boolean deletePath() {
		return FIleUtil.deletePath(this);
	}
	
	

	

		
	

}
