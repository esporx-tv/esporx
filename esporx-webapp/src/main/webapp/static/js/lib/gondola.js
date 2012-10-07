define(["ext/prototype", "ext/effects", "ext/carousel"], function() {
    "use strict";
    
	var gondolaSlideSelector = 'article.gFrame';
	var gondolaHoverableTextAreas = '.fInfos';
	var gondolaControls = '.carousel-control';
	var gondolaContainer = 'gSlide';
	var gondolaRootId = 'gSlideView';
    
	var slides = new Hash();
	var slideControls = new Hash();
	var observableTextAreas = new Hash();

	var initialize = function() {
		slides = $$(gondolaSlideSelector);
		if (slides.size() > 0) {
			slideControls = $$(gondolaControls);
			observableTextAreas = $$(gondolaHoverableTextAreas);
		}
	}

    var duplicateFirstSlide = function() {
        var copy = slides.first().clone(true);
        slides.last().insert({
            'after' : copy
        });
        slides = $$(gondolaSlideSelector);
    }

    var resizeSlides = function() {
        var slideCount = slides.size();
        var slideContainerWidth = 100 * slideCount;
        $(gondolaContainer).setStyle({
            width : slideContainerWidth + '%'
        });

        var slideWidth = 100 / slideCount;
        slides.each(function(slide) {
            slide.setStyle({
                width : slideWidth + '%'
            });
        });

        // make sure total width is 100% ;)
        var lastSlideWidth = 100 - (slideCount - 1) * slideWidth;
        slides.last().setStyle({
            width : lastSlideWidth + '%'
        });
    }

    var startAnimation = function() {
        var carousel = new Carousel(gondolaRootId, slides,
            slideControls, {
                auto : true,
                circular : true,
                wheel : false,
                frequency : 3
            });

        observeControls(carousel);
    }

    var observeControls = function(carousel) {
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
    }

    return {
        trigger : function() {
            initialize();
            duplicateFirstSlide();
            resizeSlides();
            startAnimation();
        }
    }


});
