package za.net.hanro50.debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A static version of {@link za.net.hanro50.debug.DLog DLog}
 * 
 * @author hanro
 * 
 */
@Log_Ignore
public class Log {
	private static boolean pek_bool = false;
	public static DLog SystemDynamicLogObject = new DLog(new DefaultLinker());

	public static void enablePeek(boolean peekable) {
		(pek_bool) = peekable;
	}

	/**
	 * Meant for messages that get repeated a lot, but which are only meant for
	 * debugging information that's only rarely of any use. Such as a heart beat
	 * ping from a web server
	 */
	public static void pek(String pek) {
		if (pek_bool)
			SystemDynamicLogObject.pek(pek);
	}
	/**
	 * Meant for messages that get repeated a lot, but which are only meant for
	 * debugging information that's only rarely of any use. Such as a heart beat
	 * ping from a web server
	 */
	public static void pek(Object pek) {
		if (pek_bool)
			SystemDynamicLogObject.pek(nullCheck(pek));
	}

	public static void wrn(String error) {
		SystemDynamicLogObject.wrn(error);
	}

	public static void wrn(Object error) {
		SystemDynamicLogObject.wrn(nullCheck(error));
	}

	public static void err(String error) {
		SystemDynamicLogObject.err(error);
	}

	public static void err(Object error) {
		SystemDynamicLogObject.err(nullCheck(error));
	}

	public static void rep(String message) {
		SystemDynamicLogObject.rep(message);
	}

	public static void rep(Object message) {
		SystemDynamicLogObject.rep(nullCheck(message));
	}

	public static void out(String message) {
		SystemDynamicLogObject.out(message);
	}

	public static void out(Object message) {
		SystemDynamicLogObject.out(nullCheck(message));
	}

	public static void trace(@NotNull Throwable e) {
		trace(e, null);
	}

	public static void trace(@NotNull Throwable e, @Nullable String Message) {
		SystemDynamicLogObject.trace(e, Message);
	}

	public static void trace(@NotNull Throwable e, @Nullable Object Message) {
		if (Message != null)
			SystemDynamicLogObject.trace(e, Message.toString());
		else
			SystemDynamicLogObject.trace(e, null);
	}
	
	public static void bundle(@NotNull  String start_With, String BundleAs) {
		SystemDynamicLogObject.bundle(start_With, BundleAs);
	}
	
	public static void Version() {

		out("Printing Debug Information:");

		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		List<String> keys = new ArrayList<>(System.getProperties().stringPropertyNames());
		keys.sort(cmp);

		int length = 10;
		for (String string : keys) {
			if (string.length() > length) {
				length = string.length();
			}

		}
		length++;
		out("<Please use a monospace font or this will display incorrectly!>");
		final String format = "%-" + length + "s|%s";
		out(String.format(format, "Property", "Value"));
		StringBuilder out = new StringBuilder();
		for (String key : keys) {
			out.append(String.format(format, key, System.getProperty(key).trim())).append('\n');
		}

		out(out.toString());
		out("Continuing on with rest of application->");
	}
	
	private static String nullCheck(Object in) {
		return in!=null?in.toString():"NULL!";
	}
	
}
