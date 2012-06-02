"use strict";
var Homepage = Class.create({
	initialize: function() {
		var controllerName = 'Homepage';
		var homepageLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var highLightsContainerId = 'homeHilits';
		var eventBoxClassName = 'evBox';
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
		if (hasErrors) {
			homepageLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			homepageLogger.debug('Initializing homepage');
			
			var linkDetector = new ExternalLinkDetector();
			var domUtils = new DomNavigationUtils();
			
			var eventBoxes = $$('#' + highLightsContainerId + ' article.' + eventBoxClassName);
			eventBoxes.each(function(element) {
				element.observe('click', function(event) {
					var el = Event.element(event);
					console.log(el, linkDetector.isContainedInALink(el));
					if (!linkDetector.isContainedInALink(el)) { 
						var eventBoxRoot = domUtils.firstAncestorWithClass(el, eventBoxClassName);
						var link = domUtils.firstDescendantWithTag('a', eventBoxRoot);
						if(link != undefined) {
							linkDetector.interceptRedirect(link);
						}
					}
				}.bind(this));
			}.bind(this));
			
			new Gondola().trigger();
		}
	},
});


document.observe('dom:loaded',function() {
	new Homepage();
});