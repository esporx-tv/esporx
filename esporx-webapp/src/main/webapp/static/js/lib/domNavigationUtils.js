define(["jquery"], function($) {
    "use strict";

    return {
        /*
         * returns the first provided tag name matching parent or undefined if none found
         */
        firstAncestorWithTag : function(tag, startElement) {
            return startElement.parents().filter(function(index) {
                return $(this).tagName.toUpperCase() === tag.toUpperCase();
            });
        },
        /*
         * returns the first provided class-matching parent (provided element included) or undefined if none found
         */
        firstAncestorWithClass : function(startElement, className) {
            var result;
            if (startElement.hasClass(className)) {
                result = startElement;
            } else {
                result = startElement.parents().filter(function(index) {
                    return $(this).hasClass(className);
                });
            }
            return result;
        },

        firstDescendantWithTag : function(tag, startElement) {
            return startElement.children().filter(function(index) {
                return $(this).tagName.toUpperCase() === tag.toUpperCase();
            });
        }
    };

});