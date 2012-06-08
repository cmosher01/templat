/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;



import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.Expression;
import net.sourceforge.templat.parser.TemplateParser;
import net.sourceforge.templat.parser.context.TemplateParserContext;



/**
 * "if" token
 * @author Chris Mosher
 */
class IfToken implements TemplateToken
{
    private final String tag;

    /**
     * @param tag text inside the parentheses: "if (tag)"
     */
    public IfToken(final String tag)
    {
        this.tag = tag.trim();
    }

    @Override
    public String toString()
    {
        return "IF: " + this.tag;
    }

    @Override
    public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
    {
        /* nothing to append here */
        appendTo.getClass();
        try
        {
            tryParse(parser);
        }
        catch (final TemplateParsingException e)
        {
            throw e;
        }
        catch (final Throwable e)
        {
            throw new TemplateParsingException(e);
        }
    }

    private void tryParse(final TemplateParser parser) throws TemplateParsingException, ClassCastException
    {
        final Boolean bCond;
        if (parser.getContext().isEverEqual(TemplateParser.VAR_IF, Boolean.FALSE))
        {
            bCond = Boolean.FALSE;
        }
        else
        {
            final Object objectCond = Expression.eval(this.tag, parser.getContext());

            /* this might throw a ClassCastException */
            bCond = (Boolean) objectCond;
        }

        final TemplateParserContext ctxNew = new TemplateParserContext();
        ctxNew.addVariable(TemplateParser.VAR_IF, bCond);

        parser.getContext().push(ctxNew);
    }
}
