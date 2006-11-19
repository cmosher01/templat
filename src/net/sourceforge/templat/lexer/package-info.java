/**
 * <p>
 * Translates a template into a list of tokens.
 * </p>
 * <p>
 * Lexing is the process of converting an input stream of characters
 * into a list of meaningful tokens.
 * Lexing the template is the first step in the rendering process.
 * </p>
 * <p>
 * The {@link net.sourceforge.templat.lexer.TemplateLexer} class performs the lexing.
 * It reads the template and generates instances of the appropriate
 * {@link net.sourceforge.templat.lexer.TemplateToken}s to represent the entire template.
 * The list of tokens would then typically be passed to the {@link net.sourceforge.templat.parser.TemplateParser}.
 * </p>
 * <p>
 * {@link net.sourceforge.templat.lexer.TemplateToken}
 * is an interface; the actual token classes implement this interface. The token classes represent
 * either a section of the template copied verbatim ({@link net.sourceforge.templat.lexer.StringToken}),
 * or one of the tags: if, else, end-if, loop, end-loop, template (definition), include, or an expression
 * ({@link net.sourceforge.templat.lexer.ValueToken}).
 * </p>
 * <p>
 * Since parsing expressions is complicated, this processing is handled in a separate package,
 * {@link net.sourceforge.templat.expr}. Expressions are not only found in the expression tag,
 * but also in the if tag (the condition), the include tag (the arguments), and the loop tag
 * (the loop count).
 * </p>
 */
package net.sourceforge.templat.lexer;
