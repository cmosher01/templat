/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.token.ElseToken;
import nu.mine.mosher.template.token.EndIfToken;
import nu.mine.mosher.template.token.EndLoopToken;
import nu.mine.mosher.template.token.IfToken;
import nu.mine.mosher.template.token.IncludeToken;
import nu.mine.mosher.template.token.LoopToken;
import nu.mine.mosher.template.token.StringToken;
import nu.mine.mosher.template.token.TemplateDeclarationToken;
import nu.mine.mosher.template.token.TemplateToken;
import nu.mine.mosher.template.token.ValueToken;

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

	private final StringBuilder template;

	private LexerState state = LexerState.IN_STRING;
	private int pos = 0;

	private StringBuilder strCurrent = new StringBuilder();

	private final Collection<TemplateToken> rToken;



	public TemplateLexer(final StringBuilder template, final Collection<TemplateToken> rToken)
	{
		this.template = template;
		this.rToken = rToken;
	}

	public void lex() throws TemplateLexingException
	{
		while (this.state != LexerState.END)
		{
			transition();
			advance();
		}
		this.state = LexerState.IN_STRING;
		this.pos = 0;
		this.strCurrent.setLength(0);
	}

	private void transition() throws TemplateLexingException
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
						this.rToken.add(new StringToken(this.strCurrent.toString()));
						this.strCurrent.setLength(0);
					}
					this.state = LexerState.IN_TAG;
				}
				else if (c == EOF)
				{
					if (this.strCurrent.length() > 0)
					{
						this.rToken.add(new StringToken(this.strCurrent.toString()));
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
//					else if (peek() == '@')
//					{
//						this.strCurrent.append(c);
//						advance();
//					}
					else
					{
						final String tag = this.strCurrent.toString();

						if (!matchKeyword(tag))
						{
							this.rToken.add(new ValueToken(tag));
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

	private char peek()
	{
		return getChar(this.pos+1);
	}

	private void advance()
	{
		++this.pos;
	}

	private void backup()
	{
		--this.pos;
	}

	private boolean matchKeyword(final String tag)
	{
		Matcher matcher;

		matcher = patTEMPLATE.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new TemplateDeclarationToken(matcher.group(1)));
			return true;
		}

		matcher = patIF.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new IfToken(matcher.group(1)));
			return true;
		}

		matcher = patELSE.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new ElseToken());
			return true;
		}

		matcher = patENDIF.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new EndIfToken());
			return true;
		}

		matcher = patLOOP.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new LoopToken(matcher.group(1)));
			return true;
		}

		matcher = patENDLOOP.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new EndLoopToken());
			return true;
		}

		matcher = patINCLUDE.matcher(tag);
		if (matcher.matches())
		{
			this.rToken.add(new IncludeToken(matcher.group(1)));
			return true;
		}

		return false;
	}
}
