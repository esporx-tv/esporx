var GondolaSlideAdmin = Class.create({
	initialize: function() {
		var controllerName = 'GondolaSlideAdmin';
		var eventLogger = new Logger(controllerName);
		var sanityChecker = new SanityChecker(controllerName);
		$$('input.datepicker').each(function(e) {
			new Control.DatePicker(e, {
				'datePicker' : true,
				'timePicker' : true,
				'timePickerAdjacent' : true,
				'icon' : '/img/calendar.png',
				'iconBgColor' : '#E6E6E6',
				'use24hrs' : true,
				'dateTimeFormat' : 'dd/MM/yyyy HH:mm'
			});
			e.writeAttribute('autocomplete', 'off');
		});
	}
});


document.observe("dom:loaded",function() {
	new GondolaSlideAdmin();
});