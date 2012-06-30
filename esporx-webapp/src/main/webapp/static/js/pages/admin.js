"use strict";
var Admin = Class.create({
	initialize: function() {
		var controllerName = 'Admin';
		var adminLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var slideContainerId = "showSlides";
		var slotContainerId = "showSlots";
		var channelContainerId = "showChannels";
		var eventContainerId = "showEvents";
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#' + slideContainerId, 'Slide container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + slotContainerId, 'Slot container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + channelContainerId, 'Channel container input')|| hasErrors;
		hasErrors = sanityChecker.checkIfNotExists('#' + eventContainerId, 'Event container input')|| hasErrors;
		if (hasErrors) {
			adminLogger.error('Script initialization failed due to multiple errors');
		}
		else {
			adminLogger.debug("Initialize admin js");
			$(slideContainerId).observe("click", function () {
				this.display($("slidesContainer"));
				this.active($(slideContainerId));
			}.bind(this));
			$(slotContainerId).observe("click", function() {
				this.display($("slotsContainer"));
				this.active($(slotContainerId));
			}.bind(this));
			$(channelContainerId).observe("click", function() {
				this.display($("channelsContainer"));
				this.active($(channelContainerId));
			}.bind(this));
			$(eventContainerId).observe("click", function() {
				this.display($("eventsContainer"));
				this.active($(eventContainerId));
			}.bind(this));
			
			$$(".gondolaRemove").each(function(element) {
				element.observe("click", function(event) {
					if(!window.confirm("Do you really want to delete this slide?")) {
						Event.stop(event);
					}
				});
			});
		}
		
	},

	display: function(containerToDisplay) {
		var ArrayContainer = new Array($("slidesContainer"), $("slotsContainer"), $("channelsContainer"), $("eventsContainer"));
//		var containerToDisplay = this;
		ArrayContainer.each(function(container) {
			if (container == containerToDisplay) {
				if (containerToDisplay.hasClassName("displayNone")) containerToDisplay.removeClassName("displayNone");
			} else {
				if (!container.hasClassName("displayNone")) container.addClassName("displayNone");
			}
		});
	},
	
	active: function(linkToActive) {
		console.log('test');
		var linkContainer = new Array($("showSlides"), $("showSlots"), $("showChannels"), $("showEvents"));
//		var linkToActive = this;
		linkContainer.each(function(link) {
			if (linkToActive == link) {
				if (!linkToActive.hasClassName("containerSelected"))  {
					linkToActive.removeClassName("adminName");
					linkToActive.addClassName("containerSelected");
				}
			} else {
				if (link.hasClassName("containerSelected")) {
					link.removeClassName("containerSelected");
					link.addClassName("adminName");
				}
			}
		});
	}
});


document.observe("dom:loaded",function() {
	new Admin();
});