"use strict";
var Event = Class.create({
   occurrenceTemplate:
"<div class='occurrence'>" +
"    <div class='input'>" +
"        <form:label path='startDates.${loopContext.index}'>Start</form:label>" +
"        <form:input path="startDates.${loopContext.index}" cssClass="datepicker" />" +
"        <form:errors path="startDates.${loopContext.index}" cssClass="errors" />" +
"    </div>" +
"    <div class="input">" +
"        <form:label path="endDates.${loopContext.index}">End (optional)</form:label>" +
"        <form:input path="endDates.${loopContext.index}" cssClass="datepicker" />" +
"        <form:errors path="endDates.${loopContext.index}" cssClass="errors" />" +
"    </div>" +
"    <div class="input">" +
"        <form:label path="frequencies.${loopContext.index}">Frequency</form:label>" +
"        <form:select path="frequencies.${loopContext.index}">" +
"            <form:option value="">" +
"           select a frequency" +
"            </form:option>" +
"            <form:options items="${frequencies}" />" +
"        </form:select>" +
"    </div>" +
"</div>",
   initialize: function() {
       var eventTitleInputId = "title";
       var occurrenceCreationId = "add_occurrence";
       var controllerName = 'Event';
       var eventLogger = new Logger(controllerName);
       var sanityChecker = new SanityChecker(controllerName);

       var hasErrors = false;
       hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
       hasErrors = sanityChecker.checkIfNotExists('#'+ occurrenceCreationId, 'Occurrence creation button') || hasErrors;

       if (hasErrors) {
           eventLogger.error('Script initialization failed due to multiple errors');
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
                  console.log('cliclicklclick')
           });
       };
   }
});


document.observe("dom:loaded",function() {
    new Event();
});
