define([], function() {
    return {
        splitOnFirst : function(string, delimiter) {
            var atoms, result;
            if (string === null || string === undefined) {
                return [];
            }
            if (delimiter === null || delimiter === undefined || delimiter === '') {
                return [string];
            }
            atoms = string.split(delimiter);
            result = atoms.slice(0,1);
            result.push(atoms.slice(1).join(delimiter));
            return result;
        },
        isEmpty: function(string) {
            return (string === undefined || string === null || string.length === 0);
        }
    };
});