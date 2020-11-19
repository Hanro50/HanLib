package org.han;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.han.files.FIleUtil;

/**
 * For random static methods that didn't fit anywhere
 * 
 * @author hanro
 *
 */
public final class Misc {
	/**
	 * Splitting by Character, Unless in Quotes Regex string
	 * 
	 * @see <a href=
	 *      "https://stackabuse.com/regex-splitting-by-character-unless-in-quotes/">Source:
	 *      https://stackabuse.com</a>
	 * @author <a href="https://twitter.com/ScottWRobinson">Scott Robinson </a>
	 */
	public static final String SCUQ_base = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

	/**
	 * Creates a Regex string for splitting a string into an array while ignoring
	 * Quotes.
	 * 
	 * @param in
	 * @return
	 */
	public static String SplitFormat(char in) {
		return in + SCUQ_base;
	}

	/**
	 * Creates an instance of an object independent of it's default constructor.
	 * Abusing the fact that Google's gson library is amazing. Returns null if the
	 * object is abstract or an interface
	 * 
	 * @param TypeOfT The type of the object
	 * @return An instance of the object
	 */
	public static <T> T Construct(Class<T> TypeOfT) {
		if (Modifier.isAbstract(TypeOfT.getModifiers()) || Modifier.isInterface(TypeOfT.getModifiers())) {
			return null;
		}

		// We generate a blank object so that we're sure the format is in line with the
		// current json formatter settings
		String json = FIleUtil.GsonInstance.toJson(new Object());
		// We trick it into reading the incorrect type data and generating an object
		// based on said incorrect type data
		return FIleUtil.GsonInstance.fromJson(json, TypeOfT);

	}

	/**
	 * Wraps an object into a sub class. Abusing the fact that Google's gson library
	 * is amazing
	 * 
	 * @param TypeOfSuper
	 * @param Data
	 * @return
	 */
	public static <Super extends Base, Base> Super Wrap(Class<Super> TypeOfSuper, Base Data) {
		String json = FIleUtil.GsonInstance.toJson(Data);
		return FIleUtil.GsonInstance.fromJson(json, TypeOfSuper);
	}

	/**
	 * Checks if an object can access a field. Here due to compatibility concerns
	 * between java versions 8 and 9
	 * 
	 * @param AC  The object to check
	 * @param obj The object needed for java 9's "canAccess" method
	 * @return the boolean stating whether or not the field is accessible
	 */
	public static boolean can_access(AccessibleObject AC, Object obj) {
		if (!System.getProperty("java.vm.version").startsWith("1")) {
			try {
				Object can_access = AC.getClass().getMethod("canAccess", Object.class).invoke(AC, obj);
				if (can_access instanceof Boolean) {
					return (Boolean) can_access;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
			}
		}
		return AC.isAccessible();
	}
}
