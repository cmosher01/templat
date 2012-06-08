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
    public void parse(final TemplateParser parser, @SuppressWarnings("unused") final Appendable appendTo)
        throws TemplateParsingException
    {
        /* make sure we're in an if-block */
        parser.getContext().current().getValue(TemplateParser.VAR_IF);
        parser.getContext().pop();
    }
}
