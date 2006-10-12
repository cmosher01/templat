/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.parser.TemplateParser;
import nu.mine.mosher.template.token.TemplateLexer;
import nu.mine.mosher.template.token.TemplateToken;

/**
 * <code>TemplAT</code> is a template processor, used in conjunction with Java.
 * Templates contain processing instructions within at-signs, thus
 * the name TemplAT. Processing instructions include if-then-else,
 * loops, includes (with parameters), and (Java) method calls.
 *
 * @author Chris Mosher
 */
public class Templat
{
	private final URL url;
	private final List<Object> rArg = new ArrayList<Object>();
	private final List<TemplateToken> rToken = new ArrayList<TemplateToken>();



	/**
	 * Initializes this <code>Templat</code> to read from the
	 * given <code>URL</code>. The contents of the <code>URL</code>
	 * are assumed to be in UTF-8 format.
	 * @param url the <code>URL</code> to read the template from (UTF-8)
	 */
	public Templat(final URL url)
	{
		this.url = url;
	}

	/**
	 * Gets the <code>URL</code> of this <code>Templat</code> (as
	 * passed into the constructor).
	 * @return the <code>URL</code>.
	 */
	public URL getURL()
	{
		return this.url;
	}

	/**
	 * Specifies the next argument for this <code>templat</code>.
	 * You must specify all of this template's arguments, in
	 * the correct order (by calling this method repeatedly),
	 * before calling <code>parse</code>.
	 * @param arg
	 */
	public void addArg(final Object arg)
	{
		this.rArg.add(arg);
	}

	/**
	 * Gets (an immutable wrapper for) this template's arguments,
	 * as previously passed into <code>addArg</code>.
	 * @return list of arguments
	 */
	public List<Object> getArgs()
	{
		return Collections.unmodifiableList(this.rArg);
	}

	/**
	 * Parses this template. You must call <code>addArg</code> for
	 * each of this template's arguments before calling this method.
	 * @param appendTo the <code>StringBuilder</code> to append the result to
	 * @throws TemplateLexingException
	 * @throws TemplateParsingException
	 * @throws IOException
	 */
	public void parse(final StringBuilder appendTo) throws TemplateLexingException, TemplateParsingException, IOException
	{
		lex();
		final TemplateParser parser = new TemplateParser(this);
		parser.parse(appendTo);
	}

	/**
	 * Gets (an immutable wrapper for) a list of this template's tokens.
	 * You must call <code>parse</code> before calling this method.
	 * @return this template's tokens
	 */
	public List<TemplateToken> getTokens()
	{
		return Collections.unmodifiableList(this.rToken);
	}

	/**
	 * For debugging purposes only.
	 * @return dump of this Template
	 */
	@Override
	public String toString()
	{
		final StringWriter stringWriter = new StringWriter();
		final BufferedWriter buf = new BufferedWriter(stringWriter);

		try
		{
			buf.write("TEMPLATE tokens: "); buf.newLine();
			for (final TemplateToken token: this.rToken)
			{
				buf.write(token.toString()); buf.newLine();
			}
			buf.flush();
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(e);
		}

		return stringWriter.toString();
	}



	/**
	 * Lexigraphically analyze the template's file into a stream
	 * of tokens.
	 * @throws TemplateLexingException
	 * @throws IOException
	 */
	private void lex() throws TemplateLexingException, IOException
	{
		final StringBuilder sb = new StringBuilder(8192);
		appendTemplate(this.url,sb);
		final TemplateLexer lexer = new TemplateLexer(sb,this.rToken);
		lexer.lex();
	}

	/**
	 * Appends the contents of the given <code>URL</code> to
	 * the given <code>StringBuilder</code>.
	 * @param url
	 * @param appendTo
	 * @throws IOException
	 */
	private static void appendTemplate(final URL url, final StringBuilder appendTo) throws IOException
	{
		final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));

		final StringWriter strBufTemplate = new StringWriter();
		final BufferedWriter bufTemplate = new BufferedWriter(strBufTemplate);

		for (String sLine = in.readLine(); sLine != null; sLine = in.readLine())
		{
			bufTemplate.write(sLine);
			bufTemplate.newLine();
		}
		bufTemplate.flush();
		bufTemplate.close();
		in.close();

		appendTo.append(strBufTemplate.getBuffer());
	}
}
