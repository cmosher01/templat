/*
 * Created on Sep 6, 2005
 */
package nu.mine.mosher.template.expr;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.exception.ExprLexingException;
import nu.mine.mosher.template.expr.exception.ExprParsingException;
import nu.mine.mosher.template.parser.ContextStack;



public class Include
{
	public static TemplateIncludeExpression eval(final String incl, final ContextStack stackContext) throws TemplateParsingException
	{
		try
		{
			return tryEval(incl,stackContext);
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException("error occurred while evaluating template include \""+incl+"\":",e);
		}
	}

	private static TemplateIncludeExpression tryEval(final String incl, final ContextStack stackContext) throws ExprLexingException, ExprParsingException, TemplateParsingException
	{
		final IncludeParser parser = new IncludeParser(incl,stackContext);

		return (TemplateIncludeExpression)parser.parse();
	}
}
