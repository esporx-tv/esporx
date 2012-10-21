/*global JSON:false*/
define(["jquery", "lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/occurrenceForm.tpl", "lib/dateUtils", "underscore", "ext/json2"], function($, logger, sanityChecker, templateHelper, eventTemplate, dateUtils, _) {
    "use strict";

    var eventSelectId = "#event",
        occurrenceCreationId = "#add_occurrence",
        submitInputId = "#submit",
        submitInput,
        occurrenceContainerClass = ".occurrence",
        closeButtonClass = ".close",
        hasErrors = false,
        frequencyTypes = [],
        frequencyUrl = '/frequencyTypes',
        channels = [],
        channelUrl = '/channel/all',
        retrieveFrequencyTypes = function() {
            $.getJSON(frequencyUrl, function(data) {
                frequencyTypes = _.map(data, function(item) {return {value: item};});
            });
        },
        retrieveChannels = function() {
            $.getJSON(channelUrl, function(data) {
                channels = _.map(data, function(item) {return {value: item.id, name: item.name};});
            });
        },
        observeClose = function(element) {
            $(element).click(function() {
                //$(element).
                logger.debug("DELETE TODO: if ID, http DELETE, else reorder elements");
            });
        },
        transformArrayToString = function(myArray) {
            return _.reduce(myArray, function(prev, id){ return prev + ',' + id;}, '').substring(1);
        },
        observeSubmit = function() {
            $('#occurrenceCommand').submit(function(event) {
                _.each($(occurrenceContainerClass), function(item) {
                    var startDate, endDate, frequency, id, channelList,eventId;
                    eventId = $(eventSelectId).val();
                    if(eventId.trim() === '') {
                        $(eventSelectId).css('border','1px solid red');
                        return false;
                    }
                    else {
                        $(eventSelectId).css('border','inherit');

                        startDate = $('.startDate', item).first().val();
                        endDate = $('.endDate', item).first().val();
                        frequency = $('.frequency', item).first().val();
                        id = $('.occurrenceId', item).first().val();
                        channelList = transformArrayToString($('.channels', item).first().val());
                        $.post('/occurrence', {
                            data: {
                                id:id,
                                startDate:startDate,
                                endDate:endDate,
                                frequencyType:frequency,
                                channels: channelList,
                                eventId:eventId
                            }
                        }).done(function (result) {
                            var id = parseInt(result, 10);
                            if(!isNaN(id)) {
                                $('.occurrenceId', item).first().val(id);
                            }
                        });
                    }
                });
                event.stopPropagation();
                return false;
            });
        };

    return {
        trigger: function() {
            hasErrors = sanityChecker.checkIfNotExists(eventSelectId, 'Event select input') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(occurrenceCreationId, 'Occurrence creation button') || hasErrors;
            hasErrors = sanityChecker.checkIfNotExists(submitInputId, 'Submit button') || hasErrors;

            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            } else {
                logger.debug('Initializing event form ...');
                dateUtils.init();
                retrieveFrequencyTypes();
                retrieveChannels();
                submitInput = $(submitInputId);
                $(eventSelectId).focus();
                $(occurrenceCreationId).click(function() {
                    var loop = $(occurrenceContainerClass).length;
                    logger.debug('adding 1 occurrence...');
                    submitInput.before(
                        templateHelper.template(eventTemplate, {
                            icon : $.withBaseImgUrl("close-icon.gif"),
                            loop : loop,
                            frequencies : frequencyTypes,
                            channels : channels
                        })
                    );
                    observeClose($(closeButtonClass).last());
                    dateUtils.observeAll();
                    logger.debug('... done!');
                });
                observeSubmit();
                logger.debug('... done!');
            }
        }
    };
});
