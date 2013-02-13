<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="event.page.title"
                           arguments="${eventCommand.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/event'], function($, event) {
            $(document).ready(function() {
                event.trigger();
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
                        <spring:message code="event.submission.twitter_id.placeholder"
                                        var="twitterIdPlaceholder" />
                        <form:label path="twitterId" title="${twitterIdPlaceholder}">
                            <spring:message code="event.submission.twitter_id" />
                        </form:label>
                        <form:input path="twitterId" placeholder="${twitterIdPlaceholder}" />
                        <form:errors path="twitterId" cssClass="errors" />
                    </div>

                    <div class="input">
                        <spring:message code="event.submission.twitter_hashtag.placeholder"
                                        var="twitterHashtagPlaceholder" />
                        <form:label path="twitterHashtags" title="${twitterHashtagPlaceholder}">
                            <spring:message code="event.submission.twitter_hashtag" />
                        </form:label>
                        <form:input path="twitterHashtags" placeholder="${twitterHashtagPlaceholder}" />
                        <form:errors path="twitterHashtags" cssClass="errors" />
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

					<div id="submit" class="submit">
						<input type="submit" value="<spring:message code="event.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>