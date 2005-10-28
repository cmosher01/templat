/*
 * Created on Oct 26, 2005
 */
package nu.mine.mosher.template.expr;

import nu.mine.mosher.template.exception.TemplateParsingException;

abstract class Selector
{
	abstract Object apply(Object var) throws TemplateParsingException;
}
