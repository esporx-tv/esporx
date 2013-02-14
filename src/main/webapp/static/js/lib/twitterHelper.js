/*global twitterlib: true*/
define([
    "jquery",
    "lib/logger",
    "lib/handlebarsHelper",
    "text!tpl/tweet.tpl",
    "underscore",
    "ext/async",
    "ext/twitterlib"], function($, logger, templateHelper, tweetTemplate, _, async) {

    var displayTweets = function(tweets) {
        var htmlTweets = $("#tweets ul");
        _.map(tweets, function(tweet) {
            var html = $(templateHelper.template(tweetTemplate, {
                "from_user": tweet.from_user,
                "text": tweet.text
            }));
            htmlTweets.append(html);
        });
    }, isEmpty = function(string) {
        if (string === undefined || string === null || string.length === 0) {
            return;
        }
    };

    return {
        tweetWall: function(accountId, searchQuery) {
            var limit = {limit: 10};
            if (isEmpty(searchQuery) && isEmpty(accountId)) {
                return;
            } else {
                async.parallel([
                    function(callback) {
                        if (isEmpty(searchQuery)) {
                            callback(null, []);
                        }
                        else {
                            var pattern = searchQuery.replace(",", " OR ");
                            twitterlib.search(pattern, limit, function(tweets) {
                                callback(null, tweets);
                            });
                        }
                    },
                    function(callback) {
                        if (isEmpty(accountId)) {
                            callback(null, []);
                        }
                        else {
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
                ], function(err, results){
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
