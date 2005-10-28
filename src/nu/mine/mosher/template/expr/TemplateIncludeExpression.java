/*
 * Created on Oct 27, 2005
 */
package nu.mine.mosher.template.expr;

import java.util.ArrayList;

public class TemplateIncludeExpression
{
	private final String sNameTemplate;
	private final ArrayList<Object> rArg;

	TemplateIncludeExpression(final String sNameTemplate, final ArrayList<Object> rArg)
	{
		this.sNameTemplate = sNameTemplate;
		this.rArg = rArg;
	}

	public String getTemplateName()
	{
		return this.sNameTemplate;
	}

	public ArrayList<Object> getArgs()
	{
		return this.rArg;
	}

}
