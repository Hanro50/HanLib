package za.net.hanro50.files;

import static za.net.hanro50.files.FIleUtil.GsonInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.gson.JsonSyntaxException;

import za.net.hanro50.Misc;
import za.net.hanro50.debug.Log;
import za.net.hanro50.debug.Log_Ignore;
import za.net.hanro50.internal.FileHandlingBase;

@Log_Ignore()
public abstract class JsonObj extends FileHandlingBase {
	private static final long serialVersionUID = 1L;
	@Ignore
	private boolean loaded;

	/**
	 * Tell the loader about fields it should ignore
	 * 
	 * @author hanro
	 *
	 */
	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	protected @interface Ignore {
	}

	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public JsonObj(String ClassPath, String Path, String FileName, String FileExtension,FIleUtil FU) {
		super(ClassPath, Path, FileName, FileExtension,FU);
		loaded = false;
	}

	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public JsonObj(String Path, String FileName, String FileExtension,FIleUtil FU) {
		super(Path, FileName, FileExtension,FU);
		loaded = false;
	}

	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public JsonObj(File file,FIleUtil FU) {
		super(file,FU);
		loaded = false;
		if (!this.exists())
			save();
	}

	/**
	 * Get a json version of this object
	 */
	public String toJson() {
		return FIleUtil.GsonInstance.toJson(this);
	}

	/**
	 * Loads data from a provided json text string into this object
	 * 
	 * @param json the json text string
	 * @return true if the data was loaded successfully.
	 * @throws JsonSyntaxException if the provided json text string is invalid
	 */
	public boolean load(String json) throws JsonSyntaxException {
		try {
			JsonObj RawData = GsonInstance.fromJson(json, this.getClass());
			Log.pek(RawData.getClass().getName());
			for (Field field : RawData.getClass().getDeclaredFields()) {
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				boolean access = Misc.can_access(field, this);
				field.setAccessible(true);
				try {
					Object data = field.get(RawData);
					if (data != null && !field.isAnnotationPresent(Ignore.class)) {
						field.set(this, data);
						Log.pek(">Loading:" + field.getName() + "(Value:" + field.get(RawData) + ")");
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Log.trace(e);
				}
				field.setAccessible(access);
			}
			loaded = true;
			return true;
		} catch (ClassCastException e) {
			Log.err("Failed to load data");
			Log.trace(e);
		}
		return false;
	}

	/**
	 * Load object data form this object's file. Can be used to refresh data if the
	 * data in the file has changed. In essence just a binder for
	 * {@linkplain #load(String)} using the data this file object points to as a
	 * base
	 * 
	 * @throws IOException           Could not read file
	 * @throws FileNotFoundException Could not find file
	 * @return returns true if the data was loaded successfully.
	 */
	public boolean load() throws IOException {
		if (this.exists())
			return load(this.read(""));
		else {
			Log.wrn("Could not find:" + this.getAbsolutePath());
			loaded = true;
			return true;
		}
	}

	/**
	 * Load and save the data provided by the given json string
	 *
	 */

	public void save(String json) {
		save(json, true);
	}

	/**
	 * Convert this object's data to json and then write that data to file
	 */

	public void save() {
		save(toJson(), false);
	}

	/**
	 * Save a given json string to file.
	 * 
	 * @param json     The json String
	 * @param load_self Whether or not the set string's data should be loaded into
	 *                 this object before saving it. See {@link #load(String)} for
	 *                 more details
	 */

	public void save(String json, boolean load_self) {
		if (load_self) {
			load(json);
		}
		try {
			this.write(json);
			loaded = true;
		} catch (IOException e) {
			Log.trace(e);
		}
	}

	/**
	 * Checks if this object is considered loaded. Run
	 * {@link #load()},{@link #save()}, {@link #load(String)} ,
	 * {@link #save(String)} or {@link #makeLoaded()} to flip this to true
	 * 
	 * @return true if the object is considered loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Checks if this object is loaded and loads it if it isn't with
	 * {@link #load()}. If that fails it's assumed the base file is corrupted. This
	 * will then force this file to consider itself loaded
	 * 
	 */
	public void makeLoaded() {
		try {
			if (!isLoaded()) {
				this.load();
			}
		} catch (IOException e) {
			Log.trace(e);
		}
		loaded = true;
	}

}
