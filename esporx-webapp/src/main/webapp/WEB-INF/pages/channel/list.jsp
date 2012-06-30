<%-- not used right now... --%>
<%@ include file="../include/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title><spring:message code="channel.list.header" /></title>
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/channel.css" />"	type="text/css" />
    
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>    
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/domNavigationUtils.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/externalLinkDetector.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/common.js" />"></script>
    
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="channel.list.main_header" />
			</h1>
			<c:choose>
				<c:when test="${empty channels}">
					<p>
						<spring:message code="channel.list.no_results" />
					</p>
				</c:when>
				<c:otherwise>
					<div id="catContent">
						<c:forEach var="channel" items="${channels}">
							<div class="channelItem">
								<h2 class="channelName">
									<a href="<c:url value="/channel/watch/${channel.id}" />">
										&ldquo; <c:out value="${channel.title}" /> &rdquo;</a>
								</h2>
								<ul class="channelInfos">
									<li class="channelBroadcastDate"><fmt:formatDate
											pattern="EEEEEEEEEEE dd MMMM yyyy, HH:mm"
											value="${channel.broadcastDate}" />
									</li>
									<li class="channelLanguage">${channel.language}</li>
								</ul>
			
								<blockquote class="channelDescription">
									<p>&ldquo;<c:out value="${fn:substring(channel.description,0,200)}" />...&rdquo;</p>
								</blockquote>
								<a href="<c:url value="/channel/watch/${channel.id}" />"> &gt;&gt; </a>
							</div>
							
						</c:forEach>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>