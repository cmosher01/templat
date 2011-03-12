/*
 * Created on 2006-06-01
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import net.sourceforge.templat.Templat;
import net.sourceforge.templat.exception.TemplateLexingException;
import net.sourceforge.templat.exception.TemplateParsingException;

/**
 * Main command-line program for the <code>Templat</code> class.
 * Calling the template engine from the command line is only marginally useful.
 *
 * @author Chris Mosher
 */
public class TemplatMain
{
	/**
	 * @param rArg
	 * @throws IOException 
	 * @throws TemplateParsingException 
	 * @throws TemplateLexingException 
	 */
	public static void main(final String... rArg) throws IOException, TemplateLexingException, TemplateParsingException
	{
		if (rArg.length != 1)
		{
			throw new IllegalArgumentException("usage: java -jar templat.jar TEMPLATE_FILE");
		}

		final File fileTemplat = new File(rArg[0]).getCanonicalFile();
		final Templat templat = new Templat(fileTemplat.toURI().toURL());

		final StringBuilder sbResult = new StringBuilder();
		templat.render(sbResult);

		final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out),"UTF-8"));
		out.write(sbResult.toString());
		out.flush();
		out.close();
	}
}
