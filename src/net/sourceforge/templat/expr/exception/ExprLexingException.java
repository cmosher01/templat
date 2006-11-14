/*
 * Created on 2005-09-26
 */
package net.sourceforge.templat.expr.exception;

/**
 * Indicates an exception during lexing an expression.
 *
 * @author Chris Mosher
 */
public class ExprLexingException extends Exception
{
	/**
	 * 
	 */
	public ExprLexingException()
	{
		super();
	}

	/**
	 * @param e
	 */
	public ExprLexingException(final Throwable e)
	{
		super(e);
	}
}
