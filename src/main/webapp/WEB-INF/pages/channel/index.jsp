<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="channel.page.title"
                           arguments="${channel.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/channel.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/channel'], function($, channel) {
            $(document).ready(function(){
                channel.fetchTweets("<c:out value="${channel.twitterSearch}" />");
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
		<c:choose>
			<c:when test="${empty channel}">
				<h1 id="mainTitle">
					<spring:message code="channel.details.no_result.title" />
				</h1>
				<div id="catContent">
					<p><spring:message code="channel.details.no_result.text" /></p>
				</div>
			</c:when>
			<c:otherwise>
				<h1 id="mainTitle">
					<spring:message code="channel.details.labels.channel" /> &ldquo;<c:out value="${channel.title}" />&rdquo;
				</h1>
				<div id="catContent">
					<div class="videoPlayer">
						<c:out value="${embeddedVideo}" escapeXml="false" />
					</div>
					<div class="channelDetails">
						<blockquote>
							<p><c:out value="${channel.description}" escapeXml="false" /></p>
						</blockquote>
						<ul>
							<li><spring:message code="channel.details.labels.language" /> <c:choose>
									<c:when test="${empty channel.language}">
										Other
									</c:when>
									<c:otherwise>
										<c:out value="${channel.language}" />
									</c:otherwise>
								</c:choose></li>
						</ul>
                        <h3>Tweets</h3>
                        <div id="tweets">
                            <ul></ul>
                        </div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>