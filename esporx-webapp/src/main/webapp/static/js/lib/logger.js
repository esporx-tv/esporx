define([], function() {
    "use strict";
	var _caller = '';
	var _debugActive = true;
    
    return {
        setCaller : function(caller) {
            _caller = caller;
        },
        
        debug : function(text) {
            if (_debugActive) {
                try {
                    console.debug('[' + _caller + '] ' + text);
                } catch (e) {
                }
            }
        },
        error : function(text) {
            try {
                console.error('[' + _caller + '] ' + text);
            } catch (e) {
                alert('[' + _caller + '][error] ' + text);
            }
        }
    }
});