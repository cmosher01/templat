/*
 * Created on Sep 5, 2005
 */
package nu.mine.mosher.template.parser;

import java.util.LinkedList;
import nu.mine.mosher.template.Templat;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.token.TemplateToken;

public class TemplateParser
{
	public static final String VAR_IF = "@if";
	public static final String VAR_LOOP_INDEX = "@index";
	public static final String VAR_LOOP_TIMES = "@times";
	public static final String VAR_ARGS = "@args";

	private final Templat template;

	private final LinkedList<Integer> rPosParse = new LinkedList<Integer>();
	private final ContextStack stackContext = new ContextStack();

	public TemplateParser(final Templat template)
	{
		this.template = template;

		this.rPosParse.addFirst(0);

		final TemplateParserContext ctx = new TemplateParserContext();
		ctx.addVariable("true",true);
		ctx.addVariable("false",false);
		ctx.addVariable("null",null);
		ctx.addVariable(VAR_ARGS,this.template.getArgs());

		this.stackContext.push(ctx);
	}

	public void parse(final StringBuilder appendTo) throws TemplateParsingException
	{
		while (this.rPosParse.getFirst() < this.template.getTokens().size())
		{
			final TemplateToken token = nextToken();
			token.parse(this,appendTo);
		}
	}

	private TemplateToken nextToken()
	{
		final TemplateToken token = this.template.getTokens().get(this.rPosParse.getFirst());

		int pos = this.rPosParse.removeFirst();
		++pos;
		this.rPosParse.addFirst(pos);

		return token;
	}

	public void saveParsePosition()
	{
		this.rPosParse.addFirst(this.rPosParse.getFirst());
	}

	public void restoreParsePosition()
	{
		this.rPosParse.removeFirst();
	}

	public ContextStack getContext()
	{
		return this.stackContext;
	}

	public Templat getTemplate()
	{
		return this.template;
	}
}
