/*global twitterlib: true*/
define([
    "jquery",
    "lib/logger",
    "lib/sanityChecker",
    "lib/handlebarsHelper",
    "text!tpl/tweet.tpl",
    "underscore",
    "ext/twitterlib",
    "ext/ckeditor/ckeditor_basic"], function($, logger, sanityChecker, templateHelper, tweetTemplate, _) {

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
            displayTweets: function(tweets) {
                var htmlTweets = $("#tweets ul");
                _.map(tweets, function(tweet) {
                    var html = $(templateHelper.template(tweetTemplate, {
                        "from_user": tweet.from_user,
                        "text": tweet.text
                    }));
                    htmlTweets.append(html);
                });
            },
            fetchTweets: function(str) {
                if(typeof str === "undefined" || str.length === 0) {
                    return;
                } else {
                    var pattern = str.replace(",", " OR "),
                        that = this;
                    twitterlib.search(pattern, {limit: 10}, function(tweets, options) {
                        that.displayTweets(tweets);
                    });
                }
            }
        };
    });
