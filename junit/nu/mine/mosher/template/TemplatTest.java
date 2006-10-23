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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import nu.mine.mosher.template.exception.TemplateLexingException;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.uuid.UUIDFactory;
import org.junit.Ignore;
import org.junit.Test;

public class TemplatTest
{
	private static final UUIDFactory uuid = new UUIDFactory();
	private static final int NEWLINELENGTH;
	static
	{
		NEWLINELENGTH = System.getProperty("line.separator").length();
	}

	@Test
	public void testEmpty() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile(""));
	}

	@Test
	public void testA() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("A"));
	}

	@Test
	public void testAB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AB"),buildFile("AB"));
	}

	@Test
	public void testAnB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A","B"),buildFile("A","B"));
	}

	@Test
	public void testABC() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A","B","C"),buildFile("A","B","C"));
	}

	@Test
	public void testLoop1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@loop i : 1@A@end loop@"));
	}

	@Test
	public void testLoop2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AA"),buildFile("@loop i : 2@A@end loop@"));
	}

	@Test
	public void testLoop4() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AB0CB1CB2CB3CD"),buildFile("A@loop i: 4@B@i@C@end loop@D"));
	}

	@Test
	public void testLoop20() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AAAAAAAAAAAAAAAAAAAA"),buildFile("@loop i : 20@A@end loop@"));
	}

	@Test
	public void testNestedLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AAAAAA"),buildFile("@loop i : 2@@loop j : 3@A@end loop@@end loop@"));
	}

	@Test
	public void testNestedLoop2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(
			buildFile(
				"A","","B","","C","","C","","D","","B","","C","","C","","D","","E"),
			buildFile(
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
		assertTemplate(buildFile("ABCEG"),buildFile("A@if (true)@B@if (true)@C@else@D@end if@E@else@F@end if@G"));
		assertTemplate(buildFile("ABDEG"),buildFile("A@if (true)@B@if (false)@C@else@D@end if@E@else@F@end if@G"));
		assertTemplate(buildFile("AFG"),buildFile("A@if (false)@B@if (true)@C@else@D@end if@E@else@F@end if@G"));
		assertTemplate(buildFile("AFG"),buildFile("A@if (false)@B@if (false)@C@else@D@end if@E@else@F@end if@G"));

		assertTemplate(buildFile("ABG"),buildFile("A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G"));
		assertTemplate(buildFile("ABG"),buildFile("A@if (true)@B@else@C@if (false)@D@else@E@end if@F@end if@G"));
		assertTemplate(buildFile("ACDFG"),buildFile("A@if (false)@B@else@C@if (true)@D@else@E@end if@F@end if@G"));
		assertTemplate(buildFile("ACEFG"),buildFile("A@if (false)@B@else@C@if (false)@D@else@E@end if@F@end if@G"));
	}

	@Test
	public void testLoopIf() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AABGABGD"),buildFile("A@loop i: 2@A@if (true)@B@else@C@if (true)@D@else@E@end if@F@end if@G@end loop@D"));
	}

	@Test
	public void testIfTrueLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("ABCCDH"),buildFile("A@if (true)@B@loop i: 2@C@end loop@D@else@E@loop i: 2@F@end loop@G@end if@H"));
	}

	@Test
	public void testIfFalseLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AEFFGH"),buildFile("A@if (false)@B@loop i: 2@C@end loop@D@else@E@loop i: 2@F@end loop@G@end if@H"));
	}

	@Test
	public void testLoop0() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AD"),buildFile("A@loop i: 0@C@end loop@D"));
	}

	@Test
	public void testLoopNegative() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AD"),buildFile("A@loop i: arg0@C@end loop@D"),-1);
	}

	@Test(expected=TemplateParsingException.class)
	public void testMissingIf() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("A@else@B"));
	}

	@Ignore("Syntax error of else in loop isn't caught")
	@Test(expected=TemplateParsingException.class)
	public void testMissingIf2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("ADE"),buildFile("A@loop i: 0@C@else@D@end loop@E"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testJustAnEndIf() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@end if@"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testJustAnElse() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@else@"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testJustAnEndLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@end loop@"));
	}

	@Ignore("Syntax error of missing end loop isn't caught")
	@Test(expected=TemplateParsingException.class)
	public void testNoEndLoop() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("ABC"),buildFile("@loop i : 5@ABC"));
	}

	@Test
	public void testLiteralNumber() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A3B"),buildFile("A@3@B"));
	}

	@Test
	public void testSpacesAroundExpr() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@ 3 @"));
	}

	@Test
	public void testSpacesInExprParen() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@( 3 )@"));
	}

	@Test
	public void testSpacesInExprBool() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("true"),buildFile("@! false@"));
	}

	@Test
	public void testSpacesInExprArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[ 0 ]@"),(Object)new String[]{"A"});
	}

	public static class XYZ { public int sum(int a, int b) { return a+b; } }
	@Test
	public void testSpacesInExprMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@arg0.sum( 1 , 2 )@"),new XYZ());
	}

	@Ignore("Literal negative numbers aren't handled.")
	@Test
	public void testLiteralNegativeNumber() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A-3B"),buildFile("A@-3@B"));
	}

	@Test
	public void testLiteralNumberBig() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A2147483647B"),buildFile("A@2147483647@B"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testLiteralNumberTooBig() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("A@2147483648@B"));
	}

	@Test
	public void test1A() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0@"),"A");
	}

	@Test
	public void test1AB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AB"),buildFile("@arg0@"),"AB");
	}

	@Test
	public void test2AB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AB"),buildFile("@arg0@@arg1@"),"A","B");
	}

	@Test
	public void test1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("1"),buildFile("@arg0@"),1);
	}

	@Test
	public void testNegative1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("-1"),buildFile("@arg0@"),-1);
	}

	@Test
	public void test10AB() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("ABCDEFGHIJ"),buildFile("@arg0@@arg1@@arg2@@arg3@@arg4@@arg5@@arg6@@arg7@@arg8@@arg9@"),"A","B","C","D","E","F","G","H","I","J");
	}

	public static class X { public String get() { return "A"; } };
	@Test
	public void testExprMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get()@"),new X());
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprMethodBad() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.badget()@"),new X());
	}

	@Test
	public void testExprArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0]@"),(Object)new String[]{"A"});
	}

	@Test
	public void testExprList() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0]@"),Collections.<String>singletonList("A"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprArrayIndexOutOfBounds() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[1]@"),(Object)new String[]{"A"});
	}

	public static class Y { public X get() { return new X(); } };
	@Test
	public void testExprMethodMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get().get()@"),new Y());
	}

	public static class Z { public String[] get() { return new String[] {"A"}; } };
	@Test
	public void testExprMethodArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get()[0]@"),new Z());
	}

	public static class W { public List<String> get() { return Collections.<String>singletonList("A"); } };
	@Test
	public void testExprMethodList() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get()[0]@"),new W());
	}

	@Test
	public void testExprArrayMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0].get()@"),(Object)new X[]{new X()});
	}

	@Test
	public void testExprArrayArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0][0]@"),(Object)new String[][]{new String[]{"A"}});
	}

	@Test
	public void testExprArrayList() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0][0]@"),(Object)new List[]{Collections.<String>singletonList("A")});
	}

	@Test
	public void testExprListMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0].get()@"),Collections.<X>singletonList(new X()));
	}

	@Test
	public void testExprListArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0][0]@"),Collections.<String[]>singletonList(new String[]{"A"}));
	}

	@Test
	public void testExprListList() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0[0][0]@"),Collections.<List<String>>singletonList(Collections.<String>singletonList("A")));
	}

	public static class Y2 { public Y get() { return new Y(); } };
	@Test
	public void testExprMethodMethodMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get().get().get()@"),new Y2());
	}

	@Test
	public void testExprTenTimesMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0.get().toString().toString().toString().toString().toString().toString().toString().toString().toString()@"),new X());
	}

	@Test
	public void testExprArrayLength() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@arg0.length()@"),(Object)new String[]{"A","B","C"});
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprArrayLengthBad() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.length(0)@"),(Object)new String[]{"A","B","C"});
	}

	@Test
	public void testExprArraySize() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@arg0.size()@"),(Object)new String[]{"A","B","C"});
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprArraySizeBad() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.size(0)@"),(Object)new String[]{"A","B","C"});
	}

	@Test
	public void testExprListLength() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@arg0.length()@"),(Object)Collections.<String>nCopies(3,"A"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprListLengthBad() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.length(0)@"),(Object)Collections.<String>nCopies(3,"A"));
	}

	@Test
	public void testExprListSize() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("3"),buildFile("@arg0.size()@"),(Object)Collections.<String>nCopies(3,"A"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprListSizeBad() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.size(0)@"),(Object)Collections.<String>nCopies(3,"A"));
	}

	public static class Q { public String toString() { return "A"; } };
	@Test
	public void testToString() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A"),buildFile("@arg0@"),new Q());
	}

	@Test
	public void testNullValue() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("AB"),buildFile("A@arg0@B"),(Object)null);
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprNullMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.toString()@"),(Object)null);
	}

	@Test(expected=TemplateParsingException.class)
	public void testExprNullArray() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0[0]@"),(Object)null);
	}

	@Test(expected=TemplateParsingException.class)
	public void testNullConst() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@null.toString()@"));
	}

	@Test(expected=TemplateLexingException.class)
	public void testUnbalancedAt() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("A@B"));
	}

	@Test
	public void testAt() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("A@B"),buildFile("A@@B"));
	}

	@Test
	public void testPackage() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("true"),buildFile("@java.lang.String.valueOf(true)@"));
	}

	@Test
	public void testPackage2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("true"),buildFile("@java.lang.Boolean.parseBoolean(java.lang.Boolean.toString(true))@"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testSimpleSyntaxError() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@x@"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testGetClassNotSupported() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@arg0.getClass()@"),new X());
	}

	@Test(expected=TemplateParsingException.class)
	public void testClassLiteralNotSupported() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("java.lang.String"),buildFile("@java.lang.String.class.getName()@"));
	}

	@Test
	public void testBigFile() throws IOException, TemplateLexingException, TemplateParsingException
	{
		final Random rand = new Random();
		final int N = 16*1024;
		final StringBuilder sb = new StringBuilder(N);
		for (int i = 0; i < N; ++i)
		{
			sb.append('A'+rand.nextInt(26));
		}
		final String s = sb.toString();
		assertTemplate(buildFile(s),buildFile(s));
	}

	@Test
	public void testTypicalListAccess() throws IOException, TemplateLexingException, TemplateParsingException
	{
		final List<String> things = new ArrayList<String>();
		Collections.<String>addAll(things,"A","B","C","D","E","F","G");
		assertTemplate(buildFile("0A1B2C3D4E5F6G"),buildFile("@loop i : arg0.size()@@i@@arg0[i]@@end loop@"),(Object)things);
	}

	@Test
	public void testTypicalArrayAccess() throws IOException, TemplateLexingException, TemplateParsingException
	{
		final List<String> things = new ArrayList<String>();
		Collections.<String>addAll(things,"A","B","C","D","E","F","G");
		assertTemplate(buildFile("0A1B2C3D4E5F6G"),buildFile("@loop i : arg0.size()@@i@@arg0[i]@@end loop@"),(Object)things.toArray());
	}

	@Test
	public void testTrue() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("true"),buildFile("@true@"));
	}

	@Test
	public void testFalse() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("false"),buildFile("@false@"));
	}

	@Test
	public void testNotTrue() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("false"),buildFile("@!true@"));
	}

	@Test
	public void testNotFalse() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("true"),buildFile("@!false@"));
	}

	@Test
	public void testNull() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@null@"));
	}

	@Test
	public void testParens1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("99"),buildFile("@(99)@"));
	}

	@Test
	public void testParens2() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("99"),buildFile("@((99))@"));
	}

	@Test
	public void testParens20() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("99"),buildFile("@((((((((((((((((((((99))))))))))))))))))))@"));
	}

	@Test
	public void testArabicDigit() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("2"),buildFile("@\u0662@"));
	}

	@Test(expected=TemplateParsingException.class)
	public void testNullArg() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile(""),buildFile("@java.lang.System.getProperty(null)@"));
	}

	public static class Overload
	{
		public String a(Object x) { return "a(Object)"; }
		public String a(String x) { return "a(String)"; }
	}
	public class A { public A() {} public String toString() { return "a"; } }

	@Ignore("It is undefined which overloaded method will be called.")
	@Test
	public void testOverloadA() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplate(buildFile("a(Object)"),buildFile("@arg0.a(arg1)@"),new Overload(),new String("some string"));
	}

	@Test
	public void testIncludeSimple() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplateInclude(buildFile("A"),buildFile("@include XXX()@"),buildFile("A"),0);
	}

	@Test
	public void testIncludeArg1() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplateInclude(buildFile("A"),buildFile("@include XXX(arg0)@"),buildFile("@arg0@"),1,"A");
	}

	@Test
	public void testInclMethodMethodMethod() throws IOException, TemplateLexingException, TemplateParsingException
	{
		assertTemplateInclude(buildFile("A"),buildFile("@include XXX(arg0.get().get().get())@"),buildFile("@arg0@"),1,new Y2());
	}










	private static String buildFile(final String... lines) throws IOException
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

	private void assertTemplate(final String expected, final String template, final Object... args) throws IOException, TemplateLexingException, TemplateParsingException
	{
		File file = null;
		try
		{
			file = buildTemplateFile(template,args.length);

			final Templat templat = new Templat(file.toURL());

			final StringBuilder sb = new StringBuilder(256);
			templat.parse(sb,args);
	
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

	private void assertTemplateInclude(final String expected, final String template, final String includedTemplate, final int cArg, final Object... args) throws IOException, TemplateLexingException, TemplateParsingException
	{
		File fileIncluded = null;
		File file = null;
		try
		{
			fileIncluded = buildTemplateFile(includedTemplate,cArg);
			final String includedName = fileIncluded.getName();
			final String uuidname = includedName.substring(0,includedName.length()-4);

			file = buildTemplateFile(template.replaceFirst("include XXX","include "+uuidname),args.length);

			final Templat templat = new Templat(file.toURL());
	
			final StringBuilder sb = new StringBuilder(256);
			templat.parse(sb,args);
	
			for (int i = 0; i < NEWLINELENGTH; ++i)
			{
				sb.deleteCharAt(sb.length()-1);
			}
			assertEquals(expected,sb.toString());
		}
		finally
		{
			if (fileIncluded != null)
			{
				fileIncluded.delete();
			}
			if (file != null)
			{
				file.delete();
			}
		}
	}

	private File buildTemplateFile(final String template, final int cArg) throws IOException
	{
		final String name = "TEMPLATE"+uuid.createUUID().toString().replace("-","_");
		final File file = new File(name+".tat").getCanonicalFile();
		file.deleteOnExit();

		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		writer.write("@template "+name+"(");
		boolean first = true;
		for (int arg = 0; arg < cArg; ++arg)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				writer.write(",");
			}
			writer.write("arg"+arg);
		}
		writer.write(")@");

		writer.write(template);
		writer.flush();
		writer.close();

		return file;
	}
}
