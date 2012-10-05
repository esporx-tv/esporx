define(["lib/logger", "lib/sanityChecker", "lib/dateUtils"], function(logger, sanityChecker) {
    "use strict";
    logger.setCaller('Admin');

    //TODO: this should be configurable
    var slideContainerId = 'showSlides';
    var slotContainerId = 'showSlots';
    var channelContainerId = 'showChannels';
    var eventContainerId = 'showEvents';


    var display = function(containerToDisplay) {
        var arrayContainer = new Array($('slidesContainer'), $('slotsContainer'), $('channelsContainer'), $('eventsContainer'));
        arrayContainer.each(function(container) {
            if (container == containerToDisplay) {
                if (containerToDisplay.hasClassName('displayNone')) containerToDisplay.removeClassName('displayNone');
            } else {
                if (!container.hasClassName('displayNone')) container.addClassName('displayNone');
            }
        });
    };

    var active = function(linkToMakeActive) {
        var linkContainer = new Array($('showSlides'), $('showSlots'), $('showChannels'), $('showEvents'));
        linkContainer.each(function(link) {
            if (linkToMakeActive == link) {
                if (!linkToMakeActive.hasClassName('containerSelected'))  {
                    linkToMakeActive.removeClassName('adminName');
                    linkToMakeActive.addClassName('containerSelected');
                }
            } else {
                if (link.hasClassName('containerSelected')) {
                    link.removeClassName('containerSelected');
                    link.addClassName('adminName');
                }
            }
        });
    };
    
    return {
        trigger: function() {
            var hasErrors = false;
            hasErrors = sanityChecker.checkIfNotExists('#' + slideContainerId, 'Slide container input')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + slotContainerId, 'Slot container input')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + channelContainerId, 'Channel container input')|| hasErrors;
            hasErrors = sanityChecker.checkIfNotExists('#' + eventContainerId, 'Event container input')|| hasErrors;
            if (hasErrors) {
                logger.error('Script initialization failed due to multiple errors');
            }
            else {
                logger.debug('Initialize admin js');
                $(slideContainerId).observe('click', function () {
                    display($('slidesContainer'));
                    active($(slideContainerId));
                });
                $(slotContainerId).observe('click', function() {
                    display($('slotsContainer'));
                    active($(slotContainerId));
                });
                $(channelContainerId).observe('click', function() {
                    display($('channelsContainer'));
                    active($(channelContainerId));
                });
                $(eventContainerId).observe('click', function() {
                    display($('eventsContainer'));
                    active($(eventContainerId));
                });

                $$('.gondolaRemove').each(function(element) {
                    element.observe('click', function(event) {
                        if(!window.confirm('Do you really want to delete this slide?')) {
                            Event.stop(event);
                        }
                    });
                });

                $$('.send').each(function(element) {
                    element.observe('click', function(event) {
                        if(!window.confirm('Confirm deletion?')) {
                            Event.stop(event);
                        }
                    });
                });
            }

        }
    }
});