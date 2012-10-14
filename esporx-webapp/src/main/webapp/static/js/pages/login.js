/*global $: true*/
define(["lib/logger", "lib/sanityChecker"], function(logger, sanityChecker) {
    "use strict";
    var loginInputId = "j_username";

    return {
        focus: function(loginInputId) {
            var hasErrors = false;
            hasErrors = sanityChecker.checkIfNotExists('#' + loginInputId, 'Login input')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug("Initializing login...");
                $(loginInputId).focus();
                logger.debug("... done!");
            }
        }
    };
});
