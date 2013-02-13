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

    //TODO: this should be configurable
    var channelTitleInputId = "#title",
        descriptionInputElementId = "#description",
        formElementId = "#channelCommand",
        loadingIconId = "#loadingIcon",
        channelSubmitInputId = "#submitChannel",
        videoUrlInputElementId = "#videoUrl",
        videoProviderInputElementId = "#provider",
        supportedUrlClass = 'supportedUrl',
        unsupportedUrlClass = 'unsupportedUrl',
        findVideoProviderUrl = '/video/matchProvider',
        hasErrors = false,
        input,
        request,
        checkProvider = function(urlInput) {
            var $input = $(urlInput),
                inputValue = $input.val().trim();
            if (inputValue === '') {
                logger.debug('Nothing to send!');
                $input.removeClass(unsupportedUrlClass);
                $input.removeClass(supportedUrlClass);
            } else {
                logger.debug('Checking ['+inputValue+'] against our records...');
                request = $.ajax(findVideoProviderUrl, {
                    type : 'GET',
                    data : {
                        url : inputValue
                    }
                }).done(function(response) {
                    if ($.trim(response) !== "") {
                        logger.debug('Match found: '+ response);
                        if(response === 'true') {
                            $(channelSubmitInputId).prop('disabled', false);
                            $input.removeClass(unsupportedUrlClass);
                            $input.addClass(supportedUrlClass);
                        } else {
                            $input.removeClass(supportedUrlClass);
                            $input.addClass(unsupportedUrlClass);
                        }
                    }
                }).fail(function() {
                    logger.debug('Request failed - could not find any matching video provider');
                });
            }
        },
        observeVideoUrlCheck = function(input) {
            var $urlInput = $(input),
                url = $urlInput.val().trim();

            $(channelSubmitInputId).prop('disabled', true);

            if(url !== '' && url !== 'http://') {
                checkProvider(videoUrlInputElementId);
            }
            $urlInput.focusout(function(event) {
                checkProvider(videoUrlInputElementId);
            });
        };

    return {
        //TODO: I'm too fat! make me slim!
        trigger : function() {
            hasErrors = sanityChecker.checkIfNotExists(channelTitleInputId, 'Channel title input')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(descriptionInputElementId, 'Description textarea element')	|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(formElementId, 'Form element')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(loadingIconId, 'Loading icon')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(videoUrlInputElementId, 'Video URL text input') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(videoProviderInputElementId, 'Video provider input') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(channelSubmitInputId, 'Form submit input') || hasErrors;

            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            } else {
                observeVideoUrlCheck(videoUrlInputElementId);
                $(channelTitleInputId).focus();
                $(formElementId).submit(function() {
                    $(videoProviderInputElementId).val($(videoUrlInputElementId).val());
                });
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