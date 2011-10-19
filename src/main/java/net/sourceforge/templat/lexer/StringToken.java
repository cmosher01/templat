/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * Represents areas of the template that are outside of any tag.
 *
 * @author Chris Mosher
 */
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

	@Override
	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,Boolean.FALSE))
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
