/*
 * Created on Sep 18, 2005
 */
public class Num
{
	public static boolean isEven(final int n)
	{
		return n % 2 == 0;
	}

	public static int oneOrigin(final int iZeroOrigin)
	{
		if (iZeroOrigin < 0)
		{
			throw new IllegalArgumentException("cannot be negative, was "+iZeroOrigin);
		}
		return iZeroOrigin+1;
	}

	public static int getYearWritten()
	{
		return 2005;
	}

	public static String getAuthor()
	{
		return "Chris Mosher";
	}
}
