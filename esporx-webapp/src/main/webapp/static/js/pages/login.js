"use strict";
var Login = Class.create({
	initialize: function() {
		var controllerName = 'Login';
		var loginLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var loginInputId = "j_username";
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + loginInputId, 'Login input')|| hasErrors;
		if (hasErrors) {
			loginLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			loginLogger.debug("Initializing login...");
			$(loginInputId).focus();
		}
		
	}
});


document.observe("dom:loaded",function() {
	new Login();
});