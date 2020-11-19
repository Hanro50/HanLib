package org.han.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.han.Misc;
import org.han.debug.Log;
import org.han.debug.Log_Ignore;
import org.han.types.FileUtilConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Log_Ignore()
public class FIleUtil {
	/**
	 * The Gson object used to generate and read json data. See
	 * {@link org.han.ylib.filehandling.ClassObj ClassObj}
	 */

	public static final Gson GsonInstance = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
			.create();

	public static final Gson GsonInstanceAll = new GsonBuilder().setPrettyPrinting().create();
	String DefaultClassPath;

	public FIleUtil(File dir) {
		DefaultClassPath = dir.getAbsolutePath() + "/";
		FileChk("");
	}

	public FIleUtil() {
		try {

			String line = readJar("FileUtil.config", "");
			try {
				Object object = Misc.Construct(Class.forName(line));
				if (object instanceof FileUtilConfig) {
					DefaultClassPath = ((FileUtilConfig) object).getClassPath().getAbsolutePath() + "/";
					FileChk("");
					Log.out("Default class path: " + DefaultClassPath);
					return;
				}
				Log.err("Warning " + object.getClass().getName() + " is not an instance of "
						+ FileUtilConfig.class.getName());

			} catch (ClassNotFoundException e) {
				Log.err(line + "<-Does not point to a class");
			}
		} catch (RuntimeException | IOException r) {
			Log.wrn("Could not read internal settings document, let's try getting the Default Class Path manually");
		}
		Log.pek(System.getProperty("sun.java.command"));
		try {
			String value = null;
			for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
				if (entry.getKey().startsWith("JAVA_MAIN_CLASS")) {
					value = entry.getValue();
					break;
				}
			}
			if (value == null) {
				if (Thread.currentThread().getId() != 1)
					Log.err("This platform does not support dynamic main class locating, invoke the class FIleUtil on main thread");
				value = Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1]
						.getClassName();
			}
			try {
				DefaultClassPath = new File(
						Class.forName(value).getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
								.getParentFile().getPath()
						+ "/data/";

			} catch (RuntimeException | ClassNotFoundException e) {
				DefaultClassPath = new File(
						FileObj.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
								.getParentFile().getPath()
						+ "/data/";
				Log.trace(e);
			}

			// }
			Log.out("Default class path: " + DefaultClassPath);
			FileChk("");
		} catch (URISyntaxException e) {
			DefaultClassPath = "";
			Log.trace(e);
		}
	}

	public String getDefaultClassPath() {
		return DefaultClassPath;
	}

	public String readJar(String toResource, String splitter) throws IOException {
		return readJar(this.getClass().getClassLoader(), toResource, splitter);
	}

	public String[] readJar(String toResource) throws IOException {
		return readJar(this.getClass().getClassLoader(), toResource);
	}

	public static String readJar(ClassLoader CL, String toResource, String splitter) throws IOException {
		String out = "";
		for (String line : readJar(CL, toResource)) {
			out += splitter + line;
		}
		return out.substring(splitter.length());
	}

	public static String[] readJar(ClassLoader CL, String toResource) throws IOException {
		return readStream(CL.getResourceAsStream(toResource));
	}

	public static String[] readStream(InputStream Stream) throws IOException {

		BufferedReader buffReader = new BufferedReader(new InputStreamReader(Stream));

		List<String> lines = new ArrayList<String>();
		String T2 = buffReader.readLine();
		while (T2 != null) {
			lines.add(T2);
			T2 = buffReader.readLine();
		}
		buffReader.close();
		String[] result = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			result[i] = lines.get(i);
		}

		return result;

	}

	/**
	 * Checks if a file exists within the {@link #ClassPath ClassPath} folder. </br>
	 * 
	 * @see {@link #FileChkroot(String) FileChkroot(String Path)} for a version of
	 *      this that doesn't use the path stored in {@link #FileChkroot(String)
	 *      FileChkroot(String Path)}
	 * @param Path The Path of the file you want to check relative to
	 *             {@link #ClassPath the path given here}
	 * 
	 * @implNote Doesn't check more then one level. If you want to check "F/C/".
	 *           Check F/ and then F/C/. This is mostly just here to make discourage
	 *           deeper directory listings then that which is required
	 * 
	 */
	public void FileChk(String Path) {
		FileChkroot(DefaultClassPath + Path);
	}

	/**
	 * Checks if a file exists within the {@link #Root Root} folder. </br>
	 * 
	 * @see {@link #FileChkroot(String) FileChkroot(String Path)} for a version of
	 *      this that uses the path stored in {@link #Root Root} folder (That
	 *      contains the jar file)
	 * @param Path The Path of the file you want to check relative to {@link #Root
	 *             the path given here}
	 * 
	 * @implNote Doesn't check more then one level. If you want to check "F/C/".
	 *           Check F/ and then F/C/. This is mostly just here to make discourage
	 *           deeper directory listings then that which is required
	 */
	public void FileChkroot(String Path) {
		try {
			File F = new File(Path);
			Log.rep("Checking: " + F.getAbsolutePath());
			if (!F.exists())
				F.mkdir();
		} catch (Throwable e) {
			Log.trace(e);
		}
	}

	/**
	 * Gets called to make sure the files being saved are save to store.
	 * 
	 * @param raw The raw name string
	 * @return a wrapped version of the previously mentioned name string
	 */
	public static String SafeStr(String raw) {
		return raw.trim().replaceAll("\\.", "").replaceAll("[^a-zA-Z0-9\\._]+", "_").toLowerCase();
	}

	/**
	 * Returns a list of files with the given extension and path. With reference to
	 * the {@link #DefaultClassPath} variable as the root file.
	 * 
	 * @param path      The path to the file
	 * @param extention The extension of the files you're looking for
	 * @return A list of file objects
	 * @see org.han.files.DirObj DirObj
	 */
	public File[] listFiles(String path, String extention) {
		DirObj F = new DirObj(path, this);
		return F.listFiles(extention);
	}

	/**
	 * Returns a list of files within a given path. With reference to the
	 * {@link #DefaultClassPath} variable as the root file.
	 * 
	 * @param path      The path to the file
	 * @param extention The extension of the files you're looking for
	 * @return A list of file objects
	 * @see org.han.files.DirObj DirObj
	 */
	public File[] listFiles(String path) {
		DirObj F = new DirObj(path, this);
		return F.listFiles();
	}

	public String[] FileListString(String path, String extention) {
		extention = SafeStr(extention);
		File[] FL = listFiles(path, extention);
		return listFilesString(FL);
	}

	/**
	 * Returns a list of files within a given path. With reference to the
	 * {@link #DefaultClassPath} variable as the root file.
	 * 
	 * @param path      The path to the file
	 * @param extention The extension of the files you're looking for
	 * @return A list of Strings containing the names of the files in a directory
	 * @see org.han.files.DirObj DirObj
	 */
	public String[] listFilesString(String path) {
		File[] FL = listFiles(path);
		return listFilesString(FL);
	}

	/**
	 * Converts an array of files into a string array containing the names of the
	 * file
	 * 
	 * @param FileList
	 * @return
	 */
	public String[] listFilesString(File[] FileList) {

		if (FileList == null || FileList.length == 0) {
			return new String[] { "No files found" };
		}
		String[] R = new String[FileList.length];

		for (int i = 0; i < FileList.length; i++) {
			String T = FileList[i].getName();

			R[i] = T;
		}
		return R;
	}

	public FileObj getFileObj(String ClassPath, String Path, String FileName, String FileExtention) {
		return new FileObj(ClassPath, Path, FileName, FileExtention, this);
	}

	public FileObj getFileObj(String Path, String FileName, String FileExtention) {
		return new FileObj(Path, FileName, FileExtention, this);
	}

	public FileObj getFileObj(File file) {
		return new FileObj(file, this);
	}

	public DirObj getDirObj(String Path, String FileName) {
		return new DirObj(Path, FileName, this);
	}

	public DirObj getDirObj(String Path) {
		return new DirObj(Path, this);
	}

	public DirObj getDirObj(File Path) {
		return new DirObj(Path, this);
	}

}
