<%-- not used right now... --%>
<%@ include file="../include/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title><spring:message code="cast.list.header" /></title>
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/cast.css" />"	type="text/css" />
    
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
				<spring:message code="cast.list.main_header" />
			</h1>
			<c:choose>
				<c:when test="${empty casts}">
					<p>
						<spring:message code="cast.list.no_results" />
					</p>
				</c:when>
				<c:otherwise>
					<div id="catContent">
						<c:forEach var="cast" items="${casts}">
							<div class="castItem">
								<h2 class="castName">
									<a href="<c:url value="/cast/watch/${cast.id}" />">
										&ldquo; <c:out value="${cast.title}" /> &rdquo;</a>
								</h2>
								<ul class="castInfos">
									<li class="castBroadcastDate"><fmt:formatDate
											pattern="EEEEEEEEEEE dd MMMM yyyy, HH:mm"
											value="${cast.broadcastDate}" />
									</li>
									<li class="castLanguage">${cast.language}</li>
								</ul>
			
								<blockquote class="castDescription">
									<p>&ldquo;<c:out value="${fn:substring(cast.description,0,200)}" />...&rdquo;</p>
								</blockquote>
								<a href="<c:url value="/cast/watch/${cast.id}" />"> &gt;&gt; </a>
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