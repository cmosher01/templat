/*
 * Created on Sep 26, 2005
 */
package nu.mine.mosher.template.expr;

import java.util.ArrayList;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.context.ContextStack;

/**
 * Actions for <code>ExprParser</code> and <code>IncludeParser</code>.
 *
 * @author Chris Mosher
 */
class ExprActions
{
	final ContextStack ctx;



	ExprActions(final ContextStack stackContext)
	{
		this.ctx = stackContext;
	}



	ArrayList<Object> createList()
	{
	    return new ArrayList<Object>();
	}

	ArrayList<Object> addToList(final Object arg, final Object arglist)
	{
	    final ArrayList<Object> rArg = (ArrayList<Object>)arglist;
	    rArg.add(arg);
	    return rArg;
	}

	ArraySubscript createArraySubscript(final Object subscript) throws TemplateParsingException
	{
		try
		{
			return new ArraySubscript((Integer)subscript);
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException("invalid number: "+subscript,e);
		}
	}

	MethodCall createMethodCall(final Object nameMethod, final Object arglist)
	{
		return new MethodCall(nameMethod.toString(),(ArrayList<Object>)arglist);
	}

	Object applySelectors(final Object name, final Object selectors) throws TemplateParsingException
	{
		final String sVarName = name.toString();
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

		try
		{
			return Class.forName(name);
		}
		catch (final ClassNotFoundException e)
		{
			throw new TemplateParsingException(e);
		}
	}



	TemplateIncludeExpression createInclude(final Object tmplname, final Object arglist)
	{
		return new TemplateIncludeExpression(tmplname.toString(),(ArrayList<Object>)arglist);
	}
}
