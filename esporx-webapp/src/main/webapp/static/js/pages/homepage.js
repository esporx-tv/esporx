/*global $$: true
  Event: true*/
define(["lib/logger", "lib/sanityChecker", "lib/domNavigationUtils", "lib/externalLinkDetector", "lib/gondola"], function(logger, sanityChecker, domNavigationUtils, externalLinkDetector, gondola) {
    "use strict";

    var highLightsContainerId = 'homeHilits',
        eventBoxClassName = 'evBox',
        hasErrors = false,
        eventBoxes,
        clickedElement,
        eventBoxRoot,
        link;

    return {
        trigger : function() {
            hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing homepage ...');
                eventBoxes = $$('#' + highLightsContainerId + ' article.' + eventBoxClassName);
                eventBoxes.each(function(element) {
                    element.observe('click', function(event) {
                        clickedElement = Event.element(event);
                        if (!externalLinkDetector.isContainedInALink(clickedElement)) {
                            eventBoxRoot = domNavigationUtils.firstAncestorWithClass(clickedElement, eventBoxClassName);
                            link = domNavigationUtils.firstDescendantWithTag('a', eventBoxRoot);
                            if(link !== undefined) {
                                externalLinkDetector.interceptRedirect(link);
                            }
                        }
                    }.bind(this));
                }.bind(this));
                gondola.trigger();
                logger.debug('... done!');
            }
        }
    };
});
