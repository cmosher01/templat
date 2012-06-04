.PHONY: site doc

site: doc

doc:
	$(MAKE) -C $@ all
	$(MAKE) -C $@ site
