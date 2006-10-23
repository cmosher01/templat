/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

class StringToken implements TemplateToken
{
	private final String tag;

	public StringToken(final String tag)
	{
		this.tag = tag;
	}

	public String toString()
	{
		return "STRING: "+this.tag;
	}

	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
		{
			return;
		}

		appendTo.append(this.tag);
	}
}
