/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;



import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;



/**
 * "end if" token
 * @author Chris Mosher
 */
class EndIfToken implements TemplateToken
{
    @Override
    public String toString()
    {
        return "END IF";
    }

    @Override
    public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
    {
        /* nothing to append here */
        appendTo.getClass();
        /* make sure we're in an if-block */
        parser.getContext().current().getValue(TemplateParser.VAR_IF);
        parser.getContext().pop();
    }
}
