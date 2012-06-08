/*
 * Created on Sep 4, 2005
 */
package net.sourceforge.templat.lexer;



import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.expr.Expression;
import net.sourceforge.templat.parser.TemplateParser;
import net.sourceforge.templat.parser.context.TemplateParserContext;



/**
 * "loop" token
 * @author Chris Mosher
 */
class LoopToken implements TemplateToken
{
    private final String tag;

    /**
     * @param tag text after loop token: "variable : count"
     */
    public LoopToken(final String tag)
    {
        this.tag = tag;
    }

    @Override
    public String toString()
    {
        return "LOOP: " + this.tag;
    }

    @Override
    public void parse(final TemplateParser parser, final Appendable appendTo) throws TemplateParsingException
    {
        /* nothing to append here */
        appendTo.getClass();

        /* @formatter:off */
        /*
         * Parse the loop statement, which is in this format:
         *     <index-variable> : <loop count>
         */
         /* @formatter:on */
        final int posColon = this.tag.indexOf(':');

        if (posColon < 0)
        {
            throw new TemplateParsingException("Missing colon in loop statement.");
        }

        final TemplateParserContext ctxNew = new TemplateParserContext();

        {
            final String varIndex = this.tag.substring(0, posColon).trim();
            ctxNew.addVariable(TemplateParser.VAR_LOOP_INDEX, varIndex);
            ctxNew.addVariable(varIndex, Integer.valueOf(0));
        }

        {
            final String exprTimes = this.tag.substring(posColon + 1).trim();

            final Number numTimes;
            if (parser.getContext().isEverEqual(TemplateParser.VAR_IF, Boolean.FALSE))
            {
                numTimes = Integer.valueOf(0);
            }
            else
            {
                numTimes = (Number) Expression.eval(exprTimes, parser.getContext());
            }
            final int times = numTimes.intValue();

            ctxNew.addVariable(TemplateParser.VAR_LOOP_TIMES, Integer.valueOf(times));
            if (times <= 0)
            {
                ctxNew.addVariable(TemplateParser.VAR_IF, Boolean.FALSE);
            }
        }

        parser.getContext().push(ctxNew);

        parser.saveParsePosition();
    }
}
