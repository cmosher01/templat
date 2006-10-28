/*
 * Created on 2005-09-04
 */
package nu.mine.mosher.template.lexer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nu.mine.mosher.template.exception.TemplateLexingException;

/**
 * Lexer to analyze a given template and fully tokenize its contents.
 *
 * @author Chris Mosher
 */
public class TemplateLexer
{
	private static final char EOF = '\uFFFF';
	private static final Pattern patTEMPLATE = Pattern.compile("template\\s+(.+)");
	private static final Pattern patIF = Pattern.compile("if\\s+\\((.+)\\)");
	private static final Pattern patELSE = Pattern.compile("else");
	private static final Pattern patENDIF = Pattern.compile("end\\s+if");
	private static final Pattern patLOOP = Pattern.compile("loop\\s+(.+)");
	private static final Pattern patENDLOOP = Pattern.compile("end\\s+loop");
	private static final Pattern patINCLUDE = Pattern.compile("include\\s+(.+)");

	private final CharSequence template;

	private static enum LexerState
	{
		/** in a regular part of the file (outside of a tag */ IN_STRING,
		/** in a tag (flanked by at signs) */ IN_TAG,
		/** at the end of the file */ END,
	}

	private LexerState state = LexerState.IN_STRING;

	private int pos = 0;

	private StringBuilder strCurrent = new StringBuilder();



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
			advance();
		}
		this.state = LexerState.IN_STRING;
		this.pos = 0;
		this.strCurrent.setLength(0);
	}

	private void transition(final List<TemplateToken> rToken) throws TemplateLexingException
	{
		char c = getCurrent();

		switch (this.state)
		{
			case IN_STRING:
			{
				if (c == '@')
				{
					if (this.strCurrent.length() > 0)
					{
						rToken.add(new StringToken(this.strCurrent.toString()));
						this.strCurrent.setLength(0);
					}
					this.state = LexerState.IN_TAG;
				}
				else if (c == EOF)
				{
					if (this.strCurrent.length() > 0)
					{
						rToken.add(new StringToken(this.strCurrent.toString()));
						this.strCurrent.setLength(0);
					}
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
						this.state = LexerState.IN_STRING;
					}
					else
					{
						final String tag = this.strCurrent.toString();

						if (!matchKeyword(tag,rToken))
						{
							rToken.add(new ValueToken(tag));
						}
						this.state = LexerState.IN_STRING;
						this.strCurrent.setLength(0);
					}
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
				throw new IllegalStateException();
		}
	}

	private char getCurrent()
	{
		return getChar(this.pos);
	}
	private char getChar(final int posToGet)
	{
		if (posToGet >= this.template.length())
		{
			return EOF;
		}
		return this.template.charAt(posToGet);
	}

	private void advance()
	{
		++this.pos;
	}

	private boolean matchKeyword(final String tag, final List<TemplateToken> rToken)
	{
		Matcher matcher;

		matcher = patTEMPLATE.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new TemplateDeclarationToken(matcher.group(1)));
			return true;
		}

		matcher = patIF.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new IfToken(matcher.group(1)));
			return true;
		}

		matcher = patELSE.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new ElseToken());
			return true;
		}

		matcher = patENDIF.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new EndIfToken());
			return true;
		}

		matcher = patLOOP.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new LoopToken(matcher.group(1)));
			return true;
		}

		matcher = patENDLOOP.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new EndLoopToken());
			return true;
		}

		matcher = patINCLUDE.matcher(tag);
		if (matcher.matches())
		{
			rToken.add(new IncludeToken(matcher.group(1)));
			return true;
		}

		return false;
	}
}
