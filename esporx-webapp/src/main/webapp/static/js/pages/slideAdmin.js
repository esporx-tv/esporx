define(["lib/logger", "lib/dateUtils"], function(logger) {
    "use strict";
    logger.setCaller('GondolaSlideAdmin');

    return {
        trigger : function() {
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
        }
    }
});