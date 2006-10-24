/*
 * Created on 2005-09-05
 */
package nu.mine.mosher.template.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.lexer.TemplateToken;
import nu.mine.mosher.template.parser.context.ContextStack;
import nu.mine.mosher.template.parser.context.TemplateParserContext;

/**
 * Parser to render a (tokenized) template, given optional arguments.
 *
 * @author Chris Mosher
 */
public class TemplateParser
{
	/** Internal variable used by IF statements */
	public static final String VAR_IF = "@if";
	/** Internal variable used by LOOP statements */
	public static final String VAR_LOOP_INDEX = "@index";
	/** Internal variable used by LOOP statements */
	public static final String VAR_LOOP_TIMES = "@times";
	/** Internal variable holding the arguments to the template */
	public static final String VAR_ARGS = "@args";
	/** Internal variable holding the URL of the template */
	public static final String VAR_URL = "@url";

	private final List<TemplateToken> rToken = new ArrayList<TemplateToken>();

	private final LinkedList<Integer> rPosParse = new LinkedList<Integer>();
	private final ContextStack stackContext = new ContextStack();

	/**
	 * Initializes this parser to use the given tokenized template (<code>tToken</code>),
	 * passing it the given arguments.
	 * @param rToken tokenized template
	 * @param rArg arguments to pass into the template
	 * @param url the URL of the template (which is used to locate any included templates)
	 */
	public TemplateParser(final List<TemplateToken> rToken, final List<Object> rArg, final URL url)
	{
		this.rToken.addAll(rToken);

		this.rPosParse.addFirst(0);

		final TemplateParserContext ctx = new TemplateParserContext();
		ctx.addVariable("true",true);
		ctx.addVariable("false",false);
		ctx.addVariable("null",null);
		ctx.addVariable(VAR_ARGS,rArg);
		ctx.addVariable(VAR_URL,url);

		this.stackContext.push(ctx);
	}

	/**
	 * Parses this template and appends the rendered result to the given <code>StringBuilder</code>.
	 * @param appendTo <code>StringBuilder</code> to append the result to
	 * @throws TemplateParsingException
	 */
	public void parse(final StringBuilder appendTo) throws TemplateParsingException
	{
		while (this.rPosParse.getFirst() < this.rToken.size())
		{
			final TemplateToken token = nextToken();
			token.parse(this,appendTo);
		}
	}

	private TemplateToken nextToken()
	{
		final TemplateToken token = this.rToken.get(this.rPosParse.getFirst());

		int pos = this.rPosParse.removeFirst();
		++pos;
		this.rPosParse.addFirst(pos);

		return token;
	}

	/**
	 * Saves the current parse position (on an internal stack).
	 * Used by some tokens.
	 */
	public void saveParsePosition()
	{
		this.rPosParse.addFirst(this.rPosParse.getFirst());
	}

	/**
	 * Restores the parse position (from the internal stack) that
	 * was saved by a previous call to <code>saveParsePosition</code>.
	 * Used by some tokens.
	 */
	public void restoreParsePosition()
	{
		this.rPosParse.removeFirst();
	}

	/**
	 * Discards the parse position (from the internal stack) that
	 * was saved by a previous call to <code>saveParsePosition</code>.
	 * Used by some tokens.
	 */
	public void forgetParsePosition()
	{
		this.rPosParse.remove(1);
	}

	/**
	 * Gets the stack of contexts used by this parser.
	 * @return the context stack
	 */
	public ContextStack getContext()
	{
		return this.stackContext;
	}
}
