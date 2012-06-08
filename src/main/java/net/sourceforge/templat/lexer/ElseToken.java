/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;



import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;



/**
 * "else" token
 * @author Chris Mosher
 */
class ElseToken implements TemplateToken
{
    @Override
    public String toString()
    {
        return "ELSE";
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

    private static void tryParse(final TemplateParser parser) throws TemplateParsingException, ClassCastException
    {
        boolean conditionIf = ((Boolean) parser.getContext().current().getValue(TemplateParser.VAR_IF)).booleanValue();
        parser.getContext().current().addVariable(TemplateParser.VAR_IF, Boolean.valueOf(!conditionIf));
    }
}
