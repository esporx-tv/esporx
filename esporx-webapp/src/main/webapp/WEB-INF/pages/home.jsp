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
                                <%--<article class="gFrame">--%>
								<%--<a class="gLink " href="<c:url value="${slide.link}" />">--%>
								    <%--<img class="gFImg" src="${slide.picture}" />--%>
									<%--<div class="fText">--%>
										<%--<section class="fInfos">--%>
											<%--<h2 class="fTitle">${slide.title}</h2>--%>
											<%--<p class="fTagline">${slide.tagLine}</p>--%>
											<%--<p class="fDesc"><c:out value="${slide.description}" escapeXml="false" /></p>--%>
										<%--</section>--%>
										<%--<section class="fDetails">--%>
											<%--<ul class="dList">--%>
												<%--<li class="dLang"><span>Lang: </span><span>${slide.language}</span>--%>
												<%--</li>--%>
												<%--<li class="dPrice"><span>Prize: </span><span>${slide.prize}</span>--%>
												<%--</li>--%>
												<%--<li class="dDate"><span>Date: </span><span><fmt:formatDate--%>
															<%--pattern=" dd MMMM yyyy" value="${slide.date}" /> </span></li>--%>
											<%--</ul>--%>
										<%--</section>--%>
									<%--</div> </a>--%>
							<%--</article>--%>
						</c:forEach>
						<%--<a href="#" class="carousel-control" id="arrowLeft" rel="prev">--%>
							<%--<p>Previous</p>--%>
						<%--</a>--%>
						<%--<a href="#" class="carousel-control" id="arrowRight" rel="next">--%>
							<%--<p>Next</p>--%>
						<%--</a>--%>
					</div>
				</div>
			</section>
			<section id="homeContent">
				<section id="homeHilits">
					<c:forEach var="slot" items="${slots}">
						<c:if test="${slot.position == 1}">
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
						<c:if test="${slot.position == 2}">
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
						<c:if test="${slot.position == 3}">
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
							<h5>Hottest Live Events</h5>
							<div class="newsLinks">
								<c:forEach var="event" items="${mostViewedEvents}">
									<a class="newsLink"
										href="<c:url value="/event/see/${event.id}" />"><c:out
											value="${event.title}" /> </a>
								</c:forEach>
							</div>
						</article>
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
											<h3>${event.title}</h3>
										</div>
										<table class="channelsList">
							    			<c:forEach var="channel" items="${channels}">
							    				<tr>
							    					<td><a href="${channel.videoUrl}">${channel.title}</a></td>
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
							<h5>Most Viewed Channels</h5>
							<div class="newsLinks">
								<c:forEach var="channel" items="${mostViewedChannels}">
									<a class="newsLink"
										href="<c:url value="/channel/watch/${channel.id}" />"><c:out
											value="${channel.title}" /> </a>
								</c:forEach>
							</div>
						</article>
						<article class="newsBox">
							<h5>Up next</h5>
							<div class="newsLinks">
								<c:forEach var="upNext" items="${upNextEvents}">
									<a class="newsLink"
										href="<c:url value="/event/see/${upNext.id}" />"><c:out
											value="${upNext.title}" /> </a>
								</c:forEach>
							</div>
						</article>
					</div>
				</section>
			</section>
		</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>
</body>
</html>