define(["lib/urlParser"], function(urlParser) {
    describe("URL parser", function() {

        beforeEach(function() {
            this.addMatchers({
                toHaveOwnProperty: function(property) {
                    return this.actual.hasOwnProperty(property);
                }
            });
        });

        it("should return an empty object when query string is undefined", function() {
            expect(urlParser.parse()).toEqual({});
        });

        it("should return an empty object when query string is null", function() {
            expect(urlParser.parse(null)).toEqual({});
        });

        it("should return an empty object when query string is empty", function() {
            expect(urlParser.parse("   ")).toEqual({});
        });

        it("should return an empty object when query string only has ?", function() {
            expect(urlParser.parse("?")).toEqual({});
        });

        it("should return an empty property when query string has no param value", function() {
            var result = urlParser.parse("property");
            expect(result).toHaveOwnProperty("property");
            expect(result.property).toEqual('');
        });

        it("should return a property matching the query string", function() {
            var result = urlParser.parse("property=toto");
            expect(result).toHaveOwnProperty("property");
            expect(result.property).toEqual("toto");
        });

        it("should return property with value as array when occurring twice in the query string", function() {
            var result = urlParser.parse("property=toto&property=titi");
            expect(result).toHaveOwnProperty("property");
            expect(result.property).toEqual(["toto", "titi"]);
        });

        it("should return property with value as array when occurring several times in the query string", function() {
            var result = urlParser.parse("property=toto&property=titi&property=toto");
            expect(result).toHaveOwnProperty("property");
            expect(result.property).toEqual(["toto", "titi", "toto"]);
        });

        it("should return properties matching the query string", function() {
            var result = urlParser.parse("property=toto&property2=titi");
            expect(result).toHaveOwnProperty("property");
            expect(result.property).toEqual("toto");
            expect(result).toHaveOwnProperty("property2");
            expect(result.property2).toEqual("titi");
        });
    });
});

