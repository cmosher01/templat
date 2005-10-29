/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;
import nu.mine.mosher.template.parser.TemplateParserContext;

class EndLoopToken extends TemplateToken
{
	@Override
	public String toString()
	{
		return "END LOOP";
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
		final Number numTimes = (Number)parser.getContext().getValue(TemplateParser.VAR_LOOP_TIMES);
		final int times = numTimes.intValue();

		final String varIndex = (String)parser.getContext().getValue(TemplateParser.VAR_LOOP_INDEX);
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
			parser.getContext().pop();
		}
	}
}
