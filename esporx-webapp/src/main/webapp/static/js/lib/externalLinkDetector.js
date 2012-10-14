define(["jquery", "lib/domNavigationUtils"], function($, domNavigationUtils) {
    "use strict";

    var getDomain,
        isExternalLink;

    getDomain = function(url) {
        return url.substring(0,1) === '/' ? getDomain(window.location.href) : url.replace('http://', '').replace('https://', '').split('/')[0];
    };
	isExternalLink = function(url) {
	    return (getDomain(window.location.href) !== getDomain(url));
	};

    return {
        scan : function() {
            $('a').each(function(index) {
                var element = $(this);
                if (isExternalLink(element.attr('href'))) {
                    element.attr('target', '_blank');
                }
            });
        },
        
        isContainedInALink : function(element) {
            return element.tagName.toUpperCase() === 'A' || domNavigationUtils.firstAncestorWithTag('a', element) !== undefined;
        },
        
        interceptRedirect : function(linkElement) {
            var destination = linkElement.attr('href');
            if ($.trim(destination) !== '' && destination.indexOf('#') !== 0) {
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