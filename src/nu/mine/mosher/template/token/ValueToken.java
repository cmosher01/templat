/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.Expression;
import nu.mine.mosher.template.parser.TemplateParser;

class ValueToken extends TemplateToken
{
	private final String tag;

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
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
		try
		{
			if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
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
