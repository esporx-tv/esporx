<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
	<title><spring:message code="cast.submission.main_header" /></title>
	
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/cast.css" />" type="text/css" />

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
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/cast.js" />"></script>
    
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
				<spring:message code="cast.submission.main_header" />
			</h1>

			<div id="catContent">
				<form:form modelAttribute="castCommand">
				    <c:if test="${not empty persistenceError}">
				        <div id="submitError">
				            An error occurred while trying to save:
				            <code><c:out value="${persistenceError}" /></code>
				        </div>
				    </c:if>
                    
                    <div class="input">
                        <spring:message code="cast.submission.event.placeholder"
                            var="eventPlaceholder" />
                        <form:label path="title" title="${eventPlaceholder}">
                            <spring:message code="cast.submission.event" />
                        </form:label>
                        <form:select path="event">
                            <form:option value=""></form:option>
                            <form:options items="${events}" itemValue="id" itemLabel="title" />
                        </form:select>
                        <form:errors path="event" cssClass="errors" />
                    </div>
				    
					<div class="input">
						<spring:message code="cast.submission.title.placeholder"
							var="titlePlaceholder" />
						<form:label path="title" title="${titlePlaceholder}">
							<spring:message code="cast.submission.title" />
						</form:label>
						<form:input path="title" placeholder="${titlePlaceholder}" />
						<form:errors path="title" cssClass="errors" />
					</div>

                    <div class="input">
                        <form:label path="language">
                            <spring:message code="cast.submission.language" />
                        </form:label>
                        <form:select path="language">
                            <form:option value="">
                                <spring:message code="cast.submission.language.options.choose" />
                            </form:option>
                            <form:options items="${allowedLocales}" />
                        </form:select>
                        <form:errors path="language" cssClass="errors" />
                    </div>

					<div class="input">
						<spring:message code="cast.submission.url.placeholder"
							var="urlPlaceholder" />
						<form:label path="videoUrl" title="${urlPlaceholder}">
							<spring:message code="cast.submission.url" />
						</form:label>
						<form:input path="videoUrl" placeholder="${urlPlaceholder}" />
						<form:errors path="videoUrl" cssClass="errors" />
					</div>

					<div class="input">
						<spring:message code="cast.submission.broadcastDate.placeholder"
							var="broadcastDatePlaceholder" />
						<form:label path="broadcastDate" title="${broadcastDatePlaceholder}">
							<spring:message code="cast.submission.broadcastDate" />
						</form:label>
						
						<fmt:formatDate value='${castCommand.broadcastDate}' pattern='dd/MM/yyyy HH:mm' var="formattedDate"/>
						<input type="text" id="broadcastDate" name="broadcastDate" class="datepicker" placeholder="<c:out value="${broadcastDatePlaceholder}" />" value="<c:out value="${formattedDate}" />"/>
						<form:errors path="broadcastDate" cssClass="errors" />
					</div>
					
					<div class="input">
						<form:label path="description">
							<spring:message code="cast.submission.description" />
						</form:label>
						<form:textarea path="description" cssClass="ckeditor" />
						<form:errors path="description" cssClass="errors" />
					</div>

					<div class="submit">
						<input type="submit" value="<spring:message code="cast.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>