<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title><spring:message code="event.submission.header" /></title>
	
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />"	type="text/css" />
	
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype-date-extensions.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/datepicker.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/scriptaculous.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/effects.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/sanityChecker.js" />"></script>
	<script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/event.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
	
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
				<form name="linkForm" action="/event/link" method="POST">
					<select name="cast" >
					<c:forEach var="cast" items="${casts}">
							<option value="<c:out value="${cast.id}" />"><c:out value="${cast.title}" /></option>
					</c:forEach>
					</select>
					<input type="hidden" value="<c:out value="${event.id}" />" name="relatedEvent" />
					<input name="submit" type="submit"	value="submit" />
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>
					
