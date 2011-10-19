/*
 * Created on 2005-09-06
 */
package net.sourceforge.templat.parser.context;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.templat.exception.TemplateParsingException;

/**
 * Represents a context of variables. Basically, a context is simply
 * a mapping of variable names to values.
 *
 * @author Chris Mosher
 */
public class TemplateParserContext
{
	private final Map<String,Object> variables = new HashMap<String,Object>();

	/**
	 * @param varName
	 * @param varValue
	 */
	public void addVariable(final String varName, final Object varValue)
	{
		this.variables.put(varName,varValue);
	}

	/**
	 * Checks to see if the given variable is defined in
	 * this context.
	 * @param varName name of variable to check
	 * @return <code>true</code> if the given variable is defined
	 */
	public boolean isDefined(final String varName)
	{
		return this.variables.containsKey(varName);
	}

	/**
	 * Gets the value of a given variable in this context.
	 * @param varName name of variable to get
	 * @return the value of the given variable
	 * @throws TemplateParsingException if <code>varName</code>
	 * is not defined in this context
	 */
	public Object getValue(final String varName) throws TemplateParsingException
	{
		if (!isDefined(varName))
		{
			throw new TemplateParsingException("Cannot find "+varName);
		}
		return this.variables.get(varName);
	}
}
