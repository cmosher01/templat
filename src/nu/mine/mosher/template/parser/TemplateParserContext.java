/*
 * Created on Sep 6, 2005
 */
package nu.mine.mosher.template.parser;

import java.util.HashMap;
import java.util.Map;

public class TemplateParserContext implements Cloneable
{
	private final Map<String,Object> variables = new HashMap<String,Object>();
	private boolean eclipsing;

	public void addVariable(final String varName, final Object varValue)
	{
		this.variables.put(varName,varValue);
	}

	public boolean isDefined(final String varName)
	{
		return this.variables.containsKey(varName);
	}

	public Object getValue(final String varName)
	{
		return this.variables.get(varName);
	}

	public void setEclipsing(final boolean eclipsing)
	{
		this.eclipsing = eclipsing;
	}

	public boolean isEclipsing()
	{
		return this.eclipsing;
	}
}
