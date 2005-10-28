

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import nu.mine.mosher.template.Template;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;

/**
 * Demonstrates how to use the <code>Template</code> class.
 *
 * @author Chris Mosher
 */
public class TemplateApp
{
	public static class Favorite
	{
		private final String name;
		private final String status;
		public Favorite(final String name, final String status) { this.name = name; this.status = status; }
		public String getName() { return this.name; }
		public String getStatus() { return this.status; }
	}

	public static class ConfigTest
	{
		ConfigTest() {}
		public boolean showOther() { return true; }
		public int[] getArray() { return new int[] { 3,5,7,9 }; }
		public String getColor() { return "red"; }
	}

	public static class Copyright
	{
		private final String who;
		private final int year;
		Copyright(String who, int year) { this.who = who; this.year = year; }
		public String getWhoWrote() { return this.who; }
		public int getYearBegun() { return this.year; }
	}

	public void run() throws IOException, TemplateLexingException, TemplateParsingException
	{
		final Template templateMinimal = new Template(new File("test/minimal.tat"));
		showTemplate(templateMinimal);

		final Template templateSimple = new Template(new File("test/simple.tat"));
		templateSimple.addArg("Richard Roe");
		templateSimple.addArg("defendant");
		showTemplate(templateSimple);

		final Template templateCopyright = new Template(new File("test/copyright.tat"));
		templateCopyright.addArg(2005);
		templateCopyright.addArg("Chris Mosher");
		templateCopyright.addArg(new ConfigTest());
		showTemplate(templateCopyright);

		final Template templateTestInclude = new Template(new File("test/testinc.tat"));
		templateTestInclude.addArg(2005);
		templateTestInclude.addArg("Chris Mosher");
		templateTestInclude.addArg(new ConfigTest());
		showTemplate(templateTestInclude);

		final Template templateTestLoop = new Template(new File("test/testloop.tat"));
		templateTestLoop.addArg("John Q. Public");
		final Collection<Copyright> cr = new ArrayList<Copyright>();
		cr.add(new Copyright("Chris Mosher",2005));
		cr.add(new Copyright("Jesse Sands",1790));
		templateTestLoop.addArg(cr);
		templateTestLoop.addArg(new ConfigTest());
		showTemplate(templateTestLoop);



		final Template templateHomepage = new Template(new File("test/homepage.tat"));

		templateHomepage.addArg("Chris Mosher");

		templateHomepage.addArg("good");

		final Collection<Favorite> rFav = new ArrayList<Favorite>();
		rFav.add(new Favorite("Google","used"));
		rFav.add(new Favorite("Java","unused"));
		templateHomepage.addArg(rFav);

		templateHomepage.addArg(7);

		templateHomepage.addArg(false);

		showTemplate(templateHomepage);
	}

	private void showTemplate(final Template template) throws TemplateLexingException, IOException, TemplateParsingException
	{
		final StringBuilder sbResult = new StringBuilder();
		template.lex();

		boolean ok = false;
		try
		{
			template.parse(sbResult);
			ok = true;
		}
		finally
		{
			if (ok)
			{
				System.out.println(sbResult);
			}
			else
			{
				System.err.println(template.toString());
			}
			System.out.println("------------------------------------------------------------------");
		}
	}
}
