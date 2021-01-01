package za.net.hanro50.files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import za.net.hanro50.Misc;
import za.net.hanro50.debug.Log;
import za.net.hanro50.internal.FileHandlingBase;

public class ConfigObj extends FileHandlingBase {
	private static final long serialVersionUID = 1L;
	boolean loaded = false;
	PropertiesConfiguration config = new PropertiesConfiguration();

	@Documented
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Option {
		String propertyName()

		default "";

		String propertyComment() default "";
	}
	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public ConfigObj(String ClassPath, String Path, String FileName, String File_Extension, FIleUtil FU) {
		super(ClassPath, Path, FileName, File_Extension, FU);
		if (this.exists()) {
			try {
				config.read(new FileReader(this));
			} catch (ConfigurationException | IOException e) {
				Log.trace(e);
			}
		}
	}

	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public ConfigObj(String Path, String FileName, String File_Extension, FIleUtil FU) {
		super(Path, FileName, File_Extension, FU);
		if (this.exists()) {
			try {
				config.read(new FileReader(this));
			} catch (ConfigurationException | IOException e) {
				Log.trace(e);
			}
		}
	}

	/**
	 * Do not forget to check if the file is loaded with {@link #isLoaded()} before
	 * modifying this object first
	 */
	public ConfigObj(File file, FIleUtil FU) {
		super(file, FU);
		if (this.exists()) {

			try {
				config.read(new FileReader(this));
			} catch (ConfigurationException | IOException e) {
				Log.trace(e);
			}
		}
	}

	public boolean load() {
		loaded = true;
		try {
			for (Field field : this.getClass().getDeclaredFields()) {
				Option option = field.getAnnotation(Option.class);
				if (Modifier.isFinal(field.getModifiers()) || option == null)
					continue;
				boolean access = Misc.can_access(field, this);
				field.setAccessible(true);
				try {
					String name = option.propertyName().trim().length() > 0 ? option.propertyName().trim()
							: field.getName();
					Object data = config.getProperty(name);
					if (data instanceof String) {
						String temp = ((String) data);
						if (temp.startsWith("\"") && temp.endsWith("\"") && temp.length() >= 2)
							data = temp.subSequence(1, ((String) data).length() - 1);
					}

					Log.out(data);
					if (data != null) {

						if (data instanceof String && !field.getType().equals(data.getClass())) {
							try {
								String out = ((String) data).trim();
								if (out.equalsIgnoreCase("{}")) {
									config.clearProperty(name);
									continue;
								}
								data = FIleUtil.GsonInstance.fromJson((String) data, field.getType());
							} catch (RuntimeException e) {
								Log.trace(e);
								continue;
							}
						}
						field.set(this, data);
					} else {
						backLoad(field);
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

	private void backLoad(Field field) {
		try {
			Option option = field.getAnnotation(Option.class);
			if (Modifier.isFinal(field.getModifiers()) || option == null)
				return;
			String name = option.propertyName().trim().length() > 0 ? option.propertyName().trim() : field.getName();
			if (field.get(this).getClass().isPrimitive()) {
				if (config.containsKey(name))
					config.clearProperty(name);
				config.addProperty(name, field.get(this));
			} else {
				String val = FIleUtil.GsonInstance.toJson(field.get(this)).replaceAll("\n", "");
				if (!val.trim().equalsIgnoreCase("{}"))
					if (config.containsKey(name))
						config.clearProperty(name);
				config.addProperty(name, val);
			}

			if (option.propertyComment().trim().length() > 0)
				config.getLayout().setComment(name, option.propertyComment());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Log.trace(e);
		}
	}

	public void save() {
		loaded = true;
		try {

			StringWriter writer2 = new StringWriter();
			config.write(writer2);

			Log.rep("Original config:\n" + writer2.toString());
			for (Field field : this.getClass().getDeclaredFields()) {
				boolean access = Misc.can_access(field, this);
				field.setAccessible(true);
				backLoad(field);
				field.setAccessible(access);
			}

			StringWriter writer = new StringWriter();
			config.write(writer);
			this.write(writer.toString());
			Log.rep("Writing config:\n" + writer.toString());
			writer.close();
		} catch (ConfigurationException | IOException e) {
			Log.trace(e);
		}
	}

	/**
	 * Checks if this object is considered loaded. Run
	 * {@link #load()} or{@link #save()}to flip this to true
	 *
	 * @return true if the object is considered loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}
}
