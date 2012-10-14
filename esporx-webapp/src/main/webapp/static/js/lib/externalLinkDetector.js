/*global $$: true*/
define(["lib/domNavigationUtils","ext/prototype"], function(domNavigationUtils) {
    "use strict";

    var getDomain,
        isExternalLink,
        isContainedInALink;

    getDomain = function(url) {
        return url.replace('http://', '').replace('https://', '').split('/')[0];
    };
	isExternalLink = function(url) {
	    return (getDomain(window.location.href) !== getDomain(url));
	};
	isContainedInALink = function(element) {
		return element.tagName.toUpperCase() === 'A' || domNavigationUtils.firstAncestorWithTag('a', element) !== undefined;
	};

    return {
        scan : function() {
            $$('a').each(function(element) {
                if (isExternalLink(element.href)) {
                    element.writeAttribute('target', '_blank');
                }
            });
        },
        
        isContainedInALink : function(element) {
            return element.tagName.toUpperCase() === 'A' || domNavigationUtils.firstAncestorWithTag('a', element) !== undefined;
        },
        
        interceptRedirect : function(linkElement) {
            var destination = linkElement.readAttribute('href');
            if (!destination.blank() && destination.indexOf('#') !== 0) {
                if(isExternalLink(destination)) {
                    window.open(destination);
                }
                else {
                    window.location = destination;
                }
            }
        }
    };
});