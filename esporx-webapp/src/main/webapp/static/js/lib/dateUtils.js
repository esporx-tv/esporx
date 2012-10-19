/*global Control: true
  console: true*/
define(["jquery", "text!css/jquery-ui.css", "text!css/datepicker.css", "jqueryui", "ext/jquery-timepicker-addon"], function($, baseCss, timeCss) {
    "use strict";

    var replaceImgPaths = function(css, pathPattern, pathReplacement) {
            return css.replace(pathPattern, pathReplacement);
        },
        observeOne = function(inputId) {
            var input = $(inputId);
            input.datetimepicker({
                showButtonPanel : true,
                showWeek: true,
                dateFormat : 'dd/mm/yy',
                timeFormat : 'hh:mm',
                hourGrid: 4,
                minuteGrid: 10,
                ampm: false
            });
            input.addClass('hasDatepicker');
            input.attr('autocomplete', 'off');
            input.css({'cursor':'pointer'});
        };

    return {
        'init': function() {
            var processedCss = replaceImgPaths(baseCss, /url\(images\//g, 'url(' + $.withBaseImgUrl(''));
            $('head').append('<style>'+ processedCss + '</style>');
            $('head').append('<style>'+ timeCss + '</style>');
        },
        'observeAll': function() {
            $('input.datepicker').not('.hasDatepicker').each(function(index) {
                observeOne($(this));
            });
        }
    };
});