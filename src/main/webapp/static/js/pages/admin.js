define(["jquery", "lib/sanityChecker", "lib/logger", "lib/urlParser", "jqueryui"], function($, sanityChecker, logger, urlParser) {
    "use strict";

    var hasErrors = false,
        tabulationContainerId = '#tabulations',
        removeButtonClass = '.send',
        getActiveTabRank = function(queryString) {
            var tabulations = ['gondola','slot','event','channel'],
                parameters = urlParser.parse(queryString),
                requestedTabRank = tabulations.indexOf(parameters.active);
            return requestedTabRank === -1 ? 0 : requestedTabRank;
        };

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(tabulationContainerId, 'Tabulation container')|| hasErrors;
            if(hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing admin homepage...');
                //TODO: use the ajax capabilities of the plugin
                $(tabulationContainerId).tabs({active: getActiveTabRank(window.location.search)});
                $(removeButtonClass).click(function() {
                    return window.confirm('Do you rilly want to rimove this ?');
                });
                logger.debug('... done!');
            }
        }
    };
});
