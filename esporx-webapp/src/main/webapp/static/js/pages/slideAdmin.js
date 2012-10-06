define(["lib/logger", "lib/dateUtils"], function(logger, dateUtils) {
    "use strict";
    logger.setCaller("Gondola slide admin");

    return {
        trigger : function() {
            logger.debug("Initializing gondola slide ...");
            dateUtils.trigger();
            logger.debug("... done!");
        }
    }
});