/*
 * Created on Sep 26, 2005
 */
package nu.mine.mosher.template.expr.exception;

public class ExprParsingException extends Exception
{
	public ExprParsingException()
	{
		super();
	}
	public ExprParsingException(final Throwable e)
	{
		super(e);
	}
	public ExprParsingException(final String message)
	{
		super(message);
	}
}
