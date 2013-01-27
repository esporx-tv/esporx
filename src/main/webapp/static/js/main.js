require(["jquery", "lib/externalLinkDetector"], function($, externalLinkDetector) {
    $(document).ready(function() {
        externalLinkDetector.scan();
    });
});