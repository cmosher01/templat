/*
 * Created on 2005-09-04
 */
package net.sourceforge.templat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.templat.exception.TemplateLexingException;
import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.lexer.TemplateLexer;
import net.sourceforge.templat.lexer.TemplateToken;
import net.sourceforge.templat.parser.TemplateParser;

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
	 * Parses this template. The arguments, if any, are passed to the template itself,
	 * and bound to the parameters specified in the template definition. The result of
	 * parsing the template is appended to the given <code>Appendable</code>.
	 * 
	 * @param appendTo the <code>Appendable</code> to append the result to
	 * @param rArg optional argument(s) to pass to the template
	 * @throws TemplateLexingException
	 * @throws TemplateParsingException
	 * @throws IOException
	 */
	public void parse(final Appendable appendTo, final Object... rArg) throws TemplateLexingException, TemplateParsingException, IOException
	{
		final StringBuilder sb = new StringBuilder(8192);
		appendTemplate(this.url,sb);

		final TemplateLexer lexer = new TemplateLexer(sb);
		final List<TemplateToken> rToken = new ArrayList<TemplateToken>();
		lexer.lex(rToken);

		final TemplateParser parser = new TemplateParser(rToken,Arrays.asList(rArg),this.url);
		parser.parse(appendTo);
	}



	/**
	 * Appends the contents of the given <code>URL</code> to
	 * the given <code>Appendable</code>.
	 * @param url
	 * @param appendTo
	 * @throws IOException
	 */
	private static void appendTemplate(final URL url, final Appendable appendTo) throws IOException
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
