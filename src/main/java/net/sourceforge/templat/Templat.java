/*
 * Created on 2005-09-04
 */
package net.sourceforge.templat;

import net.sourceforge.templat.exception.*;
import net.sourceforge.templat.lexer.*;
import net.sourceforge.templat.parser.TemplateParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <code>TemplAT</code> is a template processor, used in conjunction with Java.
 * Templates contain processing instructions within at-signs, thus the name
 * TemplAT. Processing instructions include if-then-else, loops, includes (with
 * parameters), and (Java) method calls.
 *
 * @author Chris Mosher
 */
public class Templat {
    /**
     * Initializes this <code>Templat</code> to read from the given
     * <code>URL</code>. The contents of the <code>URL</code> are assumed to be
     * in UTF-8 format.
     *
     * @param url the <code>URL</code> to read the template from (UTF-8)
     */
    public Templat(final URL url) {
        this.url = url;
    }

    /**
     * Gets the <code>URL</code> of this <code>Templat</code> (as passed into
     * the constructor).
     *
     * @return the <code>URL</code>.
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * Renders this template. The arguments, if any, are passed to the template
     * itself, and bound to the parameters specified in the template definition.
     * The result of parsing the template is appended to the given
     * <code>Appendable</code>.
     *
     * @param appendTo the <code>Appendable</code> to append the result to
     * @param rArg     optional argument(s) to pass to the template
     * @throws TemplateLexingException
     * @throws TemplateParsingException
     * @throws IOException
     */
    public void render(final Appendable appendTo, final Object... rArg) throws TemplateLexingException, TemplateParsingException, IOException {
        final TemplateLexer lexer = new TemplateLexer(loadTemplate());
        final List<TemplateToken> rToken = new ArrayList<>();
        lexer.lex(rToken);

        final TemplateParser parser = new TemplateParser(rToken, Arrays.asList(rArg), this.url);
        parser.parse(appendTo);
    }

    private String loadTemplate() throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(this.url.openConnection().getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n", "", "\n"));
        }
    }

    private final URL url;
}
