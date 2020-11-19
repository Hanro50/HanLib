package org.han.debug;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.fusesource.jansi.AnsiConsole;
import org.han.internal.ASCII_CODES;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The default {@link org.han.debug.Linker Linker} implementation. Recommended
 * if this this library is going to be your main debugging thing.
 * 
 * @author hanro
 *
 */
@Log_Ignore()
public class DefaultLinker implements Linker {
	
	private static PrintStream Out;
	private static PrintStream Err;
private static final DateTimeFormatter TimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	String LastHeader = "";
	Map<String, String> BundleMap = new HashMap<String, String>();
	static {
		init();
	}
	private static void init() {
		if (System.console() != null) {
			try {
				Class.forName("org.fusesource.jansi.AnsiConsole");
				Out = AnsiConsole.out;
				Err = AnsiConsole.err;
				return;
			} catch (ClassNotFoundException e) {
			}
		}
		Out = System.out;
		Err = System.err;
	}

	private synchronized String headerBuilder(LEVEL level, StackTraceElement LastObject) {
		String result = "";
		switch (level) {
		case err:
			result = ASCII_CODES.Bright_RED + "[" + "Error:";
			break;
		case out:
			result = ASCII_CODES.Bright_GREEN + "[" + "Out:";
			break;
		case rep:
			result = ASCII_CODES.Bright_CYAN + "[" + "Report:";
			break;
		case wrn:
			result = ASCII_CODES.Bright_YELLOW + "[" + "Warning:";
			break;
		case pek:
			result = ASCII_CODES.Bright_PURPLE + "[" + "Peek:";
			break;
		default:
			result = ASCII_CODES.Bright_WHITE + "[" + "Unknown:";
			break;
		}
		String LOCL = LastObject.getClassName();
		Bundled: {
			for (String line : BundleMap.keySet()) {
				if (LOCL.startsWith(line)) {
					LOCL = BundleMap.get(line);
					result += LOCL + ")]\n" + ASCII_CODES.RESET;
					break Bundled;
				}
			}

			result += LOCL + "(" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + ")]\n"
					+ ASCII_CODES.RESET;
		}
		if (LastHeader.equals(result)) {
			return "";
		}
		LastHeader = result;
		return result;

	}

	public void trace(@NotNull Throwable e, @NotNull StackTraceElement LastObject, @Nullable String message) {
		if (message == null)
			Log.out(message);

		String out = ASCII_CODES.Bright_RED + "[" + "Trace:" + (e.getMessage() != null ? e.getMessage() : e) + "("
				+ Thread.currentThread().getName() + ":" + Thread.currentThread().getId() + ")]" + ASCII_CODES.RESET;

		out += "\n"
				+ (ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_YELLOW + "<trace call>" + ASCII_CODES.Bright_WHITE
						+ " " + LastObject.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(") + ASCII_CODES.RESET);

		for (StackTraceElement STE : e.getStackTrace()) {
			out += "\n" + (ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_RED + "<E>" + ASCII_CODES.Bright_WHITE + " "
					+ STE.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(") + ASCII_CODES.RESET);
		}

		Err.println(out);
	}

	@Override
	public void print(@NotNull LEVEL level, @NotNull StackTraceElement LastObject, @Nullable String message) {
		// TODO Auto-generated method stub
		PrintStream Output;
		switch (level) {
		case err:
		case wrn:
			Output = Err;
			break;
		default:
			Output = Out;
			break;
		}
		String linedata = "    ["+TimeFormat.format( LocalTime.now())+"]L" + LastObject.getLineNumber() + ":";

		Output.println(headerBuilder(level, LastObject) + ASCII_CODES.Bright_WHITE + linedata
				+ message.trim().replaceAll("\n", "\n" + linedata) + ASCII_CODES.RESET);
	}

	@Override
	public void bundle(@NotNull String Startwith, String BundleAs) {
		BundleMap.put(Startwith, BundleAs);

	}

}
