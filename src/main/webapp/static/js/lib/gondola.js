define(["jquery", "lib/externalLinkDetector", "text!css/slider.css", "ext/slider"], function($, linkDetector, sliderCss) {
    "use strict";

    var restyleSlides = function(slideSelector, containerId) {
        $(slideSelector).css({cursor:'pointer'}).click(function() {
            linkDetector.interceptRedirect($('.slideLink:first-child', this));
        });
        $(slideSelector + ' img').width($(containerId).width());
        $(window).resize(function(event) {
            $(slideSelector + ' img').width($(containerId).width());
        });
    };

    return {
        trigger : function(containerId, slideSelector) {
            $('head').first().append('<style>' + sliderCss + '</style>');
            $(containerId).bjqs({
                animduration: 1000,
                animtype : 'slide',
                responsive : false,
                showcontrols: false,
                showmarkers: false,
                useCaptions: false,
                width: $(containerId).width(),
                height:'100%'
            });
            restyleSlides(slideSelector, containerId);
        }
    };


});
