/*
 * Created on 2005-10-26
 */
package net.sourceforge.templat.expr;

import net.sourceforge.templat.exception.TemplateParsingException;

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
