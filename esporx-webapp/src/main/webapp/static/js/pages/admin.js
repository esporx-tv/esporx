var Admin = Class.create({
	initialize: function() {
		var controllerName = 'Admin';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var slideContainerId = "showSlides";
		var slotContainerId = "showSlots";
		var castContainerId = "showCasts";
		var eventContainerId = "showEvents";
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + slideContainerId, 'Slide container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + slotContainerId, 'Slot container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + castContainerId, 'Cast container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + eventContainerId, 'Event container input')|| hasErrors;
		if (hasErrors) {
			eventLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			eventLogger.debug("Initialize admin js");
			$(slideContainerId).observe("click", this.display.bind($("slidesList")));
			$(slotContainerId).observe("click", this.display.bind($("slotsList")));
			$(castContainerId).observe("click", this.display.bind($("castsList")));
			$(eventContainerId).observe("click", this.display.bind($("eventsList")));
			
			$$(".gondolaRemove").each(function(element) {
				element.observe("click", function(event) {
					if(!window.confirm("Do you really want to delete this slide?")) {
						Event.stop(event);
					}
				});
			});
		}
		
	},

	display: function(event) {
		if (this.hasClassName("displayNone")) this.removeClassName("displayNone");
		else this.addClassName("displayNone");
	}
});


document.observe("dom:loaded",function() {
	new Admin();
});