/*
 * Created on Oct 27, 2005
 */
package nu.mine.mosher.template.expr;

import java.util.ArrayList;

/**
 * Stores the result of parsing an include statement in a template.
 *
 * @author Chris Mosher
 */
public class TemplateIncludeExpression
{
	private final String sNameTemplate;
	private final ArrayList<Object> rArg;

	TemplateIncludeExpression(final String sNameTemplate, final ArrayList<Object> rArg)
	{
		this.sNameTemplate = sNameTemplate;
		this.rArg = rArg;
	}

	/**
	 * The name (and path) of the template to include.
	 * @return the path
	 */
	public String getTemplateName()
	{
		return this.sNameTemplate;
	}

	/**
	 * The arguments to pass to the included template.
	 * @return list of objects
	 */
	public ArrayList<Object> getArgs()
	{
		return this.rArg;
	}

}
