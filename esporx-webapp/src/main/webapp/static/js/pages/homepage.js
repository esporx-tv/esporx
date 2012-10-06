define(["lib/logger", "lib/sanityChecker", "lib/domNavigationUtils", "lib/externalLinkDetector", "lib/gondola"], function(logger, sanityChecker, domNavigationUtils, externalLinkDetector, gondola) {
    "use strict";

    var highLightsContainerId = 'homeHilits';
    var eventBoxClassName = 'evBox';

    return {
        trigger : function() {
            logger.setCaller('Homepage');
            var hasErrors = false;
            hasErrors = sanityChecker.checkIfNotExists('#' + highLightsContainerId, 'Highlights container area')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing homepage ...');
                var eventBoxes = $$('#' + highLightsContainerId + ' article.' + eventBoxClassName);
                eventBoxes.each(function(element) {
                    element.observe('click', function(event) {
                        var el = Event.element(event);
                        console.log(el, externalLinkDetector.isContainedInALink(el));
                        if (!externalLinkDetector.isContainedInALink(el)) {
                            var eventBoxRoot = domNavigationUtils.firstAncestorWithClass(el, eventBoxClassName);
                            var link = domNavigationUtils.firstDescendantWithTag('a', eventBoxRoot);
                            if(link != undefined) {
                                externalLinkDetector.interceptRedirect(link);
                            }
                        }
                    }.bind(this));
                }.bind(this));
                gondola.trigger();
                logger.debug('... done!');
            }
        }
    }
});
