/*
 * Created on Sep 4, 2005
 */
package nu.mine.mosher.template;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
 * TemplAT is a template processor, used in conjunction with Java.
 * Templates contain processing instructions within at-signs, thus
 * the name TemplAT. Processing instructions include if-then-else,
 * loops, includes (with parameters), and (Java) method calls.
 *
 * @author Chris Mosher
 */
public class Templat
{
	private static final int K = 1024;
	private final URL url;
	private final List<TemplateToken> rToken = new ArrayList<TemplateToken>();
	private final List<Object> rArg = new ArrayList<Object>();



	public Templat(final URL url)
	{
		this.url = url;
	}

	public void addArg(final Object arg)
	{
		this.rArg.add(arg);
	}

	/**
	 * Lexigraphically analyze the template's file into a stream
	 * of tokens.
	 * @throws TemplateLexingException
	 * @throws IOException
	 */
	private void lex() throws TemplateLexingException, IOException
	{
		final StringBuilder sb = new StringBuilder(8*K);
		appendTemplate(this.url,sb);
		final TemplateLexer lexer = new TemplateLexer(sb,this.rToken);
		lexer.lex();
	}

	public void parse(final StringBuilder appendTo) throws TemplateLexingException, TemplateParsingException, IOException
	{
		lex();
		final TemplateParser parser = new TemplateParser(this);
		parser.parse(appendTo);
	}

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

	public List<Object> getArgs()
	{
		return Collections.unmodifiableList(this.rArg);
	}

	public URL getURL()
	{
		return this.url;
	}

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
}
