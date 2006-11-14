/*
 * Created on Sep 6, 2005
 */
package net.sourceforge.templat.expr;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.exception.ExprLexingException;
import net.sourceforge.templat.expr.exception.ExprParsingException;
import net.sourceforge.templat.parser.context.ContextStack;



/**
 * Contains static methods to evaluate an expression within a
 * template statement.
 *
 * @author Chris Mosher
 */
public class Expression
{
	/**
	 * Evaluates the given expression, using the given context stack.
	 * @param expr expression to evaluate
	 * @param stackContext context stack
	 * @return the result of evaluating the expression
	 * @throws TemplateParsingException
	 */
	public static Object eval(final String expr, final ContextStack stackContext) throws TemplateParsingException
	{
		try
		{
			return tryEval(expr,stackContext);
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException("error occurred while evaluating expression \""+expr+"\":",e);
		}
	}

	private static Object tryEval(final String expr, final ContextStack stackContext) throws ExprLexingException, ExprParsingException, TemplateParsingException
	{
		final ExprParser parser = new ExprParser(expr,stackContext);

		return parser.parse();
	}
}
