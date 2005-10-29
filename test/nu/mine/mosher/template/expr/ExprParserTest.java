/*
 * Created on Sep 26, 2005
 */
package nu.mine.mosher.template.expr;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import nu.mine.mosher.template.parser.ContextStack;
import nu.mine.mosher.template.parser.TemplateParserContext;

/**
 * Command-line program that parses input lines.
 *
 * @author Chris Mosher
 */
public class ExprParserTest
{
	/**
	 * Command-line program that parses input lines.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
	    final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(FileDescriptor.in)));
	    for (String sLine = in.readLine(); sLine != null; sLine = in.readLine())
	    {
	        String result = "";
	        boolean ok = false;
	        try
	        {
	        	TemplateParserContext ct = new TemplateParserContext();
	        	ct.addVariable("a", "var_a");
	        	ct.addVariable("b", "var_b");
	        	ct.addVariable("c", new Integer(3));
	        	ContextStack ctx = new ContextStack();
	        	ctx.push(ct);
	        	final ExprParser parser = new ExprParser(sLine,ctx);
	            result = parser.parse().toString();
	            ok = true;
	        }
	        catch (final Throwable e)
	        {
	        	e.printStackTrace();
	            ok = false;
	        }

	        if (ok)
	        {
	            System.out.println(result);
	        }
	        else
	        {
	            System.err.println("error during parsing or lexing");
	        }
	    }
	}
}
