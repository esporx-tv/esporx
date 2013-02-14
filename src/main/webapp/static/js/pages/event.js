define([
    "jquery",
    "lib/logger",
    "lib/sanityChecker",
    "lib/twitterHelper",
    "ext/ckeditor/ckeditor_basic"], function($, logger, sanityChecker, twitterHelper) {

        "use strict";

        var eventTitleInputId = "#title",
            hasErrors = false;

        return {
            trigger: function() {
                hasErrors = sanityChecker.checkIfNotExists(eventTitleInputId, 'Event title input') || hasErrors;

                if (hasErrors) {
                    logger.error('Script initialization failed due to multiple errors');
                } else {
                    logger.debug('Initializing event form ...');
                    $(eventTitleInputId).focus();
                    logger.debug('... done!');
                }
            },
            fetchTweets: function(accountId, search) {
                twitterHelper.tweetWall(accountId, search);
            }
        };
    });
