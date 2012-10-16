<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="event.submission.header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/event', 'jqueryui'], function($, event) {
            $(document).ready(function() {
                event.trigger();
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="event.submission.main_header" />
			</h1>
			<div id="catContent">
				<form:form modelAttribute="eventCommand">
                    <h2>Event</h2>

                    <div class="input">
                        <spring:message code="event.submission.highlight.placeholder"
                                        var="highlightPlaceholder" />
                        <form:label path="highlighted" title="${highlightPlaceholder}">
                            <spring:message code="event.submission.highlight.select" />
                        </form:label>
                        <form:checkbox class="checkbox" path="highlighted" value="0" />
                        <form:errors path="highlighted" cssClass="errors" />
                    </div>

					<div class="input">
						<spring:message code="event.submission.title.placeholder"
							var="titlePlaceholder" />
						<form:label path="title" title="${titlePlaceholder}">
							<spring:message code="event.submission.title" />
						</form:label>
						<form:input path="title" placeholder="${titlePlaceholder}" />
						<form:errors path="title" cssClass="errors" />
					</div>
                    
                    <div class="input">
                        <spring:message code="event.submission.description.placeholder"
                            var="descriptionPlaceholder" />
                        <form:label path="description" title="${descriptionPlaceholder}">
                            <spring:message code="event.submission.description" />
                        </form:label>
                        <form:textarea path="description"  cssClass="ckeditor" />
                        <form:errors path="description" cssClass="errors" />
                    </div>
                    
					<h2>Planning</h2>
                    <span id="add_occurrence">Add a new occurrence</span>

					<div id="submit" class="submit">
						<input type="submit" value="<spring:message code="event.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>
</body>
</html>