/*
 * Created on 2005-09-05
 */
package net.sourceforge.templat.exception;

/**
 * Represents an exception during lexing.
 *
 * @author Chris Mosher
 */
public class TemplateLexingException extends Exception
{
	/**
	 * 
	 */
	public TemplateLexingException()
	{
		//
	}

	/**
	 * @param message error text
	 * @param cause wrapped exception
	 */
	public TemplateLexingException(String message, Throwable cause)
	{
		super(message,cause);
	}

	/**
	 * @param message error text
	 */
	public TemplateLexingException(String message)
	{
		super(message);
	}

	/**
	 * @param cause wrapped exception
	 */
	public TemplateLexingException(Throwable cause)
	{
		super(cause);
	}
}
