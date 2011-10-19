/*
 * Created on 2005-09-07
 */
package net.sourceforge.templat.exception;

/**
 * Represents an exception during parsing.
 *
 * @author Chris Mosher
 */
public class TemplateParsingException extends Exception
{
	/**
	 * 
	 */
	public TemplateParsingException()
	{
		//
	}

	/**
	 * @param message error text
	 * @param cause wrapped exception
	 */
	public TemplateParsingException(String message, Throwable cause)
	{
		super(message,cause);
	}

	/**
	 * @param message error text
	 */
	public TemplateParsingException(String message)
	{
		super(message);
	}

	/**
	 * @param cause wrapped exception
	 */
	public TemplateParsingException(Throwable cause)
	{
		super(cause);
	}
}
