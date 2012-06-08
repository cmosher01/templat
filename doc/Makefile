.SUFFIXES: .asciidoc .html

.asciidoc.html :
	asciidoc -b html5 $<

HTMLS=index.html

.PHONY: all doc clean site

all : doc

doc : $(HTMLS)

clean :
	rm -f $(HTMLS)
	rm -Rf site

site :
	mkdir -p site
	cp $(HTMLS) site
