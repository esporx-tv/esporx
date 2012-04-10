var SanityChecker = Class.create({
	_logger : null,
	initialize: function(caller) {
		this._logger = new Logger(caller);
	},
	checkIfNotExists : function(elementIdOrClass, errorMessage) {
		var target;
		if (elementIdOrClass.startsWith('#')) {
			var elementId = elementIdOrClass.substring(1);
			target = $(elementId);
		} else {
			target = $$(elementIdOrClass);
		}
		
		if(target == null) {
			this._logger.error(errorMessage+" not found!");
			return true;
		}
		return false;
	}
});