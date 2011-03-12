/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * <span class="directive">@&nbsp;<span class="keyword">else</span>&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class ElseToken implements TemplateToken
{
	@Override
	public String toString()
	{
		return "ELSE";
	}

	@Override
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
		boolean conditionIf = ((Boolean)parser.getContext().current().getValue(TemplateParser.VAR_IF)).booleanValue();
		parser.getContext().current().addVariable(TemplateParser.VAR_IF,Boolean.valueOf(!conditionIf));
	}
}
