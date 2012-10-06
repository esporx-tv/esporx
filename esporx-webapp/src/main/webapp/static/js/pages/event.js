define(["lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!tpl/eventForm.tpl", "lib/dateUtils", "ext/ckeditor/ckeditor_basic"], function(logger, sanityChecker, templateHelper, eventTemplate, dateUtils) {
    "use strict";

    var eventTitleInputId = "title";
    var occurrenceCreationId = "add_occurrence";

    return {
       trigger: function() {
           logger.setCaller('Event');
           var hasErrors = false;
           hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
           hasErrors = sanityChecker.checkIfNotExists('#'+ occurrenceCreationId, 'Occurrence creation button') || hasErrors;

           if (hasErrors) {
               logger.error('Script initialization failed due to multiple errors');
           } else {
               $(eventTitleInputId).focus();
               dateUtils.trigger();
               $(occurrenceCreationId).observe('click', function(event) {
                   logger.debug('cliclicklclick')
               });
           };
       }
    }
});
