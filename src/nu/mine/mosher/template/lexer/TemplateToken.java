/*
 * Created on 2005-09-04
 */
package nu.mine.mosher.template.lexer;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;

/**
 * Represents a token, as output from the <code>TemplateLexer</code>.
 *
 * @author Chris Mosher
 */
public interface TemplateToken
{
	/**
	 * Parses this token. Each token that implements the <code>TemplateToken</code>
	 * interface will perform their primary action in this method.
	 * @param parser the parser that is parsing the template
	 * @param appendTo the <code>Appendable</code> to append the result of the parsing to
	 * @throws TemplateParsingException
	 */
	void parse(TemplateParser parser, Appendable appendTo) throws TemplateParsingException;
}
