define(["lib/logger", "ext/modernizr"], function(logger) {
    "use strict";
	var testDummyKey = "__DUMMY_ESPORX_DATA__";
	var maxIterations = 100;
    var localStorage = window.localStorage;

    logger.setCaller("LocalStorageChecker");
    
    var _isLimitReached = function() {
        var count = 0;
        var limitIsReached = false;
        do {
            try {
                var previousEntry = localStorage.getItem(testDummyKey);
                var entry = (previousEntry == null ? "" : previousEntry) + "m";
                localStorage.setItem(testDummyKey, entry);
            }
            catch(e) {
                logger.debug("Limit exceeded after " + count + " iteration(s). Exception received: ");
                logger.debug(e);
                limitIsReached = true;
            }
        }
        while(!limitIsReached && count++ < maxIterations);
        localStorage.removeItem(testDummyKey);
        return limitIsReached;
    };
    
    var _clear = function() {
        try {
            localStorage.clear();
            logger.debug("Storage clear successfully performed");
        }
        catch(e) {
            logger.error("An error occurred during storage clear: ");
            logger.error(e);
        }
    }

    return {
        check: function() {
            var result = false;
            if (Modernizr.localstorage && _isLimitReached()) {
                _clear();
            }
            return result;
        }
    }
    
});