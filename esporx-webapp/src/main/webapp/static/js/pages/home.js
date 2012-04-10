/*
 * everything is wrapped in a self-invoked anonymous function to limit variables scope
 */
(function () {  
	const eventTitleInputId = "title";
	
	document.observe("dom:loaded",function() {
		var controllerName = 'Home';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
		
		if (hasErrors) {
			castLogger.error('Script initialization failed due to multiple errors');
		} else {
			
			$(eventTitleInputId).focus();
		};
	});
}());