/*
 * Created on Sep 26, 2005
 */
package net.sourceforge.templat.expr.exception;

/**
 * Indicates an exception during parsing an expression.
 *
 * @author Chris Mosher
 */
public class ExprParsingException extends Exception
{
	/**
	 * 
	 */
	public ExprParsingException()
	{
		super();
	}
	/**
	 * @param e
	 */
	public ExprParsingException(final Throwable e)
	{
		super(e);
	}
	/**
	 * @param message
	 */
	public ExprParsingException(final String message)
	{
		super(message);
	}
}
