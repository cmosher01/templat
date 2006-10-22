/*
 * Created on Oct 26, 2005
 */
package nu.mine.mosher.template.expr;

import java.lang.reflect.Array;
import java.util.List;
import nu.mine.mosher.template.exception.TemplateParsingException;

class ArraySubscript extends Selector
{
	private final int subscript;

	ArraySubscript(final int subscript)
	{
		this.subscript = subscript;
	}

	int getSubscript()
	{
		return this.subscript;
	}

	@Override
	Object apply(final Object var) throws TemplateParsingException
	{
		if (var.getClass().isArray())
		{
			try
			{
				return Array.get(var,this.subscript);
			}
			catch (final Throwable e)
			{
				throw new TemplateParsingException("error accessing array",e);
			}
		}

		if (var instanceof List)
		{
			final List<Object> r = (List<Object>)var;
			return r.get(this.subscript);
		}
		throw new TemplateParsingException("error accessing array or list");
	}
}
