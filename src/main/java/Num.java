/*
 * Created on 2005-09-18
 */

/**
 * Contains static utility methods intended to be called
 * from templates.
 *
 * @author Chris Mosher
 */
public class Num
{
	/**
	 * Checks to see if the given integer is even.
	 * @param n integer to check
	 * @return <code>true</code> if the integer is even
	 */
	public static boolean isEven(final int n)
	{
		return n % 2 == 0;
	}

	/**
	 * Checks to see if the given integer is odd.
	 * @param n integer to check
	 * @return <code>true</code> if the integer is odd
	 */
	public static boolean isOdd(final int n)
	{
		return n % 2 != 0;
	}

	/**
	 * Converts an integer from a zero-origin base to a one-origin base.
	 * In other words, it adds one to the given number.
	 * @param iZeroOrigin number to add one to; must be greater than or equal to zero
	 * @return <code>iZeroOrigin + 1</code>
	 */
	public static int oneOrigin(final int iZeroOrigin)
	{
		if (iZeroOrigin < 0)
		{
			throw new IllegalArgumentException("cannot be negative, was "+iZeroOrigin);
		}
		return iZeroOrigin+1;
	}
}
