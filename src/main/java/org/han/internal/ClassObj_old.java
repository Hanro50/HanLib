package org.han.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.han.Misc;
import org.han.files.FIleUtil;
import org.han.files.FileObj;

import com.google.gson.JsonSyntaxException;

import static org.han.files.FIleUtil.*;

/**
 * OLD Class Object. This class is non functional, but is here for documentation transfer
 * 
 * @author hanro
 */
@Deprecated
public abstract class ClassObj_old<Self extends ClassObj_old<?>> extends FileHandlingBase {
	protected ClassObj_old(String ClassPath, String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(ClassPath, Path, FileName, FileExtention,FU);
		// TODO Auto-generated constructor stub
	}

	protected ClassObj_old(String Path, String FileName, String FileExtention,FIleUtil FU) {
		super(Path, FileName, FileName,FU);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5359060105208655183L;

	/**
	 * This is a factory method to create objects that inherent from the class
	 * {@link org.han.internal.ClassObj_old ClassObj}. It is preferred that you use this
	 * function instead of the class's default constructor. However calling the
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
	 *                               {@link #ClassObj_old(FileObj) constructor})
	 * @throws IOException           The file could not be read.
	 * 
	 * @see #load(ClassObj_old) for a method that handles file not found exceptions
	 *      better
	 * @see #load() for a method that can be called from an instance of
	 *      {@link org.han.internal.ClassObj_old ClassObj}
	 */
	public static <Result extends ClassObj_old<?>> Result constructor(Class<Result> ClassOfResult, FileObj file)
			throws JsonSyntaxException, FileNotFoundException, IOException {
		Result R = GsonInstance.fromJson(file.read(""), ClassOfResult);
		//R = file;
		return R;
	}

	public abstract Self Construct(FileObj file);
	
	
	/**
	 * Loads an object from an unloaded instance of itself. This uses a similar
	 * method to {@link #constructor(Class, FileObj) constructor(Class, FileObj)}
	 * rather then the more sketchy {@link #loadSelf() loadSelf()} method.
	 * 
	 * @see #load() for a non static version of this method
	 * @see #constructor(Class, FileObj) for a simple factory method for creating
	 *      instances of {@link org.han.internal.ClassObj_old ClassObj}
	 * @param <Result>
	 * @param baseObjInstance
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */

	//public static <Result extends ClassObj_old> Result load(Result baseObjInstance)
	//		throws JsonSyntaxException, IOException {
		//try {
		//	return baseObjInstance.load();
		//} catch (FileNotFoundException e) {
	//		Log.err("No file to load from");
		//	 catch (ClassCastException e) {
	//		Log.err("Could not load file as it is a different type then what was expected");
	///	}
//baseObjInstance.save();
///		}
	//	return baseObjInstance;
	//}


	public boolean can_access(Field field) {
		return Misc.can_access(field, this);
	}

}
/*
 * /** The default constructor. Please only use this as a fallback when {@link
 * #constructor(Class, FileObj) constructor(Class, FileObj)} method fails to
 * find an object. Otherwise call this followed by {@link #loadSelf()
 * loadFromFile()} to load this class.
 * 
 * Will generate a file if non exists with the default file object.
 * 
 * @throws IOException
 * 
 * protected ClassObj(FileObj file) throws IOException { this.file = file; if
 * (!file.exists()) { save(); } }
 */
