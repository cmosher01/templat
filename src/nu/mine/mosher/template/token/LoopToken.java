/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.TemplateParser;
import nu.mine.mosher.template.TemplateParserContext;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.Expression;

class LoopToken extends TemplateToken
{
	private final String tag;

	public LoopToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "LOOP: "+this.tag;
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
		final int posColon = this.tag.indexOf(':');

		if (posColon < 0)
		{
			throw new TemplateParsingException("Missing colon in loop statement.");
		}

		final TemplateParserContext ctxNew = new TemplateParserContext();

		{
			final String varIndex = this.tag.substring(0,posColon).trim();
			ctxNew.addVariable(TemplateParser.VAR_LOOP_INDEX,varIndex);
			ctxNew.addVariable(varIndex,0);
		}

		{
			final String exprTimes = this.tag.substring(posColon+1).trim();
	
			final Number numTimes = (Number)Expression.eval(exprTimes,parser.getContext());
			final int times = numTimes.intValue();
	
			ctxNew.addVariable(TemplateParser.VAR_LOOP_TIMES,times);
		}

		parser.getContext().push(ctxNew);

		parser.saveParsePosition();
	}
}
