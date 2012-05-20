<%@ include file="../include/common.jsp"%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
    <title><spring:message code="home.header" /></title>
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/timeline.css" />" type="text/css" />
    
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/timeline.js"/>"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>

	<style>
	abbr,article,aside,audio,canvas,datalist,details,figure,dialog,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video
	{
		display: block;
	}
	</style>
	<!--[if lt IE 9]>
	   <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->


    <link rel="stylesheet/less" type="text/css"	href="<c:url value="${staticRoot}/css/styles.less"/>">
    <script src="<c:url value="${staticRoot}/js/ext/less.js"/>"	type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="timeline.prog.header" />
			</h1>
			<div id="catContent">
				<div id="calContent">
					<c:choose>
						<c:when test="${empty timeline.columns}">
							<p>Nothing at the moment...</p>
						</c:when>
						<c:otherwise>
							<c:forEach var="column" items="${timeline.columns}"	varStatus="status">
								<section id="day<c:out value="${status.count+1}"/>">
									<h2>
										<fmt:formatDate value="${column.start}" pattern="EEEE, MMM d" />
									</h2>
									<c:forEach var="row" items="${column.rows}">
										<c:forEach var="eventSlot" items="${row.slots}">
											<c:set var="event" value="${eventSlot.key}" />
											<c:set var="casts" value="${eventSlot.value}" />
											<article>
												<header>
													<!-- dirty hack to get first cast date -->
													<c:forEach var="cast" items="${casts}" end="0">
														<span class="time"> 
														  <fmt:formatDate pattern="HH:mm" value="${cast.broadcastDate}" />
														</span>
													</c:forEach>
													<h3>${event.title}</h3>
												</header>
												<div class="calEventDesc">
													<c:if test="${event.highlighted}">
														<div class="eventDetails">
															<img class="gFImg"
																src="<c:url value="${staticRoot}/img/events/SC2Event.png" />" />
															<p>
																<c:out escapeXml="false" value="${fn:substring(event.strippedDescription, 0, 140)} ..." />
															</p>
														</div>
													</c:if>
													<div class="calEventCasts">
														<c:forEach var="cast" items="${casts}">
															<a href="<c:url value="/cast/watch/${cast.id}" />"> <c:out
																	value="${cast.title}" />
															</a>
														</c:forEach>
													</div>
												</div>
												<footer></footer>
											</article>
										</c:forEach>
									</c:forEach>
								</section>
							</c:forEach>
						</c:otherwise>
					</c:choose>
                    <div id="arrowLeft">
                        <a href="<c:url value="/timeline/shift/past/${currentTimestamp}" />">Previous</a>
                    </div>
                    <div id="arrowRight">
                        <a href="<c:url value="/timeline/shift/future/${currentTimestamp}" />">Next</a>
                    </div>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>