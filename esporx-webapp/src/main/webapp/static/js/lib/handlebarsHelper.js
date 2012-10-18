/*global Handlebars: true*/
define(["underscore", "ext/handlebars"], function(_) {
    "use strict";

    Handlebars.registerHelper('options', function(items, options) {
        return _.reduce(items,function(prev,current) { return prev + "<option value='" + current.value + "'>" + options.fn(current) + "</option>"; },"");
    });

    Handlebars.registerHelper('plusOne', function(item) {
        return 1 + parseInt(item, 10);
    });

    return {
        template: function(contents, object) {
            var templator = Handlebars.compile(contents);
            return templator(object);
        }
    };
});