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
                    var selectedGameIds = _.map($(filterId).val(), function(gameId) {
                        return parseInt(gameId, 10);
                    }),
                    allOccurrences = $("article[data-game]");

                    _.map(allOccurrences, function(occ) {
                        if(_.indexOf(selectedGameIds, $(occ).data("game")) !== -1) {
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
