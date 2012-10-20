<fieldset class='occurrence' id="occurrence_{{loop}}">
    <div class="close"><img src="{{icon}}" /></div>
    <legend>Occurrence n°<span class="loop">{{#plusOne loop}}{{/plusOne}}</span></legend>
    <div class='input'>
        <label for='startDates_{{loop}}'>Start</label>
        <input id="occurrence_{{loop}}_ID" class="eventId" hidden />
        <input id="startDates_{{loop}}" name="startDates_{{loop}}[]" class="datepicker startDate" />
        <!--<errors path="startDates.{{loop}}" class="errors" />-->
    </div>
    <div class="input">
        <label for="endDates_{{loop}}">End (optional)</label>
        <input id="endDates_{{loop}}" name="endDates_{{loop}}[]" class="datepicker endDate" />
        <!--<errors path="endDates.{{loop}}" class="errors" />-->
    </div>
    <div class="input">
        <label for="frequencies_{{loop}}">Frequency</label>
        <select id="frequencies_{{loop}}" name="frequencies_{{loop}}[]" class="frequency">
            <option value="">
           select a frequency
            </option>
            {{#options frequencies}}{{value}}{{/options}}
        </select>
    </div>
</fieldset>