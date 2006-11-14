/*
 * Created on 2005-09-09
 */
package net.sourceforge.templat.parser.context;

import java.util.LinkedList;
import net.sourceforge.templat.exception.TemplateParsingException;

/**
 * A stack of <code>TemplateParserContext</code>s. At any one given moment, a
 * <code>ContextStack</code> can be used by the parser to determine the value
 * of a given variable. The contexts will be searched from inner-most (at the
 * top of the stack) to outer-most (at the bottom of the stack).
 *
 * @author Chris Mosher
 */
public class ContextStack
{
	private final LinkedList<TemplateParserContext> rCtx = new LinkedList<TemplateParserContext>();

	/**
	 * Pushes the given context onto the top of this stack.
	 * @param ctx the context to push
	 */
	public void push(final TemplateParserContext ctx)
	{
		this.rCtx.addFirst(ctx);
	}

	/**
	 * Pops the context off the top of this stack.
	 */
	public void pop()
	{
		this.rCtx.removeFirst();
	}

	/**
	 * Gets the value of the given variable from this stack. The contexts in this stack
	 * are searched from inner-most (at the top of the stack) to outer-most (at the bottom
	 * of the stack). The first context that contains the given variable is used, and the
	 * value of the variable in that context is returned.
	 * @param varName variable to look up
	 * @return the value of the variable
	 * @throws TemplateParsingException if the variable is not found
	 */
	public Object getValue(final String varName) throws TemplateParsingException
	{
		for (final TemplateParserContext ctxToCheck : this.rCtx)
		{
			if (ctxToCheck.isDefined(varName))
			{
				return ctxToCheck.getValue(varName);
			}
		}
		throw new TemplateParsingException("variable not found: "+varName);
	}

	/**
	 * Checks if this stack contains the given variable.
	 * @param varName the variable to search for
	 * @return <code>true</code> if any context in this stack contains the variable.
	 */
	public boolean contains(final String varName)
	{
		for (final TemplateParserContext ctxToCheck : this.rCtx)
		{
			if (ctxToCheck.isDefined(varName))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the current (inner-most) context (that is, the one at the top of this stack).
	 * @return the inner-most context
	 */
	public TemplateParserContext current()
	{
		return this.rCtx.getFirst();
	}

	/**
	 * Checks if the given variable is equal to the given value in any
	 * of this stack's contexts.
	 * @param varName the variable to search for
	 * @param test value to check for the variable being equal to
	 * @return <code>true</code> if the variable is found, and contains the given value
	 * @throws TemplateParsingException
	 */
	public boolean isEverEqual(final String varName,final Object test) throws TemplateParsingException
	{
		for (final TemplateParserContext ctxToCheck : this.rCtx)
		{
			if (ctxToCheck.isDefined(varName))
			{
				if (ctxToCheck.getValue(varName).equals(test))
				{
					return true;
				}
			}
		}
		return false;
	}
}
