/*global twitterlib: true*/
define([
    "jquery",
    "lib/logger",
    "lib/stringUtils",
    "lib/handlebarsHelper",
    "text!tpl/tweet.tpl",
    "underscore",
    "ext/async",
    "ext/twitterlib"], function($, logger, stringUtils, templateHelper, tweetTemplate, _, async) {

    var displayTweets = function(tweets) {
        var htmlTweets = $("#tweets ul");
        _.map(tweets, function(tweet) {
            var html = $(templateHelper.template(tweetTemplate, {
                "from_user": tweet.from_user,
                "text": tweet.text
            }));
            htmlTweets.append(html);
        });
    };

    return {
        tweetWall: function(accountId, searchQuery) {
            var limit = {limit: 10};
            if (stringUtils.isEmpty(searchQuery) && stringUtils.isEmpty(accountId)) {
                return;
            } else {
                async.parallel([
                    function(callback) {
                        if (stringUtils.isEmpty(searchQuery)) {
                            callback(null, []);
                        }
                        else {
                            logger.debug("About to fetch tweets matching: " + searchQuery);
                            var pattern = searchQuery.replace(",", " OR ");
                            twitterlib.search(pattern, limit, function(tweets) {
                                callback(null, tweets);
                            });
                        }
                    },
                    function(callback) {
                        if (stringUtils.isEmpty(accountId)) {
                            callback(null, []);
                        }
                        else {
                            logger.debug("About to fetch tweets from: " + accountId);
                            twitterlib.timeline(accountId, limit, function(tweets) {
                                callback(
                                    null,
                                    _.map(tweets, function(tweet) {
                                        tweet.from_user = accountId.substring(1);
                                        return tweet;
                                    })
                                );
                            });
                        }
                    }
                ], function(err, results) {
                    var tweets = _.sortBy(
                        _.union(results[0], results[1]),
                        function(tweet) {
                            return -1 * tweet.id;
                        }
                    ).slice(0,10);
                    displayTweets(tweets);
                });
            }
        }
    };
});
