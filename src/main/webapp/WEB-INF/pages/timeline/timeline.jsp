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
						<c:when test="${empty timeline}">
							<p>Nothing at the moment...</p>
						</c:when>
						<c:otherwise>
							<c:forEach var="occurrencesPerHourPerDay" items="${timeline}" varStatus="status">
								<section id="day<c:out value="${status.count+1}"/>">
									<h2>
										<joda:format value="${occurrencesPerHourPerDay.key}" pattern="EEEE, MMM d" />
									</h2>
									<c:forEach var="occurrencesPerHour" items="${occurrencesPerHourPerDay.value}">
										<c:forEach var="occurrence" items="${occurrencesPerHour.value}">
											<c:set var="event" value="${occurrence.event}" />
											<c:set var="channels" value="${occurrence.channels}" />
											<article>
												<a href="/event/see/${event.id}">
													<header>
                                                        <span class="time">
                                                          <joda:format pattern="HH:mm" value="${occurrencesPerHour.key}" />
                                                        </span>
														<h3>${event.title}</h3>
													</header>
													<div class="calEventDesc">
														<c:if test="${event.highlighted}">
															<div class="eventDetails">
																<img class="gFImg" src="<c:url value="${staticRoot}/img/events/SC2Event.png" />" />
																<p>
																	<c:out escapeXml="false" value="${fn:substring(event.strippedDescription, 0, 140)} ..." />
																</p>
															</div>
														</c:if>
													</a>
													<div class="calEventChannels">
														<c:forEach var="channel" items="${channels}">
															<a href="<c:url value="/channel/watch/${channel.id}" />"> <c:out value="${channel.title}" /></a>
														</c:forEach>
													</div>
												</a>
											</article>
										</c:forEach>
									</c:forEach>
								</section>
							</c:forEach>
						</c:otherwise>
					</c:choose>
                    <div id="arrowLeft">
                        <a href="<c:url value="/calendar/browse/start/${previousStart}" />">Previous</a>
                    </div>
                    <div id="arrowRight">
                        <a href="<c:url value="/calendar/browse/start/${nextStart}" />">Next</a>
                    </div>
				</div>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>