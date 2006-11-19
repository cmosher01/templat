/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * <span class="directive">@&nbsp;<span class="keyword">end&nbsp;loop</span>&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class EndLoopToken implements TemplateToken
{
	@Override
	public String toString()
	{
		return "END LOOP";
	}

	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		final Number numTimes = (Number)parser.getContext().current().getValue(TemplateParser.VAR_LOOP_TIMES);
		final int times = numTimes.intValue();

		final String varIndex = (String)parser.getContext().current().getValue(TemplateParser.VAR_LOOP_INDEX);
		int i = (Integer)parser.getContext().getValue(varIndex);

		++i;

		parser.getContext().current().addVariable(varIndex,i);

		if (i < times && !parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
		{
			parser.restoreParsePosition();
			parser.saveParsePosition();
		}
		else
		{
			parser.forgetParsePosition();
			parser.getContext().pop();
		}
	}
}
