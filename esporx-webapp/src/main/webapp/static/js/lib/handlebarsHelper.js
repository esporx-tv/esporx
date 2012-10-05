define(["ext/handlebars"], function() {
    "use strict";
    return {
        template: function(contents, object) {
            var templator = Handlebars.compile(contents);
            return templator(object);
        }
    }
});