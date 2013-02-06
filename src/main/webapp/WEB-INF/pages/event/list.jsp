<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="event.list.main_header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />"	type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery'], function($) {
            $(document.body).fadeTo('fast', 1);
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">Events List</h1>
			<div id="eventList">
				<c:forEach var="event" items="${events}">
					<div class="eventBox">
						<h2 class="eventTitle"><a href="<c:url value="/event/see/${event.id}" />"><c:out value="${event.title}" /></a></h2>
						<blockquote>
                            <c:out escapeXml="false" value="${fn:substring(event.strippedDescription,0,140)} ..." />
                        </blockquote>
					</div>
				</c:forEach>
	        </div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>