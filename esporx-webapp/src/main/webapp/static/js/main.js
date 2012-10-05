require(["lib/externalLinkDetector", "lib/localStorageChecker", "ext/prototype"], function(externalLinkDetector, localStorageChecker) {
    localStorageChecker.check();
    document.observe('dom:loaded', function() {
        externalLinkDetector.scan();
    });
});