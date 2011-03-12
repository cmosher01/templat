/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * <span class="directive">@&nbsp;<span class="keyword">end&nbsp;if</span>&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class EndIfToken implements TemplateToken
{
	@Override
	public String toString()
	{
		return "END IF";
	}

	@Override
	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		parser.getContext().current().getValue(TemplateParser.VAR_IF); // make sure we're in an if-block
		parser.getContext().pop();
	}
}
