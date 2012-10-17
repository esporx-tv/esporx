define(["jquery", "lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/eventForm.tpl", "lib/dateUtils", "ext/ckeditor/ckeditor_basic"], function($, logger, sanityChecker, templateHelper, eventTemplate, dateUtils) {
    "use strict";

    var eventTitleInputId = "#title",
        occurrenceCreationId = "#add_occurrence",
        submitInputId = "#submit",
        submitInput,
        occurrenceContainerClass = ".occurrence",
        hasErrors = false,
        frequencyTypes = [],
        frequencyUrl = '/frequencyTypes',
        retrieveFrequencyTypes = function() {
            var result = [], i, length;
            $.getJSON(frequencyUrl, function(data) {
                for(i = 0, length = data.length; i < length; i++) {
                    result.push({value: data[i]});
                }
            });
            return result;
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
               frequencyTypes = retrieveFrequencyTypes();
               submitInput = $(submitInputId);
               $(eventTitleInputId).focus();
               $(occurrenceCreationId).click(function(event) {
                   var loop = $(occurrenceContainerClass).length;
                   logger.debug('adding 1 occurrence...');
                   submitInput.before(
                       templateHelper.template(eventTemplate, {
                           loop : loop,
                           frequencies : frequencyTypes
                       })
                   );
                   dateUtils.observeAll();
                   logger.debug('... done!');
               });
               logger.debug('... done!');
           }
       }
    };
});
