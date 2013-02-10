define(["lib/stringUtils"], function(stringUtils) {
    describe("String utilities", function() {

        it("should return an empty array when string is undefined", function() {
            expect(stringUtils.splitOnFirst()).toEqual([]);
        });

        it("should return an empty array when string is null", function() {
            expect(stringUtils.splitOnFirst(null)).toEqual([]);
        });

        it("should return an array with the entire string when delimiter is undefined", function() {
            expect(stringUtils.splitOnFirst("toto")).toEqual(["toto"]);
        });

        it("should return an array with the entire string when delimiter is null", function() {
            expect(stringUtils.splitOnFirst("toto", null)).toEqual(["toto"]);
        });

        it("should return an array with the entire string when delimiter is empty", function() {
            expect(stringUtils.splitOnFirst("toto", '')).toEqual(["toto"]);
        });

        it("should return an array with 2 simple atoms", function() {
            expect(stringUtils.splitOnFirst("toto=titi", '=')).toEqual(["toto", "titi"]);
        });

        it("should return an array with 2 atoms even when delimiters occurs more than once", function() {
            expect(stringUtils.splitOnFirst("toto=tit=i", '=')).toEqual(["toto", "tit=i"]);
        });
    });
});

