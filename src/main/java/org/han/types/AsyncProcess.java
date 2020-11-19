package org.han.types;

import java.util.function.Consumer;

import org.han.debug.*;

/**
 * </br>
 * </br>
 * This class is likely to be removed or rewritten in the future.
 * 
 * @author hanro
 *
 * @param <OutputType>
 */
@FunctionalInterface
public interface AsyncProcess<OutputType> {
	public OutputType complete() throws Throwable;

	public default void queue() {
		queue(null, null);
	}

	public default void queue(Consumer<OutputType> supplier) {
		queue(supplier, null);
	}

	public default void queue(Consumer<OutputType> supplier, Consumer<? super Throwable> error) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					OutputType output = complete();
					if (supplier != null)
						supplier.accept(output);
				} catch (Throwable e) {
					if (error != null)
						error.accept(e);
					else
						Log.trace(e);
				}
			}
		}).start();
	}

	public static <OutputType> void run(Class<OutputType> Output, AsyncProcess<OutputType> process) {
		process.queue();
	}

	public static <OutputType> void run(Class<OutputType> Output, AsyncProcess<OutputType> process,
			Consumer<OutputType> supplier) {
		process.queue(supplier);
	}

	public static <OutputType> void run(Class<OutputType> Output, AsyncProcess<OutputType> process,
			Consumer<OutputType> supplier, Consumer<? super Throwable> error) {
		process.queue(supplier, error);
	}

	public static <OutputType> void run(Consumer<OutputType> supplier) {
		run(null, () -> null, supplier);
	}
	
	public static <OutputType> void run(Consumer<OutputType> supplier,Consumer<? super Throwable> error) {
		run(null, () -> null, supplier,error);
	}

}
