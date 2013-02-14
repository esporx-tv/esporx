<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="event.page.title"
                           arguments="${event.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/event'], function($, event) {
            $(document).ready(function() {
                event.fetchTweets("<c:out value="${event.twitterSearch}" />");
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<c:out value="${event.title}" />
			</h1>
		</div>
		<p id="eventDescription">
			<c:out value="${event.description}" escapeXml="false" />
		</p>

        <c:import url="/WEB-INF/pages/include/tweetWall.jsp">
            <c:param name="query" value="${event.twitterSearch}" />
        </c:import>

		<h3>TODO?</h3>
		<ul>
			<li>show next occurrences and channels?</li>
			<li>show 3 hottest events</li>
		</ul>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>