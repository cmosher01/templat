// This file needs to be compiled with BYACC/J using the following command:
// yacc -J -Jclass=IncludeParser -Jsemantic=Object -Jpackage=nu.mine.mosher.template.expr -Jthrows="ExprLexingException, ExprParsingException, TemplateParsingException" -Jnorun -Jnoconstruct -Jnodebug include.yacc

%{
import nu.mine.mosher.template.parser.context.ContextStack;
import nu.mine.mosher.template.exception.TemplateParsingException;
import nu.mine.mosher.template.expr.exception.ExprLexingException;
import nu.mine.mosher.template.expr.exception.ExprParsingException;



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

include
	: tmplname args { $$ = act.createInclude($1,$2); }
	;

tmplname
    : tmplname '/' identifier { $$ = $1+"/"+$3; }
    | identifier
    ;

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
public IncludeParser(final String input, final ContextStack stackContext)
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
    throw new ExprParsingException(s+" while parsing string: "+this.lexer);
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
