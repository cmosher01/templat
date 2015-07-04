.PHONY: all install site doc

MAVEN ?= mvn

all: install site

install:
	$(MAVEN) --batch-mode $@

site: doc

doc:
	$(MAKE) -C $@ all
	$(MAKE) -C $@ site
