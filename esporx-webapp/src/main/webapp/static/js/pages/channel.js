define(["lib/logger", "lib/sanityChecker", "ext/ckeditor/ckeditor_basic"], function(logger, sanityChecker) {
    "use strict";

    //TODO: this should be configurable
	var channelTitleInputId = "title";
	var descriptionInputElementId = "description";
	var formElementId = "channelCommand";
	var loadingIconId = "loadingIcon";
    var channelSubmitInputId = "submitChannel";
    var videoUrlInputElementId = "videoUrl";
    var videoProviderInputElementId = "provider";

    var supportedUrlClass = 'supportedUrl';
    var unsupportedUrlClass = 'unsupportedUrl';

    var findVideoProviderUrl = '/video/matchProvider';

	return {
        //TODO: I'm too fat! make me slim!
        trigger : function() {
            logger.setCaller('Channel');
            var hasErrors = false;
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
                $(channelSubmitInputId).disable();
                $(channelTitleInputId).focus();

                $(videoUrlInputElementId).observe('blur',function(event) {
                    var input = Event.element(event);
                    var videoUrl = $F(input).stripTags().strip();
                    if (videoUrl.empty()) {
                        input.removeClassName(unsupportedUrlClass);
                        input.removeClassName(supportedUrlClass);
                    } else {
                        new Ajax.Request(findVideoProviderUrl, {
                            method : 'get',
                            parameters : {
                                url : videoUrl
                            },
                            onSuccess : function(transport) {
                                var response = transport.responseText;
                                if (!response.empty()) {
                                    logger.debug('Match found: '+ response);
                                    if(response == 'true') {
                                        $(channelSubmitInputId).enable();
                                        input.removeClassName(unsupportedUrlClass);
                                        input.addClassName(supportedUrlClass);
                                    } else {
                                        input.removeClassName(supportedUrlClass);
                                        input.addClassName(unsupportedUrlClass);
                                    }
                                }
                            },
                            onFailure : function() {
                                logger.debug('Request failed - could not find any matching video provider');
                            }
                        });
                    };
                });

                $(formElementId).observe('submit', function() {
                    $(videoProviderInputElementId).value = $(videoUrlInputElementId).value;
                });
            }
        }
    }
});