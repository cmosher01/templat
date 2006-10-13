/*
 * Created on Oct 11, 2006
 */
package nu.mine.mosher.template;

import static org.junit.Assert.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.uuid.UUIDFactory;
import org.junit.Test;

public class TemplatTest
{
	private static final UUIDFactory uuid = new UUIDFactory();

	@Test
	public void testEmpty() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected(""),templat(""));
	}

	@Test
	public void testA() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("A"),templat("A"));
	}

	@Test
	public void testAB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AB"),templat("AB"));
	}

	@Test
	public void testAnB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("A","B"),templat("A","B"));
	}

	@Test
	public void testABC() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("A","B","C"),templat("A","B","C"));
	}

	@Test
	public void testLoop1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("A"),templat("@loop i : 1@A@end loop@"));
	}

	@Test
	public void testLoop2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AA"),templat("@loop i : 2@A@end loop@"));
	}

	@Test
	public void testLoop4() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AB0CB1CB2CB3CD"),templat("A@loop i: 4@B@i@C@end loop@D"));
	}

	@Test
	public void testLoop20() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AAAAAAAAAAAAAAAAAAAA"),templat("@loop i : 20@A@end loop@"));
	}

	@Test
	public void testNestedLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AAAAAA"),templat("@loop i : 2@@loop j : 3@A@end loop@@end loop@"));
	}

	@Test
	public void testNestedLoop2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(
			expected(
				"A","","B","","C","","C","","D","","B","","C","","C","","D","","E"),
			templat(
				"A",
				"@loop a : 2@",
					"B",
					"@loop b : 2@",
						"C",
					"@end loop@",
					"D",
				"@end loop@",
				"E"));
	}

	@Test
	public void testIf() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("ABCEG"),templat("A@if (true)@B@if (true)@C@else@D@end if@E@else@F@end if@G"));
		noArgTest(expected("ABDEG"),templat("A@if (true)@B@if (false)@C@else@D@end if@E@else@F@end if@G"));
		noArgTest(expected("AFG"),templat("A@if (false)@B@if (true)@C@else@D@end if@E@else@F@end if@G"));
		noArgTest(expected("AFG"),templat("A@if (false)@B@if (false)@C@else@D@end if@E@else@F@end if@G"));

		noArgTest(expected("ABG"),templat("A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G"));
		noArgTest(expected("ABG"),templat("A@if (true)@B@else@C@if (false)@D@else@E@end if@F@end if@G"));
		noArgTest(expected("ACDFG"),templat("A@if (false)@B@else@C@if (true)@D@else@E@end if@F@end if@G"));
		noArgTest(expected("ACEFG"),templat("A@if (false)@B@else@C@if (false)@D@else@E@end if@F@end if@G"));
	}

	@Test
	public void testLoopIf() throws IOException, TemplateLexingException, TemplateParsingException
	{
		noArgTest(expected("AABGABGD"),templat("A@loop i: 2@A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G@end loop@D"));
	}

	private String templat(final String... lines) throws IOException
	{
		final StringWriter stringWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(stringWriter);
		for (final String line: lines)
		{
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		return stringWriter.toString();
	}

	private String expected(final String... lines) throws IOException
	{
		final StringWriter stringWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(stringWriter);
		writer.newLine(); // extra newline due to newline after @template@ declaration
		for (final String line: lines)
		{
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		return stringWriter.toString();
	}

	private void noArgTest(final String expected, final String template) throws IOException, TemplateLexingException, TemplateParsingException
	{
		File file = null;
		try
		{
			final String name = uuid.createUUID().toString().replace("-","_");
			file = new File(name).getCanonicalFile();
			file.deleteOnExit();
	
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			writer.write("@template "+name+"()@");
			writer.newLine();

			writer.write(template);
			writer.flush();
			writer.close();
	
			final Templat templat = new Templat(file.toURL());
	
			final StringBuilder sb = new StringBuilder(256);
			templat.parse(sb);
	
			assertEquals(expected,sb.toString());
		}
		finally
		{
			if (file != null)
			{
				file.delete();
			}
		}
	}
}
