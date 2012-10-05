define(["lib/logger", "lib/sanityChecker", "lib/handlebarsHelper", "text!pages/eventForm.tpl", "lib/dateUtils", "ext/ckeditor/ckeditor_basic"], function(logger, sanityChecker, templateHelper, eventTemplate) {
    "use strict";
    logger.setCaller('Event');

    var eventTitleInputId = "title";
    var occurrenceCreationId = "add_occurrence";

    return {
       trigger: function() {
           var hasErrors = false;
           hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
           hasErrors = sanityChecker.checkIfNotExists('#'+ occurrenceCreationId, 'Occurrence creation button') || hasErrors;

           if (hasErrors) {
               logger.error('Script initialization failed due to multiple errors');
           } else {
               $(eventTitleInputId).focus();

               $$('input.datepicker').each(function(e) {
                   new Control.DatePicker(e, {
                       'datePicker' : true,
                       'timePicker' : true,
                       'timePickerAdjacent' : true,
                       'icon' : '/static/img/calendar.png',
                       'iconBgColor' : '#E6E6E6',
                       'use24hrs' : true,
                       'dateTimeFormat' : 'dd/MM/yyyy HH:mm'
                   });
                   e.writeAttribute('autocomplete', 'off');
               });

               $(occurrenceCreationId).observe('click', function(event) {
                   logger.debug('cliclicklclick')
               });
           };
       }
    }
});
