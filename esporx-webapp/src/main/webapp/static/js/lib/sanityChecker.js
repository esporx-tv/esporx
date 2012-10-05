define(["lib/logger", "ext/prototype"], function(logger) {
    "use strict";
    logger.setCaller("SanityChecker");

    return {
        checkIfNotExists : function(elementIdOrClass, errorMessage) {
            var target;
            if (elementIdOrClass.startsWith('#')) {
                var elementId = elementIdOrClass.substring(1);
                target = $(elementId);
            } else {
                target = $$(elementIdOrClass);
            }

            if(target == null) {
                logger.error(errorMessage+" not found!");
                return true;
            }
            return false;
        }
    }
});