/*global gridster:true
noty:true */
define(["jquery", "lib/logger", "underscore", "ext/gridster", "lib/notyHelper"], function($, logger, _) {
    "use strict";

    return {
        generateGrid : function(containerId) {
            $(containerId).gridster({
                widget_margins: [10, 10],
                widget_base_dimensions: [140, 140],
                serialize_params: function($w, wgd) {
                    return {
                        id: $w.data("id"),
                        size_x : wgd.size_x,
                        size_y : wgd.size_y,
                        col: wgd.col,
                        row: wgd.row
                    };
                }
            }).data('gridster');
        },

        saveGrid : function(containerId) {
            var boxes,
                boxesGroupedByRow,
                maxWidth = 2,
                error = [];
            boxes = $(containerId).gridster().data("gridster").serialize();
            boxesGroupedByRow = _.groupBy(boxes, "row");
            logger.debug(boxesGroupedByRow);
            _.each(boxesGroupedByRow, function(el, key) {
                if (key < _.keys(boxesGroupedByRow).length) {
                    if (_.reduce(el, function(memo, box) {
                        return memo + box.size_x;
                    }, 0) !== maxWidth) {
                        error.push(key);
                    }
                } else {
                    if (_.find(el, function(box) {
                        return box.col === 1;
                    }) === undefined) {
                        error.push(key);
                    }
                }
            });
            if (error.length !== 0) {
                _.each(error, function(rowInError) {
                   $(".gridster li[data-row="+ rowInError +"]").addClass("invalid");
                });
            } else {
                _.each(_.keys(boxesGroupedByRow), function(row) {
                    $(".gridster li[data-row="+ row +"]").removeClass("invalid");
                });
                $.postJSON("/admin/slot/layout", boxes, function(result) {
                    _.each(result, function(el) {
                        noty({text: 'Saved positions for configurable slot with ID :' + el, theme: 'defaultTheme', layout: 'bottomRight'});
                    });
                });
            }
        }
    };

});
