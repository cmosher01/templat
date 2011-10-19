/**
 * <p>
 * Lexes and parses an expression.
 * </p>
 * <p>
 * This package is responsible for lexing and parsing an expression used in
 * some template tags. An expression is a complicated structure to parse, so this
 * task is offloaded from the {@link net.sourceforge.templat.lexer.TemplateLexer}
 * and the tokens into this package. The main entry point of this package is
 * the {@link net.sourceforge.templat.expr.Expression} class.
 * </p>
 * <p>
 * The heart of this package is the expr.yacc source file (available in the source
 * download, in src/net/sourceforge/templat/expr/expr.yacc). This is a standard
 * yacc file, except it contains Java source instead of C source. This file is processed
 * by <a href="byaccj.sourceforge.net">BYACC/J</a>, an open-source yacc implementation
 * that produces parsers written in Java instead of parsers written in C. The output of this process is
 * the {@link net.sourceforge.templat.expr.ExprParser} Java source file.
 * </p>
 * <p>
 * An expression is primarily just a variable, literal number, or class name, possibly
 * with method calls and/or array subscripts. The expr.yacc file defines the exact
 * grammar.
 * </p>
 * <p>
 * The parser (produced from the expr.yacc file) calls {@link net.sourceforge.templat.expr.ExprLexer} to get a
 * stream of tokens representing the entire input expression string being parsed. Then each applicable rule
 * in the expr.yacc grammar is applied; these rules call actions defined in the {@link net.sourceforge.templat.expr.ExprActions}
 * class. The two actions of method call and array subscript are handled by the classes
 * {@link net.sourceforge.templat.expr.MethodCall} and {@link net.sourceforge.templat.expr.ArraySubscript}.
 * </p>
 */
package net.sourceforge.templat.expr;
