/*global JSON:false
noty:false*/

define(["jquery", "lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/occurrenceForm.tpl", "lib/dateUtils", "underscore", "ext/json2", "lib/notyHelper"], function($, logger, sanityChecker, templateHelper, eventTemplate, dateUtils, _) {
    "use strict";

    var eventSelectId = "#event",
        occurrenceCreationId = "#add_occurrence",
        submitInputId = "#submit",
        submitInput,
        occurrenceContainerClass = ".occurrence",
        closeButtonClass = ".close",
        hasErrors = false,
        frequencyTypes = [],
        channels = [],
        maxLoop = 0,
        frequencyUrl = '/admin/frequencyTypes',
        channelUrl = '/admin/channels',
        eventOccurrencesUrl = '/admin/event/{ID}/occurrences',
        deleteOccurrenceUrl = '/admin/occurrence/{ID}',
        occurrencePostUrl = '/admin/occurrence',

        retrieveFrequencyTypes = function() {
            $.getJSON(frequencyUrl, function(data) {
                frequencyTypes = _.map(data, function(item) {return {value: item};});
            });
        },
        simplifyChannel = function(item) {
            return {value: item.id, name: item.name};
        },
        retrieveChannels = function() {
            $.getJSON(channelUrl, function(data) {
                channels = _.map(data, simplifyChannel);
            });
        },
        observeClose = function(element) {
            $(element).click(function() {
                var parent = $(element).parent(occurrenceContainerClass),
                    occurrenceId = $('.occurrenceId', parent).val();
                if (occurrenceId !== null && occurrenceId !== '') {
                    noty({text: 'Do you really want to delete this occurrence', type: 'confirm', theme: 'defaultTheme', layout: 'center', dismissQueue: false,
                        buttons: [
                            {addClass: 'btn btn-primary', text: 'Ok', onClick: function($noty) {
                                deleteOccurrenceUrl = deleteOccurrenceUrl.replace('{ID}', occurrenceId);
                                $.ajax(deleteOccurrenceUrl, {
                                    type: 'DELETE'
                                })
                                    .done(function(data) {
                                        if (data === "OK") {
                                            $(parent).remove();
                                            $noty.close();
                                            noty({text: 'Deleted occurrence with ID :' + occurrenceId, type: 'success', theme: 'defaultTheme', layout: 'center'});
                                        }
                                        else {
                                            $noty.close();
                                            noty({text: 'Problem occurred while trying to delete occurrence with ID :' + occurrenceId, type: 'error', theme: 'defaultTheme', layout: 'center'});
                                        }
                                    });
                            }
                            },
                            {addClass: 'btn btn-danger', text: 'Cancel', onClick: function($noty) {
                                $noty.close();
                            }
                            }
                        ]
                    });
                } else {
                    $(parent).remove();
                }
            });
        },
        transformArrayToString = function(myArray) {
            if (myArray === null) {
                return "";
            }
            return _.reduce(myArray, function(prev, id){ return prev + ',' + id;}, '').substring(1);
        },
        insertOccurrence = function(loop, frequencyTypes, channels, selectedFrequencyType, selectedChannels, selectedStartDate, selectedEndDate, id) {
            selectedStartDate = dateUtils.formatDate(selectedStartDate, 'dd/MM/yyyy HH:mm');
            selectedEndDate = dateUtils.formatDate(selectedEndDate, 'dd/MM/yyyy HH:mm');
            selectedFrequencyType = selectedFrequencyType || [];
            selectedChannels = selectedChannels || [];
            logger.debug('adding 1 occurrence...');

            frequencyTypes.__selected__ = [selectedFrequencyType];
            channels.__selected__ = selectedChannels;

            submitInput.before(
                templateHelper.template(eventTemplate, {
                    icon : $.withBaseImgUrl("close-icon.gif"),
                    loop : loop,
                    frequencies : frequencyTypes,
                    channels : channels,
                    startDate : selectedStartDate,
                    endDate : selectedEndDate,
                    id : id
                })
            );
            observeClose($(closeButtonClass).last());
            dateUtils.observeAll();
            logger.debug('... done!');
        },
        observeEventChange = function() {
            $(eventSelectId).change(function() {
                var i,
                    occurrence,
                    eventId = $(eventSelectId).val(),
                    url = eventOccurrencesUrl.replace('{ID}', eventId);
                $(occurrenceContainerClass).remove();
                if (eventId !== '') {
                    $.getJSON(url, function(data) {
                        maxLoop = 0;
                        for(i = 0; i < data.length; i++) {
                            occurrence = data[i];
                            insertOccurrence(maxLoop++, frequencyTypes, channels, occurrence.frequencyType, _.map(occurrence.channels, simplifyChannel), occurrence.startDate, occurrence.endDate, occurrence.id);
                        }
                    });
                }
            });
        },
        observeSubmit = function() {
            $('#occurrenceCommand').submit(function(event) {
                _.each($(occurrenceContainerClass), function(item) {
                    var valid = [],
                        errors = [],
                        startDate, endDate, frequency, id, channelList,eventId;
                    eventId = $(eventSelectId).val();
                    startDate = $('.startDate', item).first().val();
                    endDate = $('.endDate', item).first().val();
                    frequency = $('.frequency', item).first().val();
                    id = $('.occurrenceId', item).first().val();
                    channelList = transformArrayToString($('.channels', item).first().val());

                    if (eventId.trim() === '') {
                        errors.push($(eventSelectId));
                    }
                    else {
                        valid.push($(eventSelectId));
                    }
                    if (startDate.trim() === '') {
                        errors.push($('.startDate', item).first());
                    }
                    else {
                        valid.push($('.startDate', item).first());
                    }
                    if (frequency.trim() === '') {
                        errors.push($('.frequency', item).first());
                    }
                    else {
                        valid.push($('.frequency', item).first());
                    }
                    if (channelList.trim() === '') {
                        errors.push($('.channels', item).first());
                    }
                    else {
                        valid.push($('.channels', item).first());
                    }

                    if (errors.length > 0) {
                        _.each(errors, function(element) {
                            $(element).css('border','1px solid red');
                        });
                        return false;
                    }
                    else {
                        _.each(valid, function(element) {
                            $(element).css('border','inherit');
                        });
                        $.post(occurrencePostUrl, {
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
                                    noty({text: 'Saved occurrence with ID :' + id, theme: 'defaultTheme', layout: 'bottomRight'});
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
                observeEventChange();
                maxLoop = $(occurrenceContainerClass).length;
                $(occurrenceCreationId).click(function() {
                    insertOccurrence(maxLoop++, frequencyTypes, channels);
                });
                observeSubmit();
                logger.debug('... done!');
            }
        }
    };
});
