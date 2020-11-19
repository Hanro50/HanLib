package org.han.debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A static version of {@link org.han.debug.DLog DLog}
 * 
 * @author hanro
 * 
 */
@Log_Ignore
public class Log {
	private static boolean bpek = false;
	public static DLog SystemDynamicLogObject = new DLog(new DefaultLinker());

	public static void enablePeek(boolean pekable) {
		(bpek) = pekable;
	}

	/**
	 * Meant for messages that get repeated a lot, but which are only meant for
	 * debugging information that's only rarely of any use. Such as a heart beat
	 * ping from a web server
	 * 
	 * @param pek
	 */
	public static void pek(String pek) {
		if (bpek)
			SystemDynamicLogObject.pek(pek);
	}
	/**
	 * Meant for messages that get repeated a lot, but which are only meant for
	 * debugging information that's only rarely of any use. Such as a heart beat
	 * ping from a web server
	 * 
	 * @param pek
	 */
	public static void pek(Object pek) {
		if (bpek)
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
	
	public static void bundle(@NotNull  String Startwith, String BundleAs) {
		SystemDynamicLogObject.bundle(Startwith, BundleAs);
	}
	
	public static void Version() {

		out("Printing Debug Information:");

		List<String> keys = new ArrayList<String>();
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.addAll(System.getProperties().stringPropertyNames());
		keys.sort(cmp);

		int leng = 10;
		for (String string : keys) {
			if (string.length() > leng) {
				leng = string.length();
			}

		}
		leng++;
		out("<Please use a monospace font or this will display incorrectly!>");
		final String format = "%-" + leng + "s|%s";
		out(String.format(format, "Property", "Value"));
		String out = "";
		for (String key : keys) {
			out = out + (String.format(format, key, System.getProperty(key).trim())) + '\n';
		}

		out(out);
		out("Continuing on with rest of application->");
	}
	
	private static String nullCheck(Object in) {
		return in!=null?in.toString():"NULL!";
	}
	
}
