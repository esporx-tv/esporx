define([], function() {
    "use strict";
	var _debugActive = true;
    
    return {
        debug : function(text) {
            if (_debugActive) {
                try {
                    console.debug(text);
                } catch (e) {
                }
            }
        },
        error : function(text) {
            try {
                console.error(text);
            } catch (e) {
                alert('[error] ' + text);
            }
        }
    }
});