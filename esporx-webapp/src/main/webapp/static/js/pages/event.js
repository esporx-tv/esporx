define(["jquery", "lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/eventForm.tpl", "lib/dateUtils", "underscore", "ext/ckeditor/ckeditor_basic"], function($, logger, sanityChecker, templateHelper, eventTemplate, dateUtils, _) {
    "use strict";

    var eventTitleInputId = "#title",
        occurrenceCreationId = "#add_occurrence",
        submitInputId = "#submit",
        submitInput,
        occurrenceContainerClass = ".occurrence",
        closeButtonClass = ".close",
        observeCloseButtonClass = ".closeObserved",
        hasErrors = false,
        frequencyTypes = [],
        frequencyUrl = '/frequencyTypes',
        retrieveFrequencyTypes = function() {
            $.getJSON(frequencyUrl, function(data) {
                frequencyTypes = _.map(data, function(item) {return {value: item};});
            });
        },
        observeClose = function() {
            _.each($(closeButtonClass).not(observeCloseButtonClass), function(element) {
                $(element).addClass(observeCloseButtonClass.substring(1));
                $(element).click(function() {
                    logger.debug("DELETE TODO: if ID, http DELETE, else reorder elements");
                });
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
                   observeClose();
                   dateUtils.observeAll();
                   logger.debug('... done!');
               });
               logger.debug('... done!');
           }
       }
    };
});
