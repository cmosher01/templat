/**
 * <p>
 * Lexes and parses an expression.
 * </p>
 * <p>
 * This package is responsible for lexing and parsing an expression used in
 * some template tags. An expression is a complicated structure to parse, so this
 * task is handled by JavaCC.
 * </p>
 * <p>
 * The heart of this package is in src/main/javacc/ExprParser.jj file, which is
 * a JavaCC source file that parses an expression.
 * <p>
 * An expression is primarily just a variable, literal number, or class name, possibly
 * with method calls and/or array subscripts. The ExprParser.jj file defines the exact
 * grammar.
 * </p>
 */
package net.sourceforge.templat.expr;
