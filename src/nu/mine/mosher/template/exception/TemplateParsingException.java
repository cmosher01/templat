/*
 * Created on Sep 7, 2005
 */
package nu.mine.mosher.template.exception;

public class TemplateParsingException extends Exception
{
	public TemplateParsingException()
	{
		super();
	}

	public TemplateParsingException(String message)
	{
		super(message);
	}

	public TemplateParsingException(String message, Throwable cause)
	{
		super(message,cause);
	}

	public TemplateParsingException(Throwable cause)
	{
		super(cause);
	}
}
