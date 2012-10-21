define(["jquery", "lib/sanityChecker", "lib/logger", "jqueryui"], function($, sanityChecker, logger) {
    "use strict";

    var hasErrors = false,
        tabulationContainerId = '#tabulations',
        removeButtonClass = '.send';

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(tabulationContainerId, 'Tabulation container')|| hasErrors;
            if(hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing admin homepage...');
                //TODO: use the ajax capabilities of the plugin
                $(tabulationContainerId).tabs();
                $(removeButtonClass).click(function() {
                    return window.confirm('Do you rilly want to rimove this ?');
                });
                logger.debug('... done!');
            }
        }
    };
});
