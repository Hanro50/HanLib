package za.net.hanro50.debug;

import  java.lang.annotation.ElementType;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This tells the logging framework to ignore any calls from an object.
 * 
 * {@link #IgnorePackagesStartingWith() IgnorePackagesStartingWith()} can be set
 * to specify package roots to ignore as well. (This is useful when you're
 * redirecting the output of 3rd party libraries)
 * 
 * @author hanro
 */

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface Log_Ignore {
	/**
	 * Specifies a package that the logger should also ignore. If a class within
	 * that package calls this annotation again. This flag is reset.
	 * 
	 * @return Specifies packages to ignore "org.example.*" (Wild cards are not
	 *         accepted, a '*' will essentially always be added to any string you
	 *         provide.
	 */
	String IgnorePackagesStartingWith() default "";
}
