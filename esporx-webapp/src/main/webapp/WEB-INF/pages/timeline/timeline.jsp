<%@ include file="../include/commonTaglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="home.header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/timeline.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
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
											<c:set var="channels" value="${eventSlot.value}" />
											<article>
												<a href="/event/see/${event.id}">
													<header>
														<!-- dirty hack to get first channel date -->
														<c:forEach var="channel" items="${channels}" end="0">
															<span class="time"> 
															  <fmt:formatDate pattern="HH:mm" value="${channel.broadcastDate}" />
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
													</a>
													<div class="calEventChannels">
														<c:forEach var="channel" items="${channels}">
															<a href="<c:url value="/channel/watch/${channel.id}" />"> <c:out
																	value="${channel.title}" />
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
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>
</body>
</html>