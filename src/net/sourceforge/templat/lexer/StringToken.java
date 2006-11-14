/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

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
