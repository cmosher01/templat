/*
 * Created on Sep 5, 2005
 */
package nu.mine.mosher.template.exception;

public class UnknownVariableException extends TemplateParsingException
{
	public UnknownVariableException()
	{
		super();
	}

	public UnknownVariableException(String message)
	{
		super(message);
	}

	public UnknownVariableException(String message, Throwable cause)
	{
		super(message,cause);
	}

	public UnknownVariableException(Throwable cause)
	{
		super(cause);
	}
}
