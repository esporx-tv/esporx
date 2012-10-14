require(["lib/externalLinkDetector", "ext/prototype"], function(externalLinkDetector, localStorageChecker) {
    document.observe('dom:loaded', function() {
        externalLinkDetector.scan();
    });
});