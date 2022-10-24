# TemplAT

Copyright © 2005, 2006, 2011, 2012, 2015, 2022, Christopher Alan Mosher, Shelton, Connecticut, USA, <cmosher01@gmail.com>.

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=CVSSQ2BWDCKQ2)
[![License](https://img.shields.io/github/license/cmosher01/templat.svg)](https://www.gnu.org/licenses/gpl.html)

_TemplAT_ is a text-file template engine, used from Java.

TemplAT is a general-purpose text-file **template engine**, designed to be
used as an API from **Java** programs. It is small and simple,
yet surprisingly powerful. It does not have any external library dependencies.



## Project Goals

* _Simple_. The TemplAT language is very simple to learn. It contains only the most basic
programming constructs: `if` `then` `else`, `loop`, and `include` (with parameters).
It also allows calling Java _methods_ and accessing Java _arrays_. That's all
there is to it. And yet, these few simple tools provide a powerful templating
language.

* _Small_. The TemplAT class library consists of a single, small (53K), jar file.
That's it. It's designed to contain only what is necessary to provide its simple
language, and _nothing more_. It is self-contained, and does not rely on any other
external libraries.

* _Useful_. TemplAT can be used for generating *HTML* pages by a Java servlet for a web
server. It could just as easily be used for generating Java, C++, or SQL
*source code*. It is useful for any text file where you need some kind of
*programming ability* (loops, if-then-else, includes, method calls).

* _Reliable_. The TemplAT source code's correctness is backed by a comprehensive suite of
**unit tests**. This ensures that each new release can add features and improvements
while preventing any regression. It allows for the developers to refactor code
and improve its internal state without introducing new bugs. The internal structure
of the code is clean, simple, and straightforward.



## Usage

For `gradle` builds:

```groovy
repositories {
    mavenCentral()
    maven {
        url = uri('https://public:\u0067hp_fya6Kseu3XOBMg2icbg7f1LP6ZFYjj35v4Zj@maven.pkg.github.com/cmosher01/*')
    }
}

dependencies {
    implementation group: 'net.sourceforge.templat', name: 'templat', version: 'latest.release'
}
```



## Simple Example

Create a template (a text file ) `displayBooks.tat`

```
@template displayBooks(books)@
We have the following books:
@loop b : books.size()@
    @books.get(b).getTitle()@ by @books.get(b).getAuthor()@.
@end loop@
```

Tags are surrounded by at-signs (`@`). These regions will be parsed by the _`Templat`_
class. The `template` tag defines this file as a template, and specifies its parameters.
The `loop` tag starts a loop block, and identifies the loop counter variable and
the number of times the loop block will be expanded. The expressions in the next
line will be parsed by the _`Templat`_ class, and the returned values expanded into
the resulting text. The `end loop` tag ends the loop block.

Render the template within a Java program `PrintBooks.java`

```java
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.templat.Templat;

public class PrintBooks
{
    // A simplified structure that represents one book
    public static class Book
    {
        private final String author;
        private final String title;

        public Book(String a, String t) { author = a; title = t; }

        public String getAuthor() { return this.author;}
        public String getTitle() { return this.title;}
    }

    public static void main(String... args) throws Exception
    {
        // This is our inventory of books:
        List<Book> books = new ArrayList<>();
        books.add(new Book("Rudyard Kipling","The Jungle Book"));
        books.add(new Book("Mary Shelley","Frankenstein"));
        books.add(new Book("Oscar Wilde","The Picture of Dorian Gray"));

        // Get our displayBooks template:
        Templat tat = new Templat(new File("displayBooks.tat").toURI().toURL());

        /*
         * Render the template, passing our array of books for
         * the argument, and put the result into the StringBuilder.
         */
        Appendable result = new StringBuilder();
        tat.render(result, books);

        // Print out the result
        System.out.println(result);
    }
}
```

Compiling and running the program produces the following output:

```
$ java -cp .;templat.jar PrintBooks
We have the following books:

    The Jungle Book by Rudyard Kipling.

    Frankenstein by Mary Shelley.

    The Picture of Dorian Gray by Oscar Wilde.
```



## Overview

#### Template File Syntax

```
   @ template template_name( parameter1, parameter2, ... ) @

   @ if ( boolean_expression ) @
         if_body
[  @ else @
         else_body  ]
   @ end if @

   @ loop variable : count_expression @
         loop_body
   @ end loop @

   @ include template_path( argument1, argument2, ... ) @

   @ expression @
```

#### Rendering

```java
import net.sourceforge.templat.Templat;

...

Templat tat = new Templat(url);

Appendable result = new StringBuilder();
tat.render(result, argument1, argument2, ... );
```



## Template File

```
   @ template template_name( parameter1, parameter2, ... ) @
         template_body
```
The `template` tag defines the file as a template to be parsed by the _`Templat`_ class.
It must be at the start of every template file. `template_name` is the name of this
template. This name must be the same as the name of the file containing this template,
without the `.tat` filetype. Following the name, in parentheses, is an optional
comma-delimited list of `parameter` s for this template. Following the `template` tag
is the `template_body` (the rest of the file), which may contain other tags.

```
   @ if ( boolean_expression ) @
         if_body
[  @ else @
         else_body  ]
   @ end if @
   ```
The `if` and `end if` tags, and optional `else` tag, define a conditional expansion.
The `boolean_expression` is evaluated; if the result is true, the `if_body` is
(parsed and) expanded to the output. Otherwise, the `else_body`, if it exists, is
(parsed and) expanded to the output. Note that either body (or both) may contain
template tags and/or plain text areas.

```
   @ loop variable : count_expression @
         loop_body
   @ end loop @
```
The `loop` and `end loop` tags define a repeated expansion. The `count_expression`
is evaluated as a Java expression that returns an _integer_, and the `loop_body` is
(parsed and) expanded _that many times_ to the output. If the count is less than or
equal to zero, then the `loop_body` will not be expanded. Within the `loop_body`,
the `variable` may be referenced within any expression in any tag. The `variable` will
be a `java.lang.Integer`. It will hold the value _zero_ on the first iteration of the
loop, _one_ on the next iteration, etc., up to _count minus 1_ on the final iteration.

```
   @ include template_path( argument1, argument2, ... ) @
```
The `include` tag parses and expands another template file. `template_path` is the
(optional path and) name of the template to be included. The path is interpreted
_relative to_ the including template. The file name of the included template will
be the specified name followed by `.tat` file-type. Following the `template_path`,
within parentheses, you must specify the arguments required by the included template.
These arguments will be bound to the parameters defined by the included template
when it is parsed.

```
   @ expression @
```
Any tag that does not start with one of TemplAT's keywords will be treated as a Java
expression. An expression can be a variable name, a class name, or an integer literal.
Classes or variables may further have method calls or subscripts (for arrays
or `java.util.List` objects).

```
[  other text  ]
```
Areas of the template that are not within any tag will be passed through verbatim
to the output. The __one exception__ is that text cannot contain an _at-sign_ (`@`) by
itself (because an at-sign defines the start of a tag). Use _two at-signs_ in a row
(`@@`) in text to indicate a single at-sign in the rendered output. For example,
`john@@example.com` in text within a template would be rendered as `john@example.com`
in the output. However, `john@example.com` in the text would result in a syntax error
at render time.



## TemplAT API

Parsing templates is accomplished by the developer writing a Java program and
using the _TemplAT API_. The TemplAT API is very simple and straightforward,
consisting of basically one class and one method. The class to use is:

`net.sourceforge.templat.Templat`

Create an instance of this class, and pass the template's `URL` to the constructor:

```java
new Templat(urlOfTatFile);
```

To actually render the template, call the render method, which appends the
result to the given `Appendable`:

```java
void render(Appendable result, Object... arguments)
```



## Template File Grammar

```
 template : tmpldef body
  tmpldef : '@' 'template' '(' [param [',' param ...]] ')' '@'
     body : [text] [statement] ...
statement : if | include | loop | expr
       if : '@' 'if' '(' boolean-expr ')' '@' body '@' 'end if' '@'
  include : '@' 'include' template-path '(' [expr [',' expr ...]] ')' '@'
     loop : '@' 'loop' var ':' count-expr '@' body '@' 'end loop' '@'
     expr : '!' expr
            '(' expr ')'
            INTEGER
            name [selector...]
     name : [name '.'] IDENTIFIER
 selector : '.' IDENTIFIER '(' [expr...] ')'
            '[' expr ']'
```
