var Logger = Class.create({
	_caller : '',
	debugActive : true,
	initialize : function(caller) {
		this._caller = caller;
	},
	debug : function(text) {
		if (this.debugActive) {
			try {
				console.debug('[' + this._caller + '] ' + text);
			} catch (e) {
			}
		}
	},
	error : function(text) {
		try {
			console.error('[' + this._caller + '] ' + text);
		} catch (e) {
			alert('[' + this._caller + '][error] ' + text);
		}
	}
});