/*global twitterlib: true*/
define(
    ["jquery", "lib/logger", "lib/sanityChecker", "underscore", "ext/twitterlib", "ext/ckeditor/ckeditor_basic"],
    function($, logger, sanityChecker, _) {

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
                    var html = $(
                        '<li class="tweet">' +
                            '<span class="tweet_author">' + tweet.from_user + '</span> ' +
                            '<br />' +
                            '<span class="tweet_content">' + tweet.text + '</span> ' +
                            '</li>'
                    );
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
