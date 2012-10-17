/*global Handlebars: true*/
define(["ext/handlebars"], function() {
    "use strict";

    Handlebars.registerHelper('options', function(items, options) {
        var out = '', i, l, item;
        for (i = 0, l = items.length; i < l; i++) {
            item = items[i];
            out += "<option value='" + item.value + "'>" + options.fn(item) + "</li>";
        }
        return out;
    });

    Handlebars.registerHelper('plusOne', function(item, options) {
        return 1 + parseInt(item, 10);
    });

    return {
        template: function(contents, object) {
            var templator = Handlebars.compile(contents);
            return templator(object);
        }
    };
});