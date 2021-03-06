<%@ include file="../include/commonTaglib.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="slot.submission.main_header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery'], function($) {
            $(document).ready(function() {
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="slot.submission.main_header" />
			</h1>

			<div id="catContent">
				<form:form modelAttribute="configurableSlotCommand">
					<div class="input">
						<spring:message code="slot.submission.boxTitle.placeholder"
							var="boxTitlePlaceholder" />
						<form:label path="boxTitle" title="${boxTitlePlaceholder}">
							<spring:message code="slot.submission.boxTitle" />
						</form:label>
						<form:input path="boxTitle" placeholder="${boxTitlePlaceholder}" />
						<form:errors path="boxTitle" cssClass="errors" />
					</div>
				
				
					<div class="input">
						<spring:message code="slot.submission.title.placeholder"
							var="titlePlaceholder" />
						<form:label path="title" title="${titlePlaceholder}">
							<spring:message code="slot.submission.title" />
						</form:label>
						<form:input path="title" placeholder="${titlePlaceholder}" />
						<form:errors path="title" cssClass="errors" />
					</div>
					
					
					<div class="input">
						<spring:message code="slot.submission.link.placeholder"
							var="linkPlaceholder" />
						<form:label path="link" title="${linkPlaceholder}">
							<spring:message code="slot.submission.link" />
						</form:label>
						<form:input path="link" placeholder="${linkPlaceholder}" />
						<form:errors path="link" cssClass="errors" />
					</div>
					
					<div class="input">
						<spring:message code="slot.submission.picture.placeholder"
							var="picturePlaceholder" />
						<form:label path="picture" title="${picturePlaceholder}">
							<spring:message code="slot.submission.picture" />
						</form:label>
						<form:input path="picture" placeholder="${picturePlaceholder}" />
						<form:errors path="picture" cssClass="errors" />
					</div>
					
					<div class="input">
                        <form:label path="language">
                            <spring:message code="slot.submission.language" />
                        </form:label>
                        <form:select path="language">
                            <form:option value="">
                                <spring:message code="slot.submission.language.options.choose" />
                            </form:option>
                            <form:options items="${allowedLocales}" />
                        </form:select>
                        <form:errors path="language" cssClass="errors" />
                    </div>
					
					<div class="input">
                        <spring:message code="slot.submission.active.placeholder"
                            var="activePlaceholder" />
                        <form:label path="active" title="${activePlaceholder}">
                            <spring:message code="slot.submission.active.select" />
                        </form:label>
                        <form:checkbox class="checkbox" path="active" value="0" />
                        <form:errors path="active" cssClass="errors" />
                    </div>

                    <div class="input">
                        <form:label path="double">
                            <spring:message code="slot.submission.width.select" />
                        </form:label>
                        <form:checkbox class="checkbox" path="double" value="0" />
                        <form:errors path="double" cssClass="errors" />
                    </div>

                    <div class="input">
                        <form:label path="description">
                            <spring:message code="slot.submission.description" />
                        </form:label>
                        <form:textarea path="description" cssClass="ckeditor" />
                        <form:errors path="description" cssClass="errors" />
                    </div>
					
					<div class="submit">
						<input type="submit"
							value="<spring:message code="channel.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>