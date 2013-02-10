define(["jquery", "lib/logger", "lib/sanityChecker", "lib/domNavigationUtils", "lib/externalLinkDetector", "lib/gondola"], function($, logger, sanityChecker, domNavigationUtils, externalLinkDetector, gondola) {
    "use strict";

    var slideContainerId = '#homeGondola',
        slideSelector = '.bjqs-slide',
        highLightsContainerId = 'homeHilits',
        eventBoxClassName = 'evBox',
        hasErrors = false,
        eventBoxes,
        clickedElement,
        eventBoxRoot,
        link;

    return {
        trigger : function() {
            hasErrors = sanityChecker.checkIfNotExists('#' + slideContainerId, 'Slide container')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing homepage ...');
                eventBoxes = $('#' + highLightsContainerId + ' article.' + eventBoxClassName);
                eventBoxes.each(function(index) {
                    $(this).click(function(event) {
                        clickedElement = $(this);
                        if (!externalLinkDetector.isContainedInALink(clickedElement)) {
                            eventBoxRoot = domNavigationUtils.firstAncestorWithClass(clickedElement, eventBoxClassName);
                            link = domNavigationUtils.firstDescendantWithTag('a', eventBoxRoot);
                            if(link !== undefined) {
                                externalLinkDetector.interceptRedirect(link);
                            }
                        }
                    });
                });
                gondola.trigger(slideContainerId, slideSelector);
                logger.debug('... done!');
            }
        }
    };
});
