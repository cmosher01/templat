/*
 * Created on 2005-09-05
 */
package nu.mine.mosher.template.exception;

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
	 * @param message
	 * @param cause
	 */
	public TemplateLexingException(String message, Throwable cause)
	{
		super(message,cause);
	}

	/**
	 * @param message
	 */
	public TemplateLexingException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TemplateLexingException(Throwable cause)
	{
		super(cause);
	}
}
