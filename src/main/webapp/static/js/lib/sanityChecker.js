define(["jquery", "lib/logger"], function($, logger) {
    "use strict";

    var elementId;

    return {
        checkIfNotExists : function(elementIdOrClass, errorMessage) {
            if($(elementId) === null) {
                logger.error(errorMessage+" not found!");
                return true;
            }
            return false;
        }
    };
});