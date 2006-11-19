/*
 * Created on 2005-09-04
 */
package net.sourceforge.templat.lexer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.templat.exception.TemplateLexingException;

/**
 * Lexer to analyze a given template and fully tokenize its contents.
 * Pass the template to be lexed, as a {@link java.lang.CharSequence},
 * into the constructor. Then call {@link TemplateLexer#lex lex} to perform
 * the lexing.
 *
 * @author Chris Mosher
 */
public class TemplateLexer
{
	private static final char EOF = '\uFFFF';

	private static final Pattern patTEMPLATE = Pattern.compile("\\s*template\\s+(.+)\\s*");
	private static final Pattern patIF = Pattern.compile("\\s*if\\s+\\((.+)\\)\\s*");
	private static final Pattern patELSE = Pattern.compile("\\s*else\\s*");
	private static final Pattern patENDIF = Pattern.compile("\\s*end\\s+if\\s*");
	private static final Pattern patLOOP = Pattern.compile("\\s*loop\\s+(.+)\\s*");
	private static final Pattern patENDLOOP = Pattern.compile("\\s*end\\s+loop\\s*");
	private static final Pattern patINCLUDE = Pattern.compile("\\s*include\\s+([^\\(]+)\\((.*)\\)\\s*");



	private final CharSequence template;



	private static enum LexerState
	{
		/** in a regular part of the file (outside of a tag) */ IN_STRING,
		/** in a tag (flanked by at signs) */ IN_TAG,
		/** at the end of the file */ END,
	}

	private LexerState state = LexerState.IN_STRING;

	private int pos = 0;

	private final StringBuilder strCurrent = new StringBuilder();



	/**
	 * Initializes the lexer to read from the given template.
	 * @param template the contents of the template
	 */
	public TemplateLexer(final CharSequence template)
	{
		this.template = template;
	}

	/**
	 * Lexigraphically analyzes this lexer's template into tokens.
	 * The resulting tokens are appended to the given <code>List</code>.
	 * @param rToken <code>List</code> to append the tokens to
	 * @throws TemplateLexingException
	 */
	public void lex(final List<TemplateToken> rToken) throws TemplateLexingException
	{
		while (this.state != LexerState.END)
		{
			transition(rToken);
			++this.pos;
		}
		this.state = LexerState.IN_STRING;
		this.pos = 0;
		this.strCurrent.setLength(0);
	}

	private void transition(final List<TemplateToken> rToken) throws TemplateLexingException
	{
		final char c = getCurrent();

		switch (this.state)
		{
			case IN_STRING:
			{
				if (c == '@')
				{
					rToken.add(tokenizeString());
					this.state = LexerState.IN_TAG;
				}
				else if (c == EOF)
				{
					rToken.add(tokenizeString());
					this.state = LexerState.END;
				}
				else
				{
					this.strCurrent.append(c);
				}
			}
			break;

			case IN_TAG:
			{
				if (c == '@')
				{
					/*
					 * if we got "@@", it means just an "@" in a normal string
					 */
					if (this.strCurrent.length() == 0)
					{
						this.strCurrent.append(c);
					}
					else
					{
						rToken.add(tokenizeTag());
					}
					this.state = LexerState.IN_STRING;
				}
				else if (c == EOF)
				{
					throw new TemplateLexingException("missing terminating @.");
				}
				else
				{
					this.strCurrent.append(c);
				}
			}
			break;

			case END:
			{
				throw new IllegalStateException();
			}
		}
	}

	private char getCurrent()
	{
		try
		{
			return this.template.charAt(this.pos);
		}
		catch (final IndexOutOfBoundsException e)
		{
			return EOF;
		}
	}

	private TemplateToken tokenizeString()
	{
		final TemplateToken ret = new StringToken(this.strCurrent.toString());

		this.strCurrent.setLength(0);

		return ret;
	}
	private TemplateToken tokenizeTag()
	{
		final TemplateToken ret;
		Matcher matcher;

		if ((matcher = patTEMPLATE.matcher(this.strCurrent)).matches())
		{
			ret = new TemplateDeclarationToken(matcher.group(1));
		}
		else if ((matcher = patIF.matcher(this.strCurrent)).matches())
		{
			ret = new IfToken(matcher.group(1));
		}
		else if ((matcher = patELSE.matcher(this.strCurrent)).matches())
		{
			ret = new ElseToken();
		}
		else if ((matcher = patENDIF.matcher(this.strCurrent)).matches())
		{
			ret = new EndIfToken();
		}
		else if ((matcher = patLOOP.matcher(this.strCurrent)).matches())
		{
			ret = new LoopToken(matcher.group(1));
		}
		else if ((matcher = patENDLOOP.matcher(this.strCurrent)).matches())
		{
			ret = new EndLoopToken();
		}
		else if ((matcher = patINCLUDE.matcher(this.strCurrent)).matches())
		{
			ret = new IncludeToken(matcher.group(1),matcher.group(2));
		}
		else
		{
			ret = new ValueToken(this.strCurrent.toString());
		}

		this.strCurrent.setLength(0);

		return ret;
	}
}
