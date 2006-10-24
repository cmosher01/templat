/*
 * Created on Sep 6, 2005
 */
package nu.mine.mosher.template.expr;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.exception.ExprLexingException;
import nu.mine.mosher.template.expr.exception.ExprParsingException;
import nu.mine.mosher.template.parser.context.ContextStack;



/**
 * Contains a static method to evaluate an include statement in
 * a template.
 *
 * @author Chris Mosher
 */
public class Include
{
	/**
	 * Parses the given include statement, evaluating any arguments, and returning an
	 * object containing the name of the include file and the results of each argument.
	 * @param incl
	 * @param stackContext
	 * @return include file name, and arguments
	 * @throws TemplateParsingException
	 */
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
