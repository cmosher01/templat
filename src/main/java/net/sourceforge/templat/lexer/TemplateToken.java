/*
 * Created on 2005-09-04
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * Represents a token, as output from {@link TemplateLexer}.
 *
 * @author Chris Mosher
 */
public interface TemplateToken
{
	/**
	 * Parses this token. Each token that implements this
	 * interface will perform their primary action in this method.
	 * @param parser the parser that is parsing the template
	 * @param appendTo the <code>Appendable</code> to append the result of the parsing to
	 * @throws TemplateParsingException
	 */
	void parse(TemplateParser parser, Appendable appendTo) throws TemplateParsingException;
}
