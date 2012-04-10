var Admin = Class.create({
	initialize: function() {
		var controllerName = 'Admin';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		eventLogger.debug("Initialize admin js");
		$("showSlides").observe("click", this.display.bind($("slidesList")));
		$("showSlots").observe("click", this.display.bind($("slotsList")));
		$("showCasts").observe("click", this.display.bind($("castsList")));
		$("showEvents").observe("click", this.display.bind($("eventsList")));
		
		$$(".gondolaRemove").each(function(element) {
			element.observe("click", function(event) {
				if(!window.confirm("Do you really want to delete this slide?")) {
					Event.stop(event);
				}
			});
		});
	},

	display: function(event) {
		if (this.hasClassName("displayNone")) this.removeClassName("displayNone");
		else this.addClassName("displayNone");
	}
});


document.observe("dom:loaded",function() {
	new Admin();
});