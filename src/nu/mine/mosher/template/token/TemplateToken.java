/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.TemplateParser;
import nu.mine.mosher.template.exception.TemplateParsingException;

public abstract class TemplateToken
{
	public abstract void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException;
}
