define(["jquery", "lib/sanityChecker", "lib/logger"], function($, sanityChecker, logger) {
    "use strict";

    var hasErrors = false,
        userField = '#email';

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(userField, 'Email field')|| hasErrors;
            if(hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initializing registration form...');
                $(userField).focus();
                logger.debug('... done!');
            }
        }
    };
});
