<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="event.submission.header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />"	type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="event.submission.main_header" />
			</h1>
			<div id="catContent">
				<form name="linkForm" action="/event/link" method="POST">
					<select name="channel" >
					<c:forEach var="channel" items="${channels}">
							<option value="<c:out value="${channel.id}" />"><c:out value="${channel.title}" /></option>
					</c:forEach>
					</select>
					<input type="hidden" value="<c:out value="${event.id}" />" name="relatedEvent" />
					<input name="submit" type="submit"	value="submit" />
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>
</body>
</html>
					
