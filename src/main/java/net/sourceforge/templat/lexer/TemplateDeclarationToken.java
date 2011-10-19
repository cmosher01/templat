/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;

import java.util.List;
import java.util.StringTokenizer;
import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;
import net.sourceforge.templat.parser.context.TemplateParserContext;

/**
 * <span class="directive">@&nbsp;<span class="keyword">template</span>&nbsp;<span class="var">template-name</span>(&nbsp;<span class="var">parameter1</span>,&nbsp;<span class="var">parameter2</span>,&nbsp;<span class="var">...</span>&nbsp;)&nbsp;@</span>
 *
 * @author Chris Mosher
 */
class TemplateDeclarationToken implements TemplateToken
{
	private final String tag;

	/**
	 * @param tag template definition after template keyword: "name(args)"
	 */
	public TemplateDeclarationToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "TEMPLATE DECLARATION: "+this.tag;
	}

	@Override
	public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
	{
		appendTo.getClass(); // nothing to append here
		final int posLeftParen = this.tag.indexOf('(');
		if (posLeftParen < 0)
		{
			throw new TemplateParsingException("Expecting ( in template definition: "+this.tag);
		}
		final int posRightParen = this.tag.indexOf(')');
		if (posRightParen < 0)
		{
			throw new TemplateParsingException("Expecting ) in template definition: "+this.tag);
		}

		@SuppressWarnings("unchecked")
		final List<Object> rArgs = (List<Object>)parser.getContext().getValue(TemplateParser.VAR_ARGS);

		final TemplateParserContext ctxNew = new TemplateParserContext();

		final String sArgList = this.tag.substring(posLeftParen+1,posRightParen);
		final StringTokenizer st = new StringTokenizer(sArgList,", ");

		if (st.countTokens() != rArgs.size())
		{
			throw new TemplateParsingException("Wrong number of arguments; got "+rArgs.size()+", expected "+st.countTokens());
		}

		int iArg = 0;
		while (st.hasMoreTokens())
		{
			final String argName = st.nextToken();
			ctxNew.addVariable(argName,rArgs.get(iArg));
			++iArg;
		}

		parser.getContext().push(ctxNew);
	}
}
