define(["jquery", "lib/logger", "lib/sanityChecker", "ext/ckeditor/ckeditor_basic"], function($, logger, sanityChecker) {
    "use strict";

    //TODO: this should be configurable
	var channelTitleInputId = "title",
        descriptionInputElementId = "description",
        formElementId = "channelCommand",
        loadingIconId = "loadingIcon",
        channelSubmitInputId = "submitChannel",
        videoUrlInputElementId = "videoUrl",
        videoProviderInputElementId = "provider",
        supportedUrlClass = 'supportedUrl',
        unsupportedUrlClass = 'unsupportedUrl',
        findVideoProviderUrl = '/video/matchProvider',
        hasErrors = false,
        input,
        videoUrl,
        request,
        response;

	return {
        //TODO: I'm too fat! make me slim!
        trigger : function() {
            hasErrors = sanityChecker.checkIfNotExists('#' + channelTitleInputId, 'Channel title input')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + descriptionInputElementId, 'Description textarea element')	|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + formElementId, 'Form element')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + loadingIconId, 'Loading icon')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + videoUrlInputElementId, 'Video URL text input') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + videoProviderInputElementId, 'Video provider input') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + channelSubmitInputId, 'Form submit input') || hasErrors;

            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            } else {
                $('#' + channelSubmitInputId).disable();
                $('#' + channelTitleInputId).focus();

                $('#' + videoUrlInputElementId).blur(function(event) {
                    input = $(this);
                    videoUrl = $(input).text();
                    if (videoUrl.is(':empty')) {
                        input.removeClass(unsupportedUrlClass);
                        input.removeClass(supportedUrlClass);
                    } else {
                        request = $.ajax({
                            url: findVideoProviderUrl,
                            type : 'GET',
                            data : {
                                url : videoUrl
                            }
                        }).done(function(response) {
                            if ($.trim(response) !== "") {
                                logger.debug('Match found: '+ response);
                                if(response === 'true') {
                                    $('#' + channelSubmitInputId).enable();
                                    input.removeClass(unsupportedUrlClass);
                                    input.addClass(supportedUrlClass);
                                } else {
                                    input.removeClass(supportedUrlClass);
                                    input.addClass(unsupportedUrlClass);
                                }
                            }
                        }).fail(function() {
                            logger.debug('Request failed - could not find any matching video provider');
                        });
                    }
                });

                $('#' + formElementId).submit(function() {
                    $('#' + videoProviderInputElementId).val($('#' + videoUrlInputElementId).val());
                });
            }
        }
    };
});