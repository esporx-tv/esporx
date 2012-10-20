/*global JSON:false*/
define(["jquery", "lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/eventForm.tpl", "lib/dateUtils", "underscore", "ext/ckeditor/ckeditor_basic", "ext/json2"], function($, logger, sanityChecker, templateHelper, eventTemplate, dateUtils, _) {
    "use strict";

    var eventTitleInputId = "#title",
        occurrenceCreationId = "#add_occurrence",
        submitInputId = "#submit",
        submitInput,
        occurrenceContainerClass = ".occurrence",
        closeButtonClass = ".close",
        hasErrors = false,
        frequencyTypes = [],
        frequencyUrl = '/frequencyTypes',
        retrieveFrequencyTypes = function() {
            $.getJSON(frequencyUrl, function(data) {
                frequencyTypes = _.map(data, function(item) {return {value: item};});
            });
        },
        observeClose = function(element) {
            $(element).click(function() {
                //$(element).
                logger.debug("DELETE TODO: if ID, http DELETE, else reorder elements");
            });
        },
        observeSubmit = function() {
            $('#eventCommand').submit(function(event) {
                _.each($(occurrenceContainerClass), function(item) {
                    var startDate, endDate, frequency, id;
                    startDate = $('.startDate', item).first().val();
                    endDate = $('.endDate', item).first().val();
                    frequency = $('.frequency', item).first().val();
                    id = $('.eventId', item).first().val();
                    $.post('/occurrence', {
                        data: {id:id,startDate:startDate, endDate:endDate, frequencyType:frequency}
                    }).done(function (result){
                        logger.debug('RESULT :  ' + result);
                    });
                });
                event.stopPropagation();
                return false;
            });
        };

    return {
       trigger: function() {
           hasErrors = sanityChecker.checkIfNotExists(eventTitleInputId, 'Event title input') || hasErrors;
           hasErrors = sanityChecker.checkIfNotExists(occurrenceCreationId, 'Occurrence creation button') || hasErrors;
           hasErrors = sanityChecker.checkIfNotExists(submitInputId, 'Submit button') || hasErrors;

           if (hasErrors) {
               logger.error('Script initialization failed due to multiple errors');
           } else {
               logger.debug('Initializing event form ...');
               dateUtils.init();
               retrieveFrequencyTypes();
               submitInput = $(submitInputId);
               $(eventTitleInputId).focus();
               $(occurrenceCreationId).click(function() {
                   var loop = $(occurrenceContainerClass).length;
                   logger.debug('adding 1 occurrence...');
                   submitInput.before(
                       templateHelper.template(eventTemplate, {
                           icon : $.withBaseImgUrl("close-icon.gif"),
                           loop : loop,
                           frequencies : frequencyTypes
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
