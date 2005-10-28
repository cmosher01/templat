/*
 * Created on Sep 9, 2005
 */
package nu.mine.mosher.template;

import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import junit.framework.TestCase;

public class TemplateParserTest extends TestCase
{
//	public void testIfElse() throws TemplateLexingException, TemplateParsingException
//	{
//		should("ABCEG","A@if (true)@B@if (true)@C@else@D@end if@E@else@F@end if@G");
//		should("ABDEG","A@if (true)@B@if (false)@C@else@D@end if@E@else@F@end if@G");
//		should("AFG","A@if (false)@B@if (true)@C@else@D@end if@E@else@F@end if@G");
//		should("AFG","A@if (false)@B@if (false)@C@else@D@end if@E@else@F@end if@G");
//
//		should("ABG","A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G");
//		should("ABG","A@if (true)@B@else@C@if (false)@D@else@E@end if@F@end if@G");
//		should("ACDFG","A@if (false)@B@else@C@if (true)@D@else@E@end if@F@end if@G");
//		should("ACEFG","A@if (false)@B@else@C@if (false)@D@else@E@end if@F@end if@G");
//	}
//
//	public void testLoop() throws TemplateLexingException, TemplateParsingException
//	{
//		should("AB0CB1CB2CB3CD","A@loop i: 4@B@i@C@end loop@D");
//
//		should("AABGABGD","A@loop i: 2@A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G@end loop@D");
//	}
//
//	private void should(final String result, final String sTemplate) throws TemplateLexingException, TemplateParsingException
//	{
//		final Template template = new Template(new StringBuilder(sTemplate));
//		final StringBuilder sbResult = new StringBuilder();
//		template.parse(sbResult);
//		assertEquals(result,sbResult.toString());
//	}
}
