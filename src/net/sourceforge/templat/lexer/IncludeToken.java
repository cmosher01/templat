/*
 * Created on Sep 5, 2005
 */
package net.sourceforge.templat.lexer;

import java.io.IOException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import net.sourceforge.templat.Templat;
import net.sourceforge.templat.exception.TemplateLexingException;
import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.Expression;
import net.sourceforge.templat.parser.TemplateParser;
import net.sourceforge.templat.parser.context.ContextStack;


class IncludeToken implements TemplateToken
{
	private final String template;
	private final String args;

	/**
	 * @param template 
	 * @param args 
	 */
	public IncludeToken(final String template, final String args)
	{
		this.template = template.trim();
		this.args = args;
	}

	@Override
	public String toString()
	{
		return "INCLUDE: "+this.template+" ("+this.args+")";
	}

	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		try
		{
			tryParse(parser,appendTo);
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

	private void tryParse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException, TemplateLexingException, IOException
	{
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
		{
			return;
		}

		final String nameInclude = this.template+".tat";
		final URL url = (URL)parser.getContext().getValue(TemplateParser.VAR_URL);
		final Templat templateInclude = new Templat(new URL(url,nameInclude));
		templateInclude.render(appendTo,splitArgs(parser.getContext()).toArray());
	}

	private ArrayList<Object> splitArgs(final ContextStack ctx) throws TemplateParsingException
	{
		final ArrayList<Object> rArg = new ArrayList<Object>();

		int parens = 0;
		final StringBuilder cur = new StringBuilder();

		final StringCharacterIterator iter = new StringCharacterIterator(this.args);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next())
		{
			if (parens == 0)
			{
				if (c == '(')
				{
					++parens;
					cur.append(c);
				}
				else if (c == ',')
				{
					eval(ctx,rArg,cur);
				}
				else
				{
					cur.append(c);
				}
			}
			else
			{
				if (c == '(')
				{
					++parens;
				}
				else if (c == ')')
				{
					--parens;
				}
				cur.append(c);
			}
		}
		eval(ctx,rArg,cur);
		return rArg;
	}

	private static void eval(final ContextStack ctx, final ArrayList<Object> rArg, final StringBuilder cur) throws TemplateParsingException
	{
		final String arg = cur.toString().trim();
		if (arg.length() > 0)
		{
			rArg.add(Expression.eval(arg,ctx));
		}
		cur.setLength(0);
	}
}
