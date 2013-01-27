/*global Handlebars: true*/
define(["underscore", "ext/handlebars"], function(_) {
    "use strict";

    Handlebars.registerHelper('options', function(items, options) {
        var selectedValues = _.map(items.__selected__, function(item) {return item.value;});
		return _.reduce(items,
		function(prev,current) {
			var selectedString = '';
			if (_.contains(selectedValues, current.value)) {
				selectedString = 'selected="selected"';
			}
			return prev + "<option value='" + current.value + "' "+selectedString+">" + options.fn(current) + "</option>"; 
		},"");
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