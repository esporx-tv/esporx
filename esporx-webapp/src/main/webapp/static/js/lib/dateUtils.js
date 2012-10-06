define(["ext/prototype", "ext/scriptaculous", "ext/datepicker", "ext/prototype-date-extensions", "ext/effects"], function() {
    "use strict";

    var datePickerize = function() {
        $$('input.datepicker').each(function(input) {
            new Control.DatePicker(input, {
                'datePicker' : true,
                'timePicker' : true,
                'timePickerAdjacent' : true,
                'icon' : '/static/img/calendar.png',
                'iconBgColor' : '#E6E6E6',
                'use24hrs' : true,
                'dateTimeFormat' : 'dd/MM/yyyy HH:mm'
            });
            input.writeAttribute('autocomplete', 'off');
            input.setStyle({'cursor':'pointer'});
        });
    }

    return {
        trigger: function() {
            datePickerize();
        }
    }
});