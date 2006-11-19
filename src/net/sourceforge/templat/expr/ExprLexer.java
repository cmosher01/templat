/*
 * Created on 2005-09-26
 */
package net.sourceforge.templat.expr;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import net.sourceforge.templat.expr.exception.ExprLexingException;

/**
 * Splits a string into tokens.
 *
 * @author Chris Mosher
 */
class ExprLexer
{
	private final String stringToLex;
	private final Buffer buffer;

	/**
	 * Initializes this <code>ExprLexer</code> to split
	 * the given string into tokens.
	 * @param stringToLex
	 */
	public ExprLexer(final String stringToLex)
	{
		this.stringToLex = stringToLex;
		this.buffer = new Buffer(stringToLex);
	}

	/**
	 * Gets the string being lexed, as passed into the constructor.
	 * @return the string being lexed
	 */
	@Override
	public String toString()
	{
		return this.stringToLex;
	}

	/**
	 * Gets the next token from the input string.
	 * @return the next <code>Token</code>
	 * @throws ExprLexingException
	 */
	public Token getNextToken() throws ExprLexingException
	{
	    final char c = this.buffer.getChar();
	 
	    if (c == '\uFFFF')
	    {
	        return new Token(ExprParser.EOF,"<EOF>");
	    }
	 
	    if (c == '.')
	    {
	        return new Token(ExprParser.DOT,".");
	    }
	 
	    if (c == ',')
	    {
	        return new Token(ExprParser.COMMA,",");
	    }
	 
	    if (Character.isDigit(c))
	    {
	        char c2 = c;
	        final StringBuffer sb = new StringBuffer();
	        while (Character.isDigit(c2))
	        {
	            sb.append(c2);
	            c2 = this.buffer.getChar();
	        }
	        this.buffer.ungetChar(c2);
	        return new Token(ExprParser.NUM,Integer.valueOf(sb.toString()));
	    }
	 
	    if (Character.isJavaIdentifierStart(c))
	    {
	        char c2 = c;
	        final StringBuffer sb = new StringBuffer();
	        while (Character.isJavaIdentifierPart(c2))
	        {
	            sb.append(c2);
	            c2 = this.buffer.getChar();
	        }
	        this.buffer.ungetChar(c2);
	        return new Token(ExprParser.ID,sb.toString());
	    }

	    if (Character.isWhitespace(c))
	    {
	        char c2 = c;
	        final StringBuffer sb = new StringBuffer();
	        while (Character.isWhitespace(c2))
	        {
	            sb.append(c2);
	            c2 = this.buffer.getChar();
	        }
	        this.buffer.ungetChar(c2);
	        return new Token(ExprParser.WS,sb.toString());
	    }

	    return new Token(c);
	}

	/**
	 * Represents a token returned by the enclosing <code>ExprLexer</code>.
	 *
	 * @author Chris Mosher
	 */
	static class Token
	{
		private final short tokenType;
		private final Object tokenValue;

		Token(final char c)
		{
			this.tokenType = (short)c;
			this.tokenValue = c;
		}

		Token(final short tokenType, final Object tokenValue)
		{
			this.tokenType = tokenType;
			this.tokenValue = tokenValue;
		}

		/**
		 * Gets this token's type.
		 * @return type
		 */
		public short getTokenType()
		{
			return this.tokenType;
		}

		/**
		 * Gets this token's value.
		 * @return value
		 */
		public Object getTokenValue()
		{
			return this.tokenValue;
		}
	}

	private static class Buffer
	{
		private final PushbackReader reader;
		private Buffer(final String source)
		{
			this.reader = new PushbackReader(new StringReader(source));
		}

		private char getChar() throws ExprLexingException
		{
		    try
			{
				return(char)this.reader.read();
			}
			catch (IOException e)
			{
			    throw new ExprLexingException(e);
			}
		}

		private void ungetChar(final char c) throws ExprLexingException
		{
	        try
			{
				this.reader.unread(c);
			}
			catch (IOException e)
			{
			    throw new ExprLexingException(e);
			}
		}
	}
}
