<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="channel.page.title"
                           arguments="${channelCommand.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/channel.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/channel'], function($, channel) {
            $(document).ready(function() {
                channel.trigger();
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="feedbackArea"><%-- TODO: this should be done via a script, this pollutes markup --%>
		<img id="loadingIcon" style="display: none;" src="<c:url value="${staticRoot}/img/loading.gif" />" title="" alt="loading..." height="32" width="32" />
	</div>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="channel.submission.main_header" />
			</h1>

			<div id="catContent">
				<form:form modelAttribute="channelCommand">
				    <c:if test="${not empty persistenceError}">
				        <div id="submitError">
				            An error occurred while trying to save:
				            <code><c:out value="${persistenceError}" /></code>
				        </div>
				    </c:if>
                    
					<div class="input">
						<spring:message code="channel.submission.title.placeholder"
							var="titlePlaceholder" />
						<form:label path="title" title="${titlePlaceholder}">
							<spring:message code="channel.submission.title" />
						</form:label>
						<form:input path="title" placeholder="${titlePlaceholder}" />
						<form:errors path="title" cssClass="errors" />
					</div>

                    <div class="input">
                        <form:label path="language">
                            <spring:message code="channel.submission.language" />
                        </form:label>
                        <form:select path="language">
                            <form:option value="">
                                <spring:message code="channel.submission.language.options.choose" />
                            </form:option>
                            <form:options items="${allowedLocales}" />
                        </form:select>
                        <form:errors path="language" cssClass="errors" />
                    </div>

					<div class="input">
						<spring:message code="channel.submission.url.placeholder"
							var="urlPlaceholder" />
						<form:label path="videoUrl" title="${urlPlaceholder}">
							<spring:message code="channel.submission.url" />
						</form:label>
						<form:input path="videoUrl" placeholder="${urlPlaceholder}" />
                        <form:hidden path="videoProvider" id="provider" />
						<form:errors path="videoUrl" cssClass="errors" />
					</div>

                    <div class="input">
                        <spring:message code="channel.submission.hashtags.placeholder"
                                        var="hashtagsPlaceholder" />
                        <form:label path="twitterHashtags" title="${hashtagsPlaceholder}">
                            <spring:message code="channel.submission.hashtags" />
                        </form:label>
                        <form:input path="twitterHashtags" placeholder="${hashtagsPlaceholder}" />
                        <form:errors path="twitterHashtags" cssClass="errors" />
                    </div>

					<div class="input">
						<form:label path="description">
							<spring:message code="channel.submission.description" />
						</form:label>
						<form:textarea path="description" cssClass="ckeditor" />
						<form:errors path="description" cssClass="errors" />
					</div>

					<div class="submit">
						<input type="submit" id="submitChannel" value="<spring:message code="channel.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>