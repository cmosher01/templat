/*
 * Created on Sep 5, 2005
 */
package nu.mine.mosher.template.token;

import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;
import nu.mine.mosher.template.Templat;
import nu.mine.mosher.template.TemplateParser;
import nu.mine.mosher.template.TemplateParserContext;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.Include;
import nu.mine.mosher.template.expr.TemplateIncludeExpression;


class IncludeToken extends TemplateToken
{
	private final String tag;

	public IncludeToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "INCLUDE: "+this.tag;
	}

	@Override
	public void parse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException
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

	private void tryParse(final TemplateParser parser, final StringBuilder appendTo) throws TemplateParsingException, TemplateLexingException, IOException
	{
		if (parser.getContext().isEverEqual(TemplateParser.VAR_IF,false))
		{
			return;
		}

		final TemplateIncludeExpression inclusion = Include.eval(this.tag,parser.getContext());

		final String nameInclude = inclusion.getTemplateName()+".tat";

		final Templat templateInclude = new Templat(new File(parser.getTemplate().getFile().getParent(),nameInclude));

		for (final Object argument : inclusion.getArgs())
		{
			templateInclude.addArg(argument);
		}

		templateInclude.lex();
		templateInclude.parse(appendTo);
	}
}
