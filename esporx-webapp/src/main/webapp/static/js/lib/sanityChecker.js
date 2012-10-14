/*global $: true
$$: true*/
define(["lib/logger", "ext/prototype"], function(logger) {
    "use strict";

    var target,
        elementId;

    return {
        checkIfNotExists : function(elementIdOrClass, errorMessage) {
            if (elementIdOrClass.startsWith('#')) {
                elementId = elementIdOrClass.substring(1);
                target = $(elementId);
            } else {
                target = $$(elementIdOrClass);
            }

            if(target === null) {
                logger.error(errorMessage+" not found!");
                return true;
            }
            return false;
        }
    };
});