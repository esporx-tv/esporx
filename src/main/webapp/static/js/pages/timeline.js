define([
    "jquery",
    "lib/logger",
    "lib/sanityChecker",
    "underscore"], function($, logger, sanityChecker, _) {

    "use strict";

    var filterId = "#gameFilter",
        iconClass = ".gameIcon",
        hasErrors = false;

    return {
        trigger: function() {
            var $icons = $(filterId + " " + iconClass);
            hasErrors = sanityChecker.checkIfNotExists(filterId, 'Game filter') || hasErrors;

            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            } else {
                logger.debug('Initializing game filter...');
                $icons.click(function() {
                    var selectedGameIds,
                        allOccurrences = $("article[data-game]"),
                        newStatus = ($(this).data("selected") === undefined) ? true : !($(this).data("selected").value);

                    $(this)
                        .toggleClass("selected", newStatus)
                        .data("selected", {value:newStatus});


                    selectedGameIds = _.map(
                        _.filter(
                            $icons,
                            function(icon) {
                                var status = $(icon).data("selected");
                                return !(_.isUndefined(status)) && status.value;
                            }
                        ),
                        function(icon) {
                            return $(icon).data("game-id");
                        }
                    );

                    logger.debug(selectedGameIds);

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
