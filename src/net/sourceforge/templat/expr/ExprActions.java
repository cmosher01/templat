/*
 * Created on Sep 26, 2005
 */
package net.sourceforge.templat.expr;

import java.util.ArrayList;
import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.context.ContextStack;

/**
 * Actions for <code>ExprParser</code> and <code>IncludeParser</code>.
 *
 * @author Chris Mosher
 */
class ExprActions
{
	private final ContextStack ctx;



	public ExprActions(final ContextStack stackContext)
	{
		this.ctx = stackContext;
	}



	public ArrayList<Object> createList()
	{
	    return new ArrayList<Object>();
	}

	public ArrayList<Object> addToList(final Object arg, final Object arglist)
	{
	    @SuppressWarnings("unchecked")
		final ArrayList<Object> rArg = (ArrayList<Object>)arglist;
	    rArg.add(arg);
	    return rArg;
	}

	public ArraySubscript createArraySubscript(final Object subscript) throws TemplateParsingException
	{
		try
		{
			return new ArraySubscript(((Integer)subscript).intValue());
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException("invalid number: "+subscript,e);
		}
	}

	@SuppressWarnings("unchecked")
	public MethodCall createMethodCall(final Object nameMethod, final Object arglist)
	{
		return new MethodCall(nameMethod.toString(),(ArrayList<Object>)arglist);
	}

	public Object applySelectors(final Object name, final Object selectors) throws TemplateParsingException
	{
		final String sVarName = name.toString();
		@SuppressWarnings("unchecked")
		final ArrayList<Object> rSelector = (ArrayList<Object>)selectors;

		Object var = getVariableOrClass(sVarName);
		for (final Object selector : rSelector)
		{
			final Selector sel = (Selector)selector;
			var = sel.apply(var);
		}
		return var;
	}

	private Object getVariableOrClass(final String name) throws TemplateParsingException
	{
		if (this.ctx.contains(name))
		{
			return this.ctx.getValue(name);
		}
		// not a variable, so continue

		try
		{
			return Class.forName(name);
		}
		catch (final ClassNotFoundException e)
		{
			// not a class, either, so continue
		}

		throw new TemplateParsingException( "Unknown variable: "+name);
	}
}
