define(["lib/logger", "ext/modernizr"], function(logger) {
    "use strict";
	var testDummyKey = "__DUMMY_ESPORX_DATA__";
    var timestampKey = "__DUMMY_ESPORX_CHECK_TSTAMP__";
	var maxIterations = 100;
    var localStorage = window.localStorage;
    
    var _isLimitReached = function() {
        if (_hasBeenCheckedRecently()) {
            return false;
        }
        logger.debug("Storage clear check routine: started!");
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
        localStorage.setItem(timestampKey, new Date().getTime());
        logger.debug("Storage clear check routine: done!");
        return limitIsReached;
    };

    var _hasBeenCheckedRecently = function() {
        var previousTimestamp = localStorage.getItem(timestampKey);
        if (previousTimestamp == null) return false;
        return new Date().getTime() - previousTimestamp < 5 * 60 * 1000;
    }
    
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