/*global $$: true
$: true
Hash: true
Event: true*/
define(["ext/carousel"], function(carouselHelper) {
    "use strict";

	var gondolaSlideSelector = 'article.gFrame',
        gondolaHoverableTextAreas = '.fInfos',
        gondolaControls = '.carousel-control',
        gondolaContainer = 'gSlide',
        gondolaRootId = 'gSlideView',
        slides = new Hash(),
        copy,
        slideCount,
        slideContainerWidth,
        slideWidth,
        lastSlideWidth,
        carousel,
        slideControls = new Hash(),
        observableTextAreas = new Hash(),
        initialize,
        duplicateFirstSlide,
        resizeSlides,
        observeControls,
        startAnimation;

	initialize = function() {
		slides = $$(gondolaSlideSelector);
		if (slides.size() > 0) {
			slideControls = $$(gondolaControls);
			observableTextAreas = $$(gondolaHoverableTextAreas);
		}
	};

    duplicateFirstSlide = function() {
        copy = slides.first().clone(true);
        slides.last().insert({
            'after' : copy
        });
        slides = $$(gondolaSlideSelector);
    };

    resizeSlides = function() {
        slideCount = slides.size();
        slideContainerWidth = 100 * slideCount;
        $(gondolaContainer).setStyle({
            width : slideContainerWidth + '%'
        });

        slideWidth = 100 / slideCount;
        slides.each(function(slide) {
            slide.setStyle({
                width : slideWidth + '%'
            });
        });

        // make sure total width is 100% ;)
        lastSlideWidth = 100 - (slideCount - 1) * slideWidth;
        slides.last().setStyle({
            width : lastSlideWidth + '%'
        });
    };

    observeControls = function(carousel) {
        observableTextAreas.each(function(textBox) {
            textBox.observe('mouseover', function(event) {
                var element = Event.element(event);
                carousel.pause();
            });
            textBox.observe('mouseout', function(event) {
                var element = Event.element(event);
                carousel.resume(event);
            });
        });
    };

    startAnimation = function() {
        carousel = carouselHelper.initialize(gondolaRootId, slides,
            slideControls, {
                auto : true,
                circular : true,
                wheel : false,
                frequency : 3
            });

        observeControls(carousel);
    };

    return {
        trigger : function() {
            initialize();
            duplicateFirstSlide();
            resizeSlides();
            startAnimation();
        }
    };


});
