/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;



import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.parser.TemplateParser;



/**
 * "end loop" token
 * @author Chris Mosher
 */
class EndLoopToken implements TemplateToken
{
    @Override
    public String toString()
    {
        return "END LOOP";
    }

    @Override
    public void parse(final TemplateParser parser, @SuppressWarnings("unused") final Appendable appendTo)
        throws TemplateParsingException
    {
        final Number numTimes = (Number) parser.getContext().current().getValue(TemplateParser.VAR_LOOP_TIMES);
        final int times = numTimes.intValue();

        final String varIndex = (String) parser.getContext().current().getValue(TemplateParser.VAR_LOOP_INDEX);
        int i = ((Integer) parser.getContext().getValue(varIndex)).intValue();

        ++i;

        parser.getContext().current().addVariable(varIndex, Integer.valueOf(i));

        if (i < times && !parser.getContext().isEverEqual(TemplateParser.VAR_IF, Boolean.FALSE))
        {
            parser.restoreParsePosition();
            parser.saveParsePosition();
        }
        else
        {
            parser.forgetParsePosition();
            parser.getContext().pop();
        }
    }
}
