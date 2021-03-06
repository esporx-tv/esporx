<%@ include file="include/commonTaglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="home.header" /></title>


    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/home.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/homepage'], function($, home) {
            $(document).ready(function() {
                home.trigger();
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="homeMain">
			<section id="homeGondola">
				<div id="gSlideView">
					<ul class="bjqs">
						<c:forEach var="slide" items="${gondolaSlides}" varStatus="loop">
                            <li class="bjqs-slide">
                                <a href="<c:url value="${slide.link}" />" class="slideLink">
                                    <img src="${slide.picture}" />
                                </a>
                                <div class="slide-caption">
                                    <div class='fText'>
                                        <section class='fInfos'>
                                            <h2 class='fTitle'>${slide.title}</h2>
                                            <p class='fTagline'>${slide.tagLine}</p>
                                            <p class='fDesc'><c:out value='${slide.description}' escapeXml='false' /></p>
                                        </section>
                                        <section class='fDetails'>
                                            <ul class='dList'>
                                                <li class='dLang'><span>Lang: </span><span>${slide.language}</span>
                                                </li>
                                                <li class='dPrice'><span>Prize: </span><span>${slide.prize}</span>
                                                </li>
                                                <li class='dDate'><span>Date: </span><span><fmt:formatDate
                                                        pattern=' dd MMMM yyyy' value='${slide.date}' /> </span></li>
                                            </ul>
                                        </section>
                                    </div>
                                </div>
                            </li>
						</c:forEach>
					</ul>
				</div>
			</section>
			<section id="homeContent">
				<section id="homeHilits">
					<c:forEach var="slot" items="${slots}">
						<c:if test="${slot.width == 1}">
							<article class="evBox oneCol <c:if test="${not empty slot.link}"> pointer</c:if>">
								<h4>${slot.boxTitle}</h4>
								<div class="evContent">
									<img class="gFImg" src="${slot.picture}" />
									<c:if test="${not empty slot.link}"><a href="<c:url value="${slot.link}" />"></c:if>
										<div class="shortDesc">
											<p>${slot.title}</p>
										</div>
									<c:if test="${not empty slot.link}"></a></c:if>
								</div>
							</article>
						</c:if>
						<c:if test="${slot.width == 2}">
							<article class="evBox twoCols <c:if test="${not empty slot.link}"> pointer</c:if>">
								<h4>${slot.boxTitle}</h4>
								<div class="evContent">
									<img class="eImg" src="${slot.picture}" />
									<div class="shortDesc">
                                        <c:if test="${not empty slot.link}"><a href="<c:url value="${slot.link}" />"></c:if>
											<h5>${slot.title}</h5>
                                        <c:if test="${not empty slot.link}"></a></c:if>
										<p><c:out value="${slot.description}" escapeXml="false" /></p>
									</div>
								</div>
							</article>
						</c:if>
					</c:forEach>
				</section>
				<section id="homeNews">
					<h4>Also on esporx.tv</h4>
					<div id="alsoBoxes">
						<article class="newsBox">
						    <h5>Live Now</h5>
						    <div class="newsLink">
						    	<c:forEach var="occurrencesPerHour" items="${liveNowEvents}">
									<c:forEach var="occurrence" items="${occurrencesPerHour.value}">
										<c:set var="event" value="${occurrence.event}" />
										<c:set var="channels" value="${occurrence.channels}" />
										<div>
											<span class="time">
												<joda:format value="${occurrencesPerHour.key}" pattern="HH:mm" />
											</span>
                                            <span class="gamePic">
                                                <img src="<c:out value="${occurrence.game.iconUrl}" />"
                                                     alt="<c:out value="${occurrence.game.title}" />" />
                                            </span>
											<h3><c:out value="${event.title}" /></h3>
											<c:forEach var="channel" items="${channels}" end="0">
											    <span><a href="<c:url value="/channel/watch/${channel.id}"/>">Watch now =></a></span>
											</c:forEach>
										</div>
										<table class="channelsList">
							    			<c:forEach var="channel" items="${channels}">
							    				<tr>
							    					<td><a href="<c:url value="/channel/watch/${channel.id}" />">${channel.title}</a></td>
								    				<td>${channel.viewerCount} viewers</td>
								    				<td>[${channel.language}]</td>
							    				</tr>
							    			</c:forEach>
						    			</table>
									</c:forEach>
						    	</c:forEach>
						    </div>
						</article>
						<article class="newsBox">
							<h5>Up next</h5>
							<div class="newsLinks">
								<c:forEach var="event" items="${upNextEvents}">
								    <p><pretty:format date="${occurrence.startDateTime}" /></p>
									<a class="newsLink" href="<c:url value="/event/see/${event.id}" />">
                                        <c:out value="${event.title}" />
                                    </a>
								</c:forEach>
							</div>
						</article>
						<article class="newsBox">
                            <h5>Hottest Live Events</h5>
                            <div class="newsLinks">
                                <c:forEach var="event" items="${mostViewedEvents}">
                                    <a class="newsLink"
                                        href="<c:url value="/event/see/${event.id}" />"><c:out
                                            value="${event.title}" /> </a>
                                </c:forEach>
                            </div>
                        </article>
					</div>
				</section>
			</section>
            <c:import url="/WEB-INF/pages/include/tweetWall.jsp">
                <c:param name="accountId" value="@esporxtv" />
            </c:import>
		</div>
	</div>
    <c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>