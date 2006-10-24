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
	 * @param object the reference to check
	 * @return <code>true</code> if the reference is <code>null</code>.
	 */
	public static Boolean isNull(final Object object)
	{
		return object == null;
	}
}
