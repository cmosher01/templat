.PHONY: site

site:
	$(MAKE) -C $@ all
	$(MAKE) -C $@ site
