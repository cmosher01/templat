.PHONY: site

site: doc

doc:
	$(MAKE) -C $@ all
	$(MAKE) -C $@ site
