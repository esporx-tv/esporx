"use strict";
document.observe("dom:loaded",function() {
	new LocalStorageChecker();
	var linkDetector = new ExternalLinkDetector();
	linkDetector.scan();
});