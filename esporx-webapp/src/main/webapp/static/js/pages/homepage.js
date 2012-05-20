"use strict";
var Homepage = Class.create({
	initialize: function() {
		var controllerName = 'Homepage';
		var homepageLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var highLightsContainerId = "homeHilits";
		var eventBoxClassName = "evBox";
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
		if (hasErrors) {
			homepageLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			homepageLogger.debug('Initializing homepage');
			$$('#'+highLightsContainerId+' article.'+eventBoxClassName).each(function(element) {
				element.observe('click', function(event) {
					var el = Event.element(event);
					var link = this.matchingFirstDescendantWithTag(this.matchingFirstParentWithClass(el, eventBoxClassName), 'a');
					this.redirect(link);
				}.bind(this));
			}.bind(this));
		}
	},

	/*
	 * returns the first provided class-matching parent (provided element included) or undefined if none found
	 */
	matchingFirstParentWithClass: function(currentElement, className) {
		var result;
		if(currentElement.hasClassName(className)) {
			result = currentElement;
		}
		else {
			result = currentElement.ancestors().find(function(capturedElement) {
				return capturedElement.hasClassName(className);
			});
		}
		return result;
	},

	/*
	 * returns the first provided tag name matching parent or undefined if none found
	 */
	matchingFirstDescendantWithTag: function(currentElement, tag) {
		return currentElement.descendants().find(function(descendant) {
			return descendant.tagName.toUpperCase() == tag.toUpperCase();
		});
	},
	
	redirect: function(linkElement) {
		var destination = linkElement.href;
		if(!destination.blank()) {
			window.location = destination;
		}
	}
});


document.observe("dom:loaded",function() {
	new Homepage();
});