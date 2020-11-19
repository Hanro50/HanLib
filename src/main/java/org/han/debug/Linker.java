package org.han.debug;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/**
 * The linker can be used to redirect log output elsewhere. 
 * @author hanro
 *
 */
@Log_Ignore
public interface Linker {
	public static enum LEVEL {
		out, err, rep, wrn,pek
	}

	public void print(@NotNull LEVEL level, @NotNull StackTraceElement LastObject, @Nullable String message);

	public void trace(@NotNull Throwable e, @NotNull StackTraceElement LastObject, @Nullable String message);
	
	public void bundle(@NotNull String Startwith, String BundleAs);
	
}
