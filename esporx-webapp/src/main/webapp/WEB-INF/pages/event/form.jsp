<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title><spring:message code="event.submission.header" /></title>
	
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

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
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/event.js" />"></script>
	
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
    <script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="event.submission.main_header" />
			</h1>
			<div id="catContent">
				<form:form modelAttribute="eventCommand">
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
						<spring:message code="event.submission.startDate.placeholder"
							var="startDatePlaceholder" />
						<form:label path="startDate" title="${startDatePlaceholder}">
							<spring:message code="event.submission.startDate" />
						</form:label>
						<fmt:formatDate value='${eventCommand.startDate}' pattern='dd/MM/yyyy HH:mm' var="formattedDate"/>
						<input type="text" id="startDate" name="startDate" class="datepicker" placeholder="<c:out value="${startDatePlaceholder}" />" value="<c:out value="${formattedDate}" />"/>
						<form:errors path="startDate" cssClass="errors" />
					</div>

					<div class="input">
						<spring:message code="event.submission.endDate.placeholder"
							var="endDatePlaceholder" />
						<form:label path="endDate" title="${endDatePlaceholder}">
							<spring:message code="event.submission.endDate" />
						</form:label>
					<fmt:formatDate value='${eventCommand.endDate}' pattern='dd/MM/yyyy HH:mm' var="formattedDate"/>
						<input type="text" id="endDate" name="endDate" class="datepicker" placeholder="<c:out value="${endDatePlaceholder}" />" value="<c:out value="${formattedDate}" />"/>
						<form:errors path="endDate" cssClass="errors" />
					</div>
					
					
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
                        <spring:message code="event.submission.description.placeholder"
                            var="descriptionPlaceholder" />
                        <form:label path="description" title="${descriptionPlaceholder}">
                            <spring:message code="event.submission.description" />
                        </form:label>
                        <form:textarea path="description"  cssClass="ckeditor" />
                        <form:errors path="description" cssClass="errors" />
                    </div>
                    
					
					<div class="submit">
						<input type="submit"
							value="<spring:message code="event.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>