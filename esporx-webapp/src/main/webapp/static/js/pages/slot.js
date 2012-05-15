"use strict";
var Slot = Class.create({
	initialize: function() {
		var controllerName = 'Slot';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var slotCheckboxId = "active1";
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + slotCheckboxId, 'Slot checkbox')|| hasErrors;
		if (hasErrors) {
			eventLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			eventLogger.debug("Initialize slot js");
			$(slotCheckboxId).observe("click", function(event) {
				var element = Event.element(event);
				if(element.checked && !window.confirm("Activating this slot will disable all other slots in the same language and position. Confirm your action ?")) {
					Event.stop(event);
				}
			});
		}
		
	}
});


document.observe("dom:loaded",function() {
	new Slot();
});