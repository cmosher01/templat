/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.lexer;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

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
