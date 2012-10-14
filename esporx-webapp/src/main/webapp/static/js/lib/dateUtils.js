/*global Control: true
$$: true*/
define(["ext/datepicker"], function() {
    "use strict";

    var datePickerize = function() {
        $$('input.datepicker').each(function(input) {
            var datepicker = new Control.DatePicker(input, {
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
    };

    return {
        trigger: function() {
            datePickerize();
        }
    };
});