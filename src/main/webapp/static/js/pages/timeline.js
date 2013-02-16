define([
    "jquery",
    "lib/logger",
    "lib/sanityChecker",
    "underscore"], function($, logger, sanityChecker, _) {

    "use strict";

    var filterId = "#gameFilter",
        hasErrors = false;

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(filterId, 'Game filter') || hasErrors;

            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            } else {
                logger.debug('Initializing game filter...');
                $(filterId).bind("change", function() {
                    var selectedGameId = parseInt($(filterId).val(), 10),
                        allOccurrences = $("article[data-game]");

                    _.map(allOccurrences, function(occ) {
                        if(selectedGameId === -1 || $(occ).data("game") === selectedGameId) {
                            $(occ).show();
                        } else {
                            $(occ).hide();
                        }
                    });
                });
                logger.debug('... done!');
            }
        }
    };
});
