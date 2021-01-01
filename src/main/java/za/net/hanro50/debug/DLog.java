package za.net.hanro50.debug;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import za.net.hanro50.debug.Linker.LEVEL;


/**
 * Dynamic Logger object
 * 
 * @author hanro
 *
 *
 */
@Log_Ignore
public class DLog {
	public final Linker Link;

	public DLog(Linker Link) {
		this.Link = Link;
	}

	private StackTraceElement LastObject() {
		StackTraceElement resultcall = Thread.currentThread().getStackTrace()[1];
		LoopBreak: {
			Log_Ignore LastAnnon = Log.class.getAnnotation(Log_Ignore.class);

			for (StackTraceElement LastClass : Thread.currentThread().getStackTrace()) {
				try {
					Class<?> LastClassObject = Class.forName(LastClass.getClassName());

					if (!(LastClassObject.isAnnotationPresent(Log_Ignore.class)
							|| LastClass.getClassName().equals("java.lang.Thread")
							|| (LastAnnon != null
									&& LastClass.getClassName().startsWith(LastAnnon.IgnorePackagesStartingWith())
									&& !LastAnnon.IgnorePackagesStartingWith().equals("")))) {
						resultcall = LastClass;

						break LoopBreak;
					}
					Log_Ignore NewAnnon = LastClassObject.getAnnotation(Log_Ignore.class);
					LastAnnon = NewAnnon != null ? NewAnnon : LastAnnon;
				} catch (ClassNotFoundException e) {
					Link.print(LEVEL.err, resultcall, "INCOMPATIBLE CLASS LOADER DETECTED");
				}
			}

			Link.print(LEVEL.err, resultcall, "Could not decode stack trace");
		}
		return resultcall;
	}

	public void pek(String pek) {
		Link.print(LEVEL.pek, LastObject(), nullCheck(pek));
	}

	public void wrn(String error) {
		Link.print(LEVEL.wrn, LastObject(), nullCheck(error));
	}

	public void err(String error) {
		Link.print(LEVEL.err, LastObject(), nullCheck(error));
	}

	public void rep(String message) {
		Link.print(LEVEL.rep, LastObject(), nullCheck(message));
	}

	public void out(String message) {
		Link.print(LEVEL.out, LastObject(), nullCheck(message));
	}

	public void trace(@NotNull Throwable e) {
		trace(e, null);
	}

	public void trace(@NotNull Throwable e, @Nullable String message) {
		Link.trace(e, LastObject(), message);
	}

	private String nullCheck(String in) {
		return (in == null ? "" : in);
	}

	public void bundle(@NotNull String Startwith, String BundleAs) {
		Link.bundle(Startwith, BundleAs);
	}

}
