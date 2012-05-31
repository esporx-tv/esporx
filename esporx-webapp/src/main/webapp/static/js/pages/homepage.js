"use strict";
var Homepage = Class.create({
	initialize: function() {
		var controllerName = 'Homepage';
		var homepageLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var highLightsContainerId = "homeHilits";
		var eventBoxClassName = "evBox";
		var gondolaContainer = "gSlide";
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + gondolaContainer, 'Gondola container area')|| hasErrors;
		if (hasErrors) {
			homepageLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			try {
				homepageLogger.debug('Initializing homepage');
				
				$$('#'+highLightsContainerId+' article.'+eventBoxClassName).each(function(element) {
					element.observe('click', function(event) {
						var el = Event.element(event);
						if (!this.isContainedInALink(el)) { 
							var link = this.matchingFirstDescendantWithTag(this.matchingFirstParentWithClass(el, eventBoxClassName), 'a');
							if(link != undefined) {
								this.redirect(link);
							}
						}
					}.bind(this));
				}.bind(this));
				
				this.initCarousel();
			}
			catch(e) {
				console.log(e);
			}
		}
	},

	initCarousel: function() {
		this.duplicateFirstSlide();
		
		var slides = $$('article.gFrame');
		var slideCount = slides.size();
		var slideContainerWidth = 100 * slideCount;
		$('gSlide').setStyle({width: slideContainerWidth + '%'});
		
		var slideWidth = 100 / slideCount;
		slides.each(function(slide) {
			slide.setStyle({width: slideWidth + '%'});
		});
		
		var lastSlideWidth = 100 - (slideCount-1) * slideWidth;
		slides.last().setStyle({width: lastSlideWidth  + '%'});
		
		var carousel = new Carousel('gSlideView', $$('article.gFrame'), $$('.carousel-control'), {
			auto: true,
			circular: true,
			wheel: false,
			frequency: 3
		});

		$$('.fInfos').each(function(textBox) {
			textBox.observe('mouseover', function(event) {
				var element = Event.element(event);
				carousel.pause();
			});
			textBox.observe('mouseout', function(event) {
				var element = Event.element(event);
				carousel.resume(event);
			});
		});
	},
	
	duplicateFirstSlide: function() {
		var copy = $$('article.gFrame').first().clone(true);
		$$('article.gFrame').last().insert({'after': copy});	
	},
	
	/*
	 * true if element is a link (<a>) or one of his ancestor is a link
	 */
	isContainedInALink: function(element) {
		return element.tagName.toUpperCase() == 'A' || this.matchingFirstParentWithTag(element, 'a') != undefined;
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
	matchingFirstParentWithTag: function(currentElement, tag) {
		return currentElement.ancestors().find(function(ancestor) {
			return ancestor.tagName.toUpperCase() == tag.toUpperCase();
		});
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
		var destination = linkElement.readAttribute('href');
		if(!destination.blank()) {
			var linkDetector = new ExternalLinkDetector();
			if(linkDetector.isExternalLink(destination)) {
				window.open(destination);
			}
			else {
				window.location = destination;
			}
		}
	}
});


document.observe("dom:loaded",function() {
	new Homepage();
});