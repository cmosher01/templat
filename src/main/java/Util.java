/*
 * Created on 2006-10-09
 */

/**
 * Contains static utility methods intended to be called
 * from templates.
 *
 * @author Chris Mosher
 */
public class Util
{
	/**
	 * Checks to see if the given reference is <code>null</code>.
	 * @param <T> data-type of object
	 * @param object the reference to check
	 * @return <code>true</code> if the reference is <code>null</code>.
	 */
	public static <T> Boolean isNull(final T object)
	{
		return Boolean.valueOf(object == null);
	}
}
