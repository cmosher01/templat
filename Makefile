.PHONY: all jar site doc

MAVEN ?= mvn

all: jar site
	$(MAVEN) --batch-mode install

site: doc

doc:
	$(MAKE) -C $@ all
	$(MAKE) -C $@ site
