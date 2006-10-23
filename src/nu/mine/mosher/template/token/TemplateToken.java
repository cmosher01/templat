/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

public interface TemplateToken
{
	void parse(TemplateParser parser, StringBuilder appendTo) throws TemplateParsingException;
}
