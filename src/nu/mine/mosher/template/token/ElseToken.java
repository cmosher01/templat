/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.ContextStack;
import nu.mine.mosher.template.parser.TemplateParser;
import nu.mine.mosher.template.parser.TemplateParserContext;

class ElseToken extends TemplateToken
{
	@Override
	public String toString()
	{
		return "ELSE";
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
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
