<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title><spring:message code="slide.submission.main_header" /></title>
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />"	type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />"	type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/cast.css" />"	type="text/css" />
	
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
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/slideAdmin.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/externalLinkDetector.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/common.js" />"></script>
	
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/less.js"/>"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="slide.submission.main_header" />
			</h1>

			<div id="catContent">
				<form:form modelAttribute="gondolaSlideCommand">
					<div class="input">
						<spring:message code="slide.submission.title.placeholder"
							var="titlePlaceholder" />
						<form:label path="title" title="${titlePlaceholder}">
							<spring:message code="slide.submission.title" />
						</form:label>
						<form:input path="title" placeholder="${titlePlaceholder}" />
						<form:errors path="title" cssClass="errors" />
					</div>
					
					<div class="input">
						<spring:message code="slide.submission.tagLine.placeholder"
							var="tagLinePlaceholder" />
						<form:label path="tagLine" title="${tagLinePlaceholder}">
							<spring:message code="slide.submission.tagLine" />
						</form:label>
						<form:input path="tagLine" placeholder="${tagLinePlaceholder}" />
						<form:errors path="tagLine" cssClass="errors" />
					</div>
					
					<div class="input">
						<spring:message code="slide.submission.date.placeholder"
							var="datePlaceholder" />
						<form:label path="date"
							title="${datePlaceholder}">
							<spring:message code="slide.submission.date.placeholder" />
						</form:label>
						
                        <fmt:formatDate value='${gondolaSlideCommand.date}' pattern='dd/MM/yyyy HH:mm' var="formattedDate"/>
						<input type="text" id="date" name="date" class="datepicker" placeholder="<c:out value="${datePlaceholder}" />" value="<c:out value="${formattedDate}" />"/>
						<form:errors path="date" cssClass="errors" />
					</div>

					<div class="input">
						<spring:message code="slide.submission.link.placeholder"
							var="linkPlaceholder" />
						<form:label path="link" title="${linkPlaceholder}">
							<spring:message code="slide.submission.link" />
						</form:label>
						<form:input path="link" placeholder="${linkPlaceholder}" />
						<form:errors path="link" cssClass="errors" />
					</div>
					
					<div class="input">
						<spring:message code="slide.submission.picture.placeholder"
							var="picturePlaceholder" />
						<form:label path="picture" title="${picturePlaceholder}">
							<spring:message code="slide.submission.picture" />
						</form:label>
						<form:input path="picture" placeholder="${picturePlaceholder}" />
						<form:errors path="picture" cssClass="errors" />
					</div>
					
					<div class="input">
						<form:label path="language">
							<spring:message code="slide.submission.language" />
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
						<spring:message code="slide.submission.prize.placeholder"
							var="prizePlaceholder" />
						<form:label path="prize" title="${prizePlaceholder}">
							<spring:message code="slide.submission.prize" />
						</form:label>
						<form:input path="prize" placeholder="${prizePlaceholder}" />
						<form:errors path="prize" cssClass="errors" />
					</div>

                    <div class="input">
                        <form:label path="description">
                            <spring:message code="slide.submission.description" />
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