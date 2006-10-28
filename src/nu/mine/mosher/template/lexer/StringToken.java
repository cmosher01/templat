/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.lexer;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

class StringToken implements TemplateToken
{
	private final String tag;

	/**
	 * @param tag
	 */
	public StringToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "STRING: "+this.tag;
	}

	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
		{
			return;
		}

		try
		{
			appendTo.append(this.tag);
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException(e);
		}
	}
}
