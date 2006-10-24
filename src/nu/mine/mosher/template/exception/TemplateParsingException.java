/*
 * Created on 2005-09-07
 */
package nu.mine.mosher.template.exception;

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
	 * @param message
	 */
	public TemplateParsingException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public TemplateParsingException(String message, Throwable cause)
	{
		super(message,cause);
	}

	/**
	 * @param cause
	 */
	public TemplateParsingException(Throwable cause)
	{
		super(cause);
	}
}
