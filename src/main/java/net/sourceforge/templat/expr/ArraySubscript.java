/*
 * Created on 2005-10-26
 */
package net.sourceforge.templat.expr;



import java.lang.reflect.Array;
import java.util.List;

import net.sourceforge.templat.exception.TemplateParsingException;



class ArraySubscript implements Selector
{
    private final int subscript;

    public ArraySubscript(final int subscript)
    {
        this.subscript = subscript;
    }

    @Override
    public Object apply(final Object var) throws TemplateParsingException
    {
        if (var.getClass().isArray())
        {
            try
            {
                return Array.get(var, this.subscript);
            }
            catch (final Throwable e)
            {
                throw new TemplateParsingException("error accessing array", e);
            }
        }

        if (var instanceof List)
        {
            final List<?> r = (List<?>) var;
            return r.get(this.subscript);
        }
        throw new TemplateParsingException("error accessing array or list");
    }
}
