package net.sourceforge.templat.expr;

import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.exception.ExprLexingException;
import net.sourceforge.templat.expr.exception.ExprParsingException;
import net.sourceforge.templat.parser.context.ContextStack;

public abstract class ExprParserHelper {
	private final ExprLexer lexer;
	private final ExprActions act;

	/**
	 * @param input
	 *          the input string to parse and evaluate
	 * @param stackContext
	 *          the current context stack
	 */
	public ExprParserHelper(final String input, final ContextStack stackContext) {
		this.lexer = new ExprLexer(input);
		this.act = new ExprActions(stackContext);
	}

	abstract int yyparse() throws ExprLexingException, ExprParsingException, TemplateParsingException;

	abstract protected int getYychar();

	abstract protected Object getYyval();

	abstract protected void setYylval(final Object yylval);

	/**
	 * Parses the given string, using the grammar specified in the expr.yacc
	 * source file.
	 * 
	 * @return the resulting object
	 * @throws ExprLexingException
	 * @throws ExprParsingException
	 * @throws TemplateParsingException
	 */
	public Object parse() throws ExprLexingException, ExprParsingException, TemplateParsingException {
		try {
			final int err = yyparse();
			if (err != 0) {
				throw new ExprParsingException();
			}
		} catch (final ExprLexingException e) {
			throw e;
		} catch (final ExprParsingException e) {
			throw e;
		} catch (final TemplateParsingException e) {
			throw e;
		} catch (final Throwable e) {
			throw new ExprParsingException(e);
		}

		return getYyval();
	}

	protected void yyerror(final String s) throws ExprParsingException {
		throw new ExprParsingException(s + " at \'" + getCharForMessage(getYychar()) + "\' while parsing expression: " + this.lexer);
	}

	protected int yylex() throws ExprLexingException {
		final ExprLexer.Token token = this.lexer.getNextToken();

		setYylval(token.getTokenValue());
		if (token.getTokenType() == ExprParser.EOF) {
			return 0;
		}
		return token.getTokenType();
	}

	protected ExprActions actions() {
		return this.act;
	}

	private static String getCharForMessage(final int yychar) {
		if (yychar < 0x100) {
			return Character.toString((char) yychar);
		}

		if (yychar == ExprParser.EOF) {
			return "(EOF)";
		}

		if (yychar == ExprParser.DOT) {
			return ".";
		}

		if (yychar == ExprParser.COMMA) {
			return ",";
		}

		if (yychar == ExprParser.NUM) {
			return "(NUMBER)";
		}

		if (yychar == ExprParser.EOF) {
			return "(IDENT)";
		}

		return "(unknown)";
	}
}
