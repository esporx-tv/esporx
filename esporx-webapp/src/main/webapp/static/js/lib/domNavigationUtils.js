define(["ext/prototype"], function() {
    "use strict";

    return {
        /*
         * returns the first provided tag name matching parent or undefined if none found
         */
        firstAncestorWithTag : function(tag, startElement) {
            return startElement.ancestors().find(function(ancestor) {
                return ancestor.tagName.toUpperCase() === tag.toUpperCase();
            });
        },
        /*
         * returns the first provided class-matching parent (provided element included) or undefined if none found
         */
        firstAncestorWithClass : function(startElement, className) {
            var result;
            if (startElement.hasClassName(className)) {
                result = startElement;
            } else {
                result = startElement.ancestors().find(function(capturedElement) {
                    return capturedElement.hasClassName(className);
                });
            }
            return result;
        },

        firstDescendantWithTag : function(tag, startElement) {
            return startElement.descendants().find(function(descendant) {
                return descendant.tagName.toUpperCase() === tag.toUpperCase();
            });
        }
    };

});