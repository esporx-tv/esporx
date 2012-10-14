define(["jquery", "lib/logger", "lib/sanityChecker"], function($, logger, sanityChecker) {
    "use strict";

    var slotCheckboxId = "active1",
        hasErrors = false;

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists('#' + slotCheckboxId, 'Slot checkbox')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug("Initializing script...");
                $('#' + slotCheckboxId).click(function(event) {
                    if($(this).is(':checked') && !window.confirm("Activating this slot will disable all other slots in the same language and position. Confirm your action ?")) {
                        event.stopPropagation();
                    }
                });
                logger.debug("... done!");
            }
        }
    };
});