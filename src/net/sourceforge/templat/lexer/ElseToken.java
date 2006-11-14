/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

class ElseToken implements TemplateToken
{
	@Override
	public String toString()
	{
		return "ELSE";
	}

	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		try
		{
			tryParse(parser);
		}
		catch (final TemplateParsingException e)
		{
			throw e;
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException(e);
		}
	}

	private void tryParse(final TemplateParser parser) throws TemplateParsingException, ClassCastException
	{
		boolean conditionIf = (Boolean)parser.getContext().current().getValue(TemplateParser.VAR_IF);
		parser.getContext().current().addVariable(TemplateParser.VAR_IF,!conditionIf);
	}
}
