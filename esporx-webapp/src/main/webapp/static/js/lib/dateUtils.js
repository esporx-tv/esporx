/*global Control: true
  console: true*/
define(["jquery", "text!css/jquery-ui.css", "text!css/datepicker.css", "jqueryui", "ext/jquery-timepicker-addon"], function($, baseCss, timeCss) {
    "use strict";

    var replaceImgPaths = function(css, pathPattern, pathReplacement) {
        return css.replace(pathPattern, pathReplacement);
    };

    return {
        trigger: function() {
            var processedCss = replaceImgPaths(baseCss, /url\(images\//g, 'url(' + $.withBaseImgUrl(''));
            $('head').append('<style>'+ processedCss + '</style>');
            $('head').append('<style>'+ timeCss + '</style>');

            $('input.datepicker').each(function(index) {
                $(this).datetimepicker({
                    showButtonPanel : true,
                    showWeek: true,
                    dateFormat : 'dd/mm/yy',
                    timeFormat : 'hh:mm',
                    hourGrid: 4,
                    minuteGrid: 10,
                    ampm: true
                });
                $(this).attr('autocomplete', 'off');
                $(this).css({'cursor':'pointer'});
            });
        }
    };
});