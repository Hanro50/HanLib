package org.han.files;

import java.io.File;
import java.io.FileFilter;

import org.han.debug.Log;
import org.han.internal.FileHandlingBase;

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
	 * @param file      The given directory in the form of a file object
	 * @param extention the extension to filter by
	 * @return An array containing the file objects that reference the files within
	 *         this directory with the given extension.
	 */
	public File[] listFiles(String extention) {
		final String ext = "." + FIleUtil.SafeStr(extention);

		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(ext);
			}
		};
		return this.listFiles(Ff);
	}

	/**
	 * Returns an array of files objects that reference files within this directory.
	 * 
	 * @return An array of files objects that reference files within this directory.
	 */
	public File[] listFiles() {

		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return true;
			}
		};
		return this.listFiles(Ff);
	}

	/**
	 * Returns a string array containing the files' file names that have the given
	 * extension within this directory.
	 * 
	 * @param extention the extension to filter by
	 * 
	 * @return A string array containing the files' file names that have the given
	 *         extension within this directory.
	 */
	public String[] listFilesString(String extention) {
		File[] FL = listFiles(extention);
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
		String out = this.getAbsolutePath() + ":";
		String[] SA = listFilesString();
		if (SA.length < 1)
			out += "This directory is empty";
		else
			for (String entry : this.listFilesString()) {
				out += "\n" + entry;
			}
		return out;
	}

	public boolean filechk(){
		Log.rep("Checking: " + this.getAbsolutePath());
		if (!this.exists()) {
			this.mkdir();
			Log.rep("Created: " + this.getAbsolutePath());
			return true;
		}
		return false;
	}

}
