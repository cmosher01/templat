// This file needs to be compiled with BYACC/J using the following command:
// yacc -J -Jclass=ExprParser -Jsemantic=Object -Jpackage=net.sourceforge.templat.expr -Jthrows="ExprLexingException, ExprParsingException, TemplateParsingException" -Jnorun -Jnoconstruct -Jnodebug expr.yacc

%{
import net.sourceforge.templat.parser.context.ContextStack;
import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.exception.ExprLexingException;
import net.sourceforge.templat.expr.exception.ExprParsingException;



/**
 * Parses an expression.
 */
%}


%token EOF
%token DOT
%token COMMA
%token NUM
%token ID
%token WS



%%


expression
	: '!' S expression { $$ = !(Boolean)$3; }
	| '(' S expression S ')' { $$ = $3; }
	| literal
	| name { $$ = act.applySelectors($1,act.createList()); }
	| name selectors { $$ = act.applySelectors($1,$2); }
	;

selectors
	: selectors selector { $$ = act.addToList($2,$1); }
	| selector { $$ = act.addToList($1,act.createList()); }
	;

selector
	: DOT identifier args { $$ = act.createMethodCall($2,$3); }
	| '[' S expression S ']' { $$ = act.createArraySubscript($3); }
	;

args
	: '(' S arg_list S ')' { $$ = $3; }
	;

arg_list
	: arg_list S COMMA S expression { $$ = act.addToList($5,$1); }
	| expression { $$ = act.addToList($1,act.createList()); }
	| /* empty */ { $$ = act.createList(); }
	;

name
	: name DOT identifier { $$ = $1+"."+$3; }
	| identifier
	;

identifier
	: ID
	;

literal
	: NUM
	;

S
	: WS
	| /* empty */
	;

%%


private final ExprLexer lexer;
private final ExprActions act;

/**
 * @param input
 * @param stackContext
 */
public ExprParser(final String input, final ContextStack stackContext)
{
    this.lexer = new ExprLexer(input);
    this.act = new ExprActions(stackContext);
}

/**
 * Parses the given string with using the grammar
 * specified in the .yacc source file.
 * @return the resulting object
 * @throws ExprLexingException
 * @throws ExprParsingException
 * @throws TemplateParsingException
 */
public Object parse() throws ExprLexingException, ExprParsingException, TemplateParsingException
{
    try
    {
        final int err = yyparse();
        if (err != 0)
        {
            throw new ExprParsingException();
        }
    }
    catch (final ExprLexingException e)
    {
    	throw e;
   	}
    catch (final ExprParsingException e)
    {
    	throw e;
   	}
    catch (final TemplateParsingException e)
    {
    	throw e;
   	}
    catch (final Throwable e)
    {
        throw new ExprParsingException(e);
    }

    return this.yyval;
}



private void yyerror(final String s) throws ExprParsingException
{
    throw new ExprParsingException(s+" at \'"+getCharForMessage()+"\' while parsing expression: "+this.lexer);
}

private int yylex() throws ExprLexingException
{
	final ExprLexer.Token token = this.lexer.getNextToken();

	this.yylval = token.getTokenValue();
	if (token.getTokenType() == EOF)
	{
		return 0;
	}
	return token.getTokenType();
}

private String getCharForMessage()
{
	if (yychar < 0x100)
	{
		return Character.toString((char)yychar);
	}

	if (yychar == EOF)
	{
		return "(EOF)";
	}

	if (yychar == DOT)
	{
		return ".";
	}

	if (yychar == COMMA)
	{
		return ",";
	}

	if (yychar == NUM)
	{
		return "(NUMBER)";
	}

	if (yychar == EOF)
	{
		return "(IDENT)";
	}

	return "(unknown)";
}
