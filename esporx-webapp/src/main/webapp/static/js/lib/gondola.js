"use strict";
var Gondola = Class.create({
	gondolaSlideSelector : 'article.gFrame',
	gondolaHoverableTextAreas : '.fInfos',
	gondolaControls : '.carousel-control',
	gondolaContainer : 'gSlide',
	gondolaRootId : 'gSlideView',
	slides : new Hash(),
	slideControls : new Hash(),
	observableTextAreas : new Hash(),

	initialize : function() {
		this.slides = $$(this.gondolaSlideSelector);
		if (this.slides.size() > 0) {
			this.slideControls = $$(this.gondolaControls);
			this.observableTextAreas = $$(this.gondolaHoverableTextAreas);
		}
	},

	trigger : function() {
		this.duplicateFirstSlide();
		this.resizeSlides();
		this.startAnimation();
	},

	duplicateFirstSlide : function() {
		var copy = this.slides.first().clone(true);
		this.slides.last().insert({
			'after' : copy
		});
		this.slides = $$(this.gondolaSlideSelector);
	},

	resizeSlides : function() {
		var slideCount = this.slides.size();
		var slideContainerWidth = 100 * slideCount;
		$(this.gondolaContainer).setStyle({
			width : slideContainerWidth + '%'
		});

		var slideWidth = 100 / slideCount;
		this.slides.each(function(slide) {
			slide.setStyle({
				width : slideWidth + '%'
			});
		});

		// make sure total width is 100% ;)
		var lastSlideWidth = 100 - (slideCount - 1) * slideWidth;
		this.slides.last().setStyle({
			width : lastSlideWidth + '%'
		});
	},

	startAnimation : function() {
		var carousel = new Carousel(this.gondolaRootId, this.slides,
				this.slideControls, {
					auto : true,
					circular : true,
					wheel : false,
					frequency : 3
				});

		this.observeControls(carousel);
	},

	observeControls : function(carousel) {
		this.observableTextAreas.each(function(textBox) {
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
});