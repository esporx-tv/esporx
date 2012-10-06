<div class='occurrence'>
    <div class='input'>
        <form:label path='startDates.{{loop}}'>Start</form:label>
        <form:input path="startDates.{{loop}}" cssClass="datepicker" />
        <form:errors path="startDates.{{loop}}" cssClass="errors" />
    </div>
    <div class="input">
        <form:label path="endDates.{{loop}}">End (optional)</form:label>
        <form:input path="endDates.{{loop}}" cssClass="datepicker" />
        <form:errors path="endDates.{{loop}}" cssClass="errors" />
    </div>
    <div class="input">
        <form:label path="frequencies.{{loop}}">Frequency</form:label>
        <form:select path="frequencies.{{loop}}">
            <form:option value="">
           select a frequency
            </form:option>
            <form:options items="{{frequencies}}" />
        </form:select>
    </div>
</div>