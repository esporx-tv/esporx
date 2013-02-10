define(['jquery', 'underscore', 'lib/stringUtils'], function($, _, stringUtils) {
    var sanitize = function(queryString) {
            queryString = $.trim(queryString);
            if (queryString === undefined || queryString === null || queryString === '') {
                return '';
            }
            if (queryString.charAt(0) === '?') {
                return queryString.substring(1);
            }
            return queryString;
        },
        asProperty = function(string, previous) {
            var split = stringUtils.splitOnFirst(string, '='),
                key = split[0],
                previousValue = previous[key],
                newValue = split[1];

            if (previousValue !== undefined) {
                if (_.isArray(previousValue)) {
                    previousValue.push(split[1]);
                    newValue = previousValue;
                }
                else {
                    newValue = [previousValue, split[1]];
                }
            }
            previous[key] = newValue;
            return previous;
        };

    return {
        parse: function(queryString) {
            var result = {};
            queryString = sanitize(queryString);
            if (queryString !== '') {
                _.each(queryString.split('&'), function(element) {
                    result = asProperty(element, result);
                });
            }
            return result;
        }
    };
});