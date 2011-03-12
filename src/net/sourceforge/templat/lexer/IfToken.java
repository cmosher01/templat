/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.Expression;
import net.sourceforge.templat.parser.TemplateParser;
import net.sourceforge.templat.parser.context.TemplateParserContext;

/**
 * <span class="directive">@&nbsp;<span class="keyword">if</span>&nbsp;(&nbsp;<span class="var">boolean-expression</span>&nbsp;)&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class IfToken implements TemplateToken
{
	private final String tag;

	/**
	 * @param tag
	 */
	public IfToken(final String tag)
	{
		this.tag = tag.trim();
	}

	@Override
	public String toString()
	{
		return "IF: "+this.tag;
	}

	@Override
	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		try
		{
			tryParse(parser);
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

	private void tryParse(final TemplateParser parser) throws TemplateParsingException, ClassCastException
	{
		final Boolean bCond;
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,Boolean.FALSE))
		{
			bCond = Boolean.FALSE;
		}
		else
		{
			final Object objectCond = Expression.eval(this.tag,parser.getContext());
			bCond = (Boolean)objectCond; // could throw ClassCastException
		}

		final TemplateParserContext ctxNew = new TemplateParserContext();
		ctxNew.addVariable(TemplateParser.VAR_IF,bCond);

		parser.getContext().push(ctxNew);
	}
}
