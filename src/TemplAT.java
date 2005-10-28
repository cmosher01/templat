/*
 * Created on Sep 4, 2005
 */
import java.io.IOException;
import nu.mine.mosher.template.TemplateApp;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;

/**
 * TemplAT is a template processor, used in conjunction with Java.
 * Templates contain processing instructions within at-signs, thus
 * the name TemplAT. Processing instructions include if-then-else,
 * loops, includes (with parameters), and (Java) method calls.
 *
 * @author Chris Mosher
 */
public class TemplAT
{
	/**
	 * @param args
	 * @throws IOException 
	 * @throws TemplateLexingException 
	 * @throws TemplateParsingException 
	 */
	public static void main(String[] args) throws IOException, TemplateLexingException, TemplateParsingException
	{
		new TemplateApp().run();
	}
}
