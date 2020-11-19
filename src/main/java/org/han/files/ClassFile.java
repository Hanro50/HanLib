package org.han.files;

import static org.han.files.FIleUtil.GsonInstance;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.han.debug.Log;
import org.han.internal.FileHandlingBase;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonSyntaxException;

/**
 * Similar to {@link org.han.ylib.filehandling.ClassObj ClassObj}, but designed
 * as a wrapper for existing objects. This class is potentially less intrusive
 * and should generally be much safer to use security wise, however it is
 * requires static methods or a 3rd party to manage.
 * 
 * @author hanro
 * @param <Type> The type of the class that this object should wrap.
 */
public class ClassFile<Type extends Object> extends FileHandlingBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7324748664280475046L;
	//final FileObj file;
	private Type WrappedObject;
	final boolean StoreType;

	/**
	 * Use this constructor if you don't care about type safety and plan on loading
	 * the class later on. If you do care about the former check
	 * {@link #ClassFile(FileObj, boolean) ClassFile(FileObj, boolean)}.
	 * 
	 * This will attempt to load the object from the provided object file when used.
	 * If you don't want this behavior. Please reference
	 * {@link #ClassFile(FileObj, Type, boolean) ClassFile(FileObj, Type, boolean)}
	 * 
	 * @param file The file object that data should be loaded and saved to
	 * @throws JsonSyntaxException If set object carries invalid syntax.
	 */
	public ClassFile(FileObj file, FIleUtil FU) throws JsonSyntaxException {
		this(file, false,FU);
	}

	/**
	 * Use this constructor if you do care about type safety and plan on loading the
	 * class later on. If you don't care about the former check
	 * {@link #ClassFile(FileObj) ClassFile(FileObj)}.
	 * 
	 * This will attempt to load the object from the provided file object when used
	 * If you don't want this behavior. Please reference
	 * {@link #ClassFile(FileObj, Type) ClassFile(FileObj, Type)}
	 * 
	 * @param file
	 * @param StoreType
	 * @throws JsonSyntaxException
	 */
	public ClassFile(FileObj file, boolean StoreType,FIleUtil FU) throws JsonSyntaxException {
		this(file, null, StoreType,FU);
		try {
			load();
		} catch (IOException e) {
			Log.wrn("Could not load from file. Please use the 'setObject' method.");
		}
	}

	/**
	 * The default constructor. If you're working with a class that potentially use
	 * the contained class as a base. Please check out
	 * {@link #ClassFile(FileObj, Type, boolean)}
	 * 
	 * @param file   The file that the object data should be loaded and saved from.
	 * @param object The Object you wish to wrap
	 */
	public ClassFile(FileObj file, Type object,FIleUtil FU) {
		this(file, object, false,FU);
	}

	/**
	 * The default constructor, with an option for type checking. If you don't want
	 * to do any type checking then please see {@link #ClassFile(FileObj, Type)
	 * ClassFile(FileObj, Type)}.
	 * 
	 * @param file      The file that the object data should be loaded and saved
	 *                  from.
	 * @param object    The Object you wish to wrap
	 * @param StoreType Should the object type be saved as well?
	 */
	public ClassFile(FileObj file, Type object, boolean StoreType,FIleUtil FU) {
		super (file,FU);
		this.WrappedObject = object;
		this.StoreType = StoreType;
	}

	/**
	 * Loads object date from file if the file can be found. This will replace the
	 * internally stored object with the one loaded from file. Will do nothing if no
	 * file exists to load from.
	 * 
	 * @throws JsonSyntaxException The file you're loading from incorrectly formated
	 * @throws IOException         The file exists, but it could not be read.
	 */
	public void load() throws JsonSyntaxException, IOException {
		if (this.exists()) {
			try {
				if (!StoreType) {
					WrappedObject = decode(this.read(""), WrappedObject.getClass());
					return;
				} else {
					String[] Data = this.read("\n").split("\n", 2);
					try {
						Class<?> ClassVar = Class.forName(Data[0]);
						WrappedObject = decode(Data[1], ClassVar);
						return;
					} catch (ClassNotFoundException e) {
						Log.wrn("Could not find Class indicated by the data stored in the file.");
					}
				}
			} catch (FileNotFoundException e) {
			}
		} else {
			Log.wrn("No file found.");
		}
	}

	/**
	 * Save the Wrapped object stored within this class's data to file.</br>
	 * </br>
	 * <b>Warning:</b> if said object is equal to null, then nothing is saved
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		if (WrappedObject != null) {
			if (!StoreType)
				this.write(GsonInstance.toJson(getObject()));
			else {
				this.write(new String[] { getObject().getClass().getName(), GsonInstance.toJson(getObject()) });
			}
		} else {
			Log.wrn("No data to save, the currently stored wrapped object is null");
		}
	}

	/**
	 * An internal method that is used when reading the class from file.
	 * 
	 * @param Json     The class's data in text form
	 * @param ClassVar The type data, mostly used when type checking is enabled
	 * @return An initialised version of the internal class.
	 */
	private Type decode(String Json, Class<?> ClassVar) {
		return GsonInstance.fromJson(Json, ClassVar.asSubclass(this.getClass()));
	}

	/**
	 * @return Returns the stored object within this class.
	 */
	public Type getObject() {
		return WrappedObject;
	}

	/**
	 * Replaces the internal stored object. Will also generate a new file if non
	 * exists.
	 * 
	 * @param Object a new instance of the object.
	 */
	public void setObject(@NotNull Type Object) {
		WrappedObject = Object;
		try {
			if (!this.exists()) {
				save();
			}
		} catch (IOException e) {
			Log.err("Internal IO Exception, not important enough to warrent investigation");
		}
	}
}
