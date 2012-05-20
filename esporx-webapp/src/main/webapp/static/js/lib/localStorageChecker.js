"use strict";
var LocalStorageChecker = Class.create({
	testDummyKey: "__DUMMY_ESPORX_DATA__",
	maxIterations: 100,
	logger: new Logger("LocalStorageChecker"),
	initialize: function() {
		var result = false;
		if (Modernizr.localstorage && this._isLimitReached()) {  
			this._clear();
		}
		return result;
	},
	_isLimitReached: function() {
		var localStorage = window.localStorage;
		var count = 0;
		var limitIsReached = false;
		do {
			try {
				var previousEntry = localStorage.getItem(this.testDummyKey);
				var entry = (previousEntry == null ? "" : previousEntry) + "m";
				localStorage.setItem(this.testDummyKey, entry);
			}
			catch(e) {
				this.logger.debug("Limit exceeded after " + count + " iteration(s). Exception received: ");
				this.logger.debug(e);
				limitIsReached = true;
			}
		}
		while(!limitIsReached && count++ < this.maxIterations);
		localStorage.removeItem(this.testDummyKey);
		return limitIsReached;
	},
	_clear: function() {
		try {
			var localStorage = window.localStorage;
			localStorage.clear();
			this.logger.debug("Storage clear successfully performed");
		}
		catch(e) {
			this.logger.error("An error occurred during storage clear: ");
			this.logger.error(e);
		}
	}
});