/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

class EndIfToken extends TemplateToken
{
	@Override
	public String toString()
	{
		return "END IF";
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
		parser.getContext().current().getValue(TemplateParser.VAR_IF); // make sure we're in an if-block
		parser.getContext().pop();
	}
}
