/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.Expression;
import net.sourceforge.templat.parser.TemplateParser;

/**
 * <span class="directive">@&nbsp;<span class="var">expression</span>&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class ValueToken implements TemplateToken
{
	private final String tag;

	/**
	 * @param tag
	 */
	public ValueToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "VALUE: "+this.tag;
	}

	@Override
	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		try
		{
			if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,Boolean.FALSE))
			{
				return;
			}
			final Object value = Expression.eval(this.tag.trim(),parser.getContext());
			if (value != null)
			{
				appendTo.append(value.toString());
			}
		}
		catch (final TemplateParsingException e)
		{
			throw e;
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException(e);
		}
	}
}
