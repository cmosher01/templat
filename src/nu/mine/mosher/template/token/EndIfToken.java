/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.TemplateParser;
import nu.mine.mosher.template.TemplateParserContext;
import nu.mine.mosher.template.exception.TemplateParsingException;

public class EndIfToken extends TemplateToken
{
	@Override
	public String toString()
	{
		return "END IF";
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo)
	{
		parser.getContext().pop();
	}
}
