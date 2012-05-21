/*
 * everything is wrapped in a self-invoked anonymous function to limit variables scope
 */
(function () {  
	var eventTitleInputId = "title";
	
	document.observe("dom:loaded",function() {
		var controllerName = 'Event';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		
		var hasErrors = false;
		hasErrors = sanityChecker.checkIfNotExists('#'+ eventTitleInputId, 'Event title input') || hasErrors;
		
		if (hasErrors) {
			eventLogger.error('Script initialization failed due to multiple errors');
		} else {
			$(eventTitleInputId).focus();
			
			$$('input.datepicker').each(function(e) {
				new Control.DatePicker(e, {
					'datePicker' : true,
					'timePicker' : true,
					'timePickerAdjacent' : true,
					'icon' : '/static/img/calendar.png',
					'iconBgColor' : '#E6E6E6',
					'use24hrs' : true,
					'dateTimeFormat' : 'dd/MM/yyyy HH:mm'
				});
				e.writeAttribute('autocomplete', 'off');
			});
		};
	});
}());