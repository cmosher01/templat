/*
 * Created on 2005-10-26
 */
package nu.mine.mosher.template.expr;

import nu.mine.mosher.template.exception.TemplateParsingException;

interface Selector
{
	/**
	 * Applies this selector to the given variable.
	 * @param var variable to apply this selector to
	 * @return the result of applying this selector to the given variable
	 * @throws TemplateParsingException
	 */
	Object apply(Object var) throws TemplateParsingException;
}
