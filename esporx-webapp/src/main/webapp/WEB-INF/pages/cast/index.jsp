<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="cast.details.header" />
</title>
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/cast.css" />" type="text/css" />

    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>    
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
    
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
	<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
		<c:choose>
			<c:when test="${empty cast}">
				<h1 id="mainTitle">
					<spring:message code="cast.details.no_result.title" />
				</h1>
				<div id="catContent">
					<p><spring:message code="cast.details.no_result.text" /></p>
				</div>
			</c:when>
			<c:otherwise>
				<h1 id="mainTitle">
					<spring:message code="cast.details.labels.cast" /> &ldquo;<c:out value="${cast.title}" />&rdquo;
				</h1>
				<div id="catContent">
					<div class="videoPlayer">
						<c:out value="${embeddedVideo}" escapeXml="false" />
					</div>
					<div class="castDetails">
						<blockquote>
							<p><c:out value="${cast.description}" /></p>
						</blockquote>
						<ul>
							<li><spring:message code="cast.details.labels.language" /> <c:choose>
									<c:when test="${empty cast.language}">
										Other
									</c:when>
									<c:otherwise>
										<c:out value="${cast.language}" />
									</c:otherwise>
								</c:choose></li>
						</ul>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>