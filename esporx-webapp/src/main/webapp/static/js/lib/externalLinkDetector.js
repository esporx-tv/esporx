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
	    return !(this._getDomain(window.location.href) === this._getDomain(url));
	},
	_getDomain: function(url) {
		return url.replace('http://', '').replace('https://', '').split('/')[0];
	}
});