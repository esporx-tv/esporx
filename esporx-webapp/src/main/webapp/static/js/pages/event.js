/*global $: true
 Event: true*/
define(["lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/eventForm.tpl", "lib/dateUtils", "ext/ckeditor/ckeditor_basic"], function(logger, sanityChecker, templateHelper, eventTemplate, dateUtils) {
    "use strict";

    var eventTitleInputId = "title",
        occurrenceCreationId = "add_occurrence",
        hasErrors = false;

    return {
       trigger: function() {
           hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
           hasErrors = sanityChecker.checkIfNotExists('#'+ occurrenceCreationId, 'Occurrence creation button') || hasErrors;

           if (hasErrors) {
               logger.error('Script initialization failed due to multiple errors');
           } else {
               logger.debug('Initializing event form ...');
               dateUtils.trigger();
               $(eventTitleInputId).focus();
               $(occurrenceCreationId).observe('click', function(event) {
                   logger.debug('cliclicklclick');
               });
               logger.debug('... done!');
           }
       }
    };
});
