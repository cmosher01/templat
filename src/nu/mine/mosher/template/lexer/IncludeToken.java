/*
 * Created on Sep 5, 2005
 */
package nu.mine.mosher.template.lexer;

import java.io.IOException;
import java.net.URL;
import nu.mine.mosher.template.Templat;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.Include;
import nu.mine.mosher.template.expr.TemplateIncludeExpression;
import nu.mine.mosher.template.parser.TemplateParser;


class IncludeToken implements TemplateToken
{
	private final String tag;

	/**
	 * @param tag
	 */
	public IncludeToken(final String tag)
	{
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return "INCLUDE: "+this.tag;
	}

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
		final URL url = (URL)parser.getContext().getValue(TemplateParser.VAR_URL);
		final Templat templateInclude = new Templat(new URL(url,nameInclude));
		templateInclude.parse(appendTo,inclusion.getArgs().toArray());
	}
}
