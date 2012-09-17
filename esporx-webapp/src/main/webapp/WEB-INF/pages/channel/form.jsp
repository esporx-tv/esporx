<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
	<title><spring:message code="channel.submission.main_header" /></title>
	
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/channel.css" />" type="text/css" />

    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype-date-extensions.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/datepicker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/scriptaculous.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/effects.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/sanityChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/ckeditor/ckeditor_basic.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/domNavigationUtils.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/externalLinkDetector.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/common.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/channel.js" />"></script>
    
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="feedbackArea">
		<img id="loadingIcon" style="display: none;"
			src="<c:url value="${staticRoot}/img/loading.gif" />" title="" alt="loading..."	height="32" width="32" />
	</div>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
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
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>