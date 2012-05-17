<%@ include file="../include/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title><spring:message code="admin.submission.main_header" /></title>
	
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
	
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
    <fmt:formatDate value="${event.startDate}" pattern="dd/MM/yyyy HH:mm" var="startDate" />
	<fmt:formatDate value="${event.endDate}" pattern="dd/MM/yyyy HH:mm" var="endDate" />
	
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<c:out value="${event.title}" /><small><c:out value="${startDate} - ${endDate}" /></small>
			</h1>
		</div>
		<p id="eventDescription">
			<c:out value="${event.description}" />
		</p>
        <c:if test="${not empty event.casts}">
            <h2 id="featuring">Featured casts</h2>
            <ul id="casts">
                <c:forEach var="cast" items="${event.casts}" end="3">
                    <li><a href="<c:url value="/cast/watch/${cast.id}" />"><c:out value="${cast.title}" /></a></li>
                </c:forEach>
            </ul>
        </c:if>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>