"use strict";
var ExternalLinkDetector = Class.create({
	scan: function() {
		$$('a').each(function(element) {
			if (this.isExternalLink(element.href)) {
				element.writeAttribute('target', '_blank');
			}
		}.bind(this));
	},
	
	isExternalLink: function(url) {
	    return !(this.getDomain(window.location.href) === this.getDomain(url));
	},
	
	getDomain: function(url) {
		return url.replace('http://', '').replace('https://', '').split('/')[0];
	},

	isContainedInALink: function(element) {
		return element.tagName.toUpperCase() == 'A' || new DomNavigationUtils().firstAncestorWithTag('a', element) != undefined;
	},
	
	interceptRedirect: function(linkElement) {
		var destination = linkElement.readAttribute('href');
		if (!destination.blank() && destination.indexOf('#') != 0) {
			if(this.isExternalLink(destination)) {
				window.open(destination);
			}
			else {
				window.location = destination;
			}
		}
	}
});