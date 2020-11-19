package org.han.files;

import static org.han.files.FIleUtil.GsonInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.han.debug.Log;
import org.han.internal.FileHandlingBase;

import com.google.gson.JsonSyntaxException;
/**
 * Replaced by {@link org.han.files.JsonObj JsonObj}
 * @author hanro
 *
 * @param <Self>
 */
@Deprecated 
public abstract class ClassObj<Self extends ClassObj<Self>> extends FileHandlingBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4724335877626808520L;

	public ClassObj(String ClassPath, String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(ClassPath, Path, FileName, FileExtention,FU);
	}

	public ClassObj(String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(Path, FileName, FileExtention,FU);
	}

	public ClassObj(File file,FIleUtil FU) {
		super(file,FU);
	}

	public abstract Self Constructor(FileHandlingBase file);

	/**
	 * This is a factory method to create objects that inherent from the class
	 * {@link org.han.internal.ClassObj_old ClassObj}. It is preferred that you use
	 * this function instead of the class's default constructor. However calling the
	 * default constructor and then {@link #loadSelf() loadFromFile()} will have a
	 * similar effect. Although this method in theory is more efficient.
	 * 
	 * @param <Result>      The resulting class type. This is a static method so it
	 *                      won't automatically know what class you're talking about
	 * @param ClassOfResult The resulting class type that is being loaded.
	 * @param file          The file the class data should be loaded from. </br>
	 *                      <i> This will override the file variable set by the
	 *                      constructor. </i>
	 * @return A fully initialized class
	 * @throws JsonSyntaxException   The file is formated incorrectly
	 * @throws FileNotFoundException The file does not exist. (If this is thrown,
	 *                               please use the class's normal
	 *                               {@link #ClassObj(FileObj) constructor})
	 * @throws IOException           The file could not be read.
	 * 
	 * 
	 * 
	 * @see #load(ClassObj_old) for a method that handles file not found exceptions
	 *      better
	 * @see #load() for a method that can be called from an instance of
	 *      {@link org.han.files.ClassObj ClassObj}
	 */

	public static <Result extends ClassObj<Result>> Result constructor(Class<Result> ClassOfResult, FileObj file)
			throws JsonSyntaxException, FileNotFoundException, IOException {
		Result R = GsonInstance.fromJson(file.read(""), ClassOfResult);
		return R.Constructor(file);
	}



	/**
	 * 
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException 
	 * @throws InstantiationException This should never be thrown unless something serious has gone wrong
	 */
	@SuppressWarnings("unchecked")
	public Self load() throws JsonSyntaxException, IOException, InstantiationException {
		try {
			return (Self) ClassObj.constructor(this.getClass(), new FileObj(this,FU));
		} catch (ClassCastException e) {
			Log.trace(e);
			Log.err("CRITICAL INTERNAL EXCEPTION: This should be IMPOSSIBLE to reach. Please report the stack trace before this method to the developer");
			throw new InstantiationException(
					"This should be IMPOSSIBLE to reach. Please report the stack trace before this method to the developer");
		}
	}

	

	/**
	 * Save this class's class data to file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		this.write(GsonInstance.toJson(this));

	}

}
