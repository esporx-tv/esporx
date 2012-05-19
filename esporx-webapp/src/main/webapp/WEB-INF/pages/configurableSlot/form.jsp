<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="slot.submission.main_header" /></title>

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
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/slot.js" />"></script>
	
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="slot.submission.main_header" />
			</h1>

			<div id="catContent">
				<form:form modelAttribute="configurableSlotCommand">
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
						<form:label path="position">
							<spring:message code="slot.submission.position" />
						</form:label>
						<form:select path="position">
							<form:option value="1">
								<spring:message code="slot.submission.position.options.topLeft" />
							</form:option>
							<form:option value="2">
								<spring:message code="slot.submission.position.options.topRight" />
							</form:option>
							<form:option value="3">
								<spring:message code="slot.submission.position.options.bottom" />
							</form:option>
						</form:select>
						<form:errors path="position" cssClass="errors" />
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
                        <form:label path="description">
                            <spring:message code="slot.submission.description" />
                        </form:label>
                        <form:textarea path="description" cssClass="ckeditor" />
                        <form:errors path="description" cssClass="errors" />
                    </div>
					
					<div class="submit">
						<input type="submit"
							value="<spring:message code="cast.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>