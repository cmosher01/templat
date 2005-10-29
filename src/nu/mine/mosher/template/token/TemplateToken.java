/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

public abstract class TemplateToken
{
	public abstract void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException;
}
