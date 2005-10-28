/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template.token;

import java.util.List;
import java.util.StringTokenizer;
import nu.mine.mosher.template.TemplateParser;
import nu.mine.mosher.template.TemplateParserContext;
import nu.mine.mosher.template.exception.TemplateParsingException;

public class TemplateDeclarationToken extends TemplateToken
{
	private final String tag;

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
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
	{
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
