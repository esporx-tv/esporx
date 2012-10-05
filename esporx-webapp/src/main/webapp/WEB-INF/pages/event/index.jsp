<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="event.submission.main_header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
</head>
<body>
    <fmt:formatDate value="${event.startDate}" pattern="dd/MM/yyyy HH:mm" var="startDate" />
	<fmt:formatDate value="${event.endDate}" pattern="dd/MM/yyyy HH:mm" var="endDate" />
	
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<c:out value="${event.title}" /><small><c:out value="${startDate} - ${endDate}" /></small>
			</h1>
		</div>
		<p id="eventDescription">
			<c:out value="${event.description}" escapeXml="false" />
		</p>
        <c:if test="${not empty event.channels}">
            <h2 id="featuring">Featured channels</h2>
            <ul id="channels">
                <c:forEach var="channel" items="${event.channels}" end="3">
                    <li><a href="<c:url value="/channel/watch/${channel.id}" />"><c:out value="${channel.title}" /></a></li>
                </c:forEach>
            </ul>
        </c:if>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>
</body>
</html>