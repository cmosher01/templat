/*
 * Created on 2005-09-05
 */
package net.sourceforge.templat.parser;


import net.sourceforge.templat.exception.TemplateParsingException;
import net.sourceforge.templat.lexer.TemplateToken;
import net.sourceforge.templat.parser.context.*;

import java.net.URL;
import java.util.*;


/**
 * Parser to render a (tokenized) template, given optional arguments. The
 * {@link TemplateParser#parse parse} method performs this action. The other
 * methods are for use by the tokens, who manipulate the context and parse
 * position of this parser.
 *
 * @author Chris Mosher
 */
public class TemplateParser {
    /**
     * Internal variable holding the arguments to the template
     */
    public static final String VAR_ARGS = "@args";
    /**
     * Internal variable used by IF statements
     */
    public static final String VAR_IF = "@if";
    /**
     * Internal variable used by LOOP statements
     */
    public static final String VAR_LOOP_INDEX = "@index";
    /**
     * Internal variable used by LOOP statements
     */
    public static final String VAR_LOOP_TIMES = "@loop";
    /**
     * Internal variable holding the URL of the template
     */
    public static final String VAR_URL = "@url";

    /**
     * Initializes this parser to use the given tokenized template (
     * <code>tToken</code>), passing it the given arguments.
     *
     * @param rToken tokenized template
     * @param rArg   arguments to pass into the template
     * @param url    the URL of the template (which is used to locate any included
     *               templates)
     */
    public TemplateParser(final List<TemplateToken> rToken, final List<Object> rArg, final URL url) {
        this.rToken.addAll(rToken);

        this.rPosParse.addFirst(0);

        final TemplateParserContext ctx = new TemplateParserContext();
        ctx.addVariable("true", Boolean.TRUE);
        ctx.addVariable("false", Boolean.FALSE);
        ctx.addVariable("null", null);
        ctx.addVariable(VAR_ARGS, rArg);
        ctx.addVariable(VAR_URL, url);

        this.stackContext.push(ctx);
    }

    /**
     * Discards the parse position (from the internal stack) that was saved by a
     * previous call to <code>saveParsePosition</code>. Used by some tokens.
     */
    public void forgetParsePosition() {
        this.rPosParse.remove(1);
    }

    /**
     * Gets this parser's stack of contexts.
     *
     * @return the context stack
     */
    public ContextStack getContext() {
        return this.stackContext;
    }

    /**
     * Parses this template and appends the rendered result to the given
     * <code>Appendable</code>. This method calls each token's
     * <code>parse</code> method to have that token perform its action. This
     * parser maintains the context (variables and their values) for use by the
     * tokens.
     *
     * @param appendTo <code>Appendable</code> to append the result to
     * @throws TemplateParsingException if the syntax of the template is invalid
     */
    public void parse(final Appendable appendTo) throws TemplateParsingException {
        while (this.rPosParse.getFirst() < this.rToken.size()) {
            final TemplateToken token = nextToken();
            token.parse(this, appendTo);
        }
    }

    /**
     * Restores the parse position (from the internal stack) that was saved by a
     * previous call to <code>saveParsePosition</code>. Used by some tokens.
     */
    public void restoreParsePosition() {
        this.rPosParse.removeFirst();
    }

    /**
     * Saves the current parse position (on an internal stack). Used by some
     * tokens.
     */
    public void saveParsePosition() {
        this.rPosParse.addFirst(this.rPosParse.getFirst());
    }
    private final LinkedList<Integer> rPosParse = new LinkedList<>();
    private final List<TemplateToken> rToken = new ArrayList<>();
    private final ContextStack stackContext = new ContextStack();

    private TemplateToken nextToken() {
        final TemplateToken token = this.rToken.get(this.rPosParse.getFirst());

        int pos = this.rPosParse.removeFirst();
        ++pos;
        this.rPosParse.addFirst(pos);

        return token;
    }
}
