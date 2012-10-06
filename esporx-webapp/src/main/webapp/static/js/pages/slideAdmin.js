define(["lib/logger", "lib/dateUtils"], function(logger, dateUtils) {
    "use strict";

    return {
        trigger : function() {
            logger.setCaller("Gondola slide admin");
            logger.debug("Initializing gondola slide ...");
            dateUtils.trigger();
            logger.debug("... done!");
        }
    }
});