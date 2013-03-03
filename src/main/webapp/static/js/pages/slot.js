define(["jquery", "lib/logger", "lib/sanityChecker", "lib/gridsterUtils"], function($, logger, sanityChecker, gridHelper) {
    "use strict";

    var saveButtonId = "#saveButton",
        containerId = ".gridster > ul",
        hasErrors = false;

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(saveButtonId, 'Save button')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug("Initializing script...");
                gridHelper.generateGrid(containerId);
                $(saveButtonId).click(function() {
                    gridHelper.saveGrid(containerId);
                });
                logger.debug("... done!");
            }
        }
    };
});