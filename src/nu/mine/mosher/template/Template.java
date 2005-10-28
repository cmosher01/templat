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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.token.TemplateToken;

public class Template
{
	private final File fileTemplate;
	private final List<TemplateToken> rToken = new ArrayList<TemplateToken>();
	private final List<Object> rArg = new ArrayList<Object>();



	public Template(final File fileTemplate)
	{
		this.fileTemplate = fileTemplate;
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
	public void lex() throws TemplateLexingException, IOException
	{
		final StringBuilder sb = new StringBuilder((int)this.fileTemplate.length());
		appendTemplate(this.fileTemplate,sb);
		final TemplateLexer lexer = new TemplateLexer(sb,this.rToken);
		lexer.lex();
	}

	public void parse(final StringBuilder appendTo) throws TemplateLexingException, TemplateParsingException, IOException
	{
		final TemplateParser parser = new TemplateParser(this);
		parser.parse(appendTo);
	}

	private static void appendTemplate(final File file, final StringBuilder appendTo) throws IOException
	{
		final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		final StringWriter strBufTemplate = new StringWriter();
		final BufferedWriter bufTemplate = new BufferedWriter(strBufTemplate);

		for (String sLine = in.readLine(); sLine != null; sLine = in.readLine())
		{
			bufTemplate.write(sLine);
			bufTemplate.newLine();
		}
		bufTemplate.flush();
		bufTemplate.close();

		appendTo.append(strBufTemplate.getBuffer());
	}

	public List<Object> getArgs()
	{
		return Collections.unmodifiableList(this.rArg);
	}

	public File getFile()
	{
		return this.fileTemplate;
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
