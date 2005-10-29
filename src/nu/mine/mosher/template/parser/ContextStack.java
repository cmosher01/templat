/*
 * Created on Sep 9, 2005
 */
package nu.mine.mosher.template.parser;

import java.util.LinkedList;
import nu.mine.mosher.template.exception.TemplateParsingException;

public class ContextStack
{
	private final LinkedList<TemplateParserContext> rCtx = new LinkedList<TemplateParserContext>();

	public void push(final TemplateParserContext ctx)
	{
		this.rCtx.addFirst(ctx);
	}

	public void pop()
	{
		this.rCtx.removeFirst();
	}

	public Object getValue(final String varName) throws TemplateParsingException
	{
		for (final TemplateParserContext ctxToCheck : this.rCtx)
		{
			if (ctxToCheck.isDefined(varName))
			{
				return ctxToCheck.getValue(varName);
			}
			if (ctxToCheck.isEclipsing())
			{
				throw new TemplateParsingException("variable not found: "+varName);
			}
		}
		throw new TemplateParsingException("variable not found: "+varName);
	}

	public boolean contains(final String varName)
	{
		for (final TemplateParserContext ctxToCheck : this.rCtx)
		{
			if (ctxToCheck.isDefined(varName))
			{
				return true;
			}
			if (ctxToCheck.isEclipsing())
			{
				return false;
			}
		}
		return false;
	}

	public TemplateParserContext current()
	{
		return this.rCtx.getFirst();
	}

	public boolean isEverEqual(final String varName,final Object test)
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
			if (ctxToCheck.isEclipsing())
			{
				return false;
			}
		}
		return false;
	}
}
