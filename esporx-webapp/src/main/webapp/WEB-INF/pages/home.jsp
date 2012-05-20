<%@ include file="./include/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title><spring:message code="home.header" /></title>
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/home.css" />" type="text/css" />
    
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/sanityChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/externalLinkDetector.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/common.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/gondola.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/homepage.js" />"></script>

    <!-- HTML5 Navigators Compatibility Settings -->
	<style>
		abbr,article,aside,audio,canvas,datalist,details,figure,dialog,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video {
			display: block;
		}
	</style>

	<!--[if lt IE 9]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<!-- /HTML5 Navigators Compatibility Settings -->

    <link rel="stylesheet/less" type="text/css"	href="<c:url value="${staticRoot}/css/styles.less"/>">
    <script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>

<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="homeMain">
			<section id="homeGondola">
				<div id="gSlideView">
					<div id="gSlide">
						<c:forEach var="slide" items="${gondolaSlides}">
							<article id="gF${slide.id}" class="gFrame event">
								<a class="gLink" href="<c:url value="${slide.link}" />"> 
								    <img class="gFImg" src="${slide.picture}" />
									<div class="fText">
										<section class="fInfos">
											<h2 class="fTitle">${slide.title}</h2>
											<p class="fTagline">${slide.tagLine}</p>
											<p class="fDesc"><c:out value="${slide.description}" escapeXml="false" /></p>
										</section>
										<section class="fDetails">
											<ul class="dList">
												<li class="dLang"><span>Lang: </span><span>${slide.language}</span>
												</li>
												<li class="dPrice"><span>Prize: </span><span>${slide.prize}</span>
												</li>
												<li class="dDate"><span>Date: </span><span><fmt:formatDate
															pattern=" dd MMMM yyyy" value="${slide.date}" /> </span></li>
											</ul>
										</section>
									</div> </a>
							</article>
						</c:forEach>
						<div id="arrowLeft">
							<p>Previous</p>
						</div>
						<div id="arrowRight">
							<p>Next</p>
						</div>
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
							<h5>Most Viewed Casts</h5>
							<div class="newsLinks">
								<c:forEach var="cast" items="${mostViewedCasts}">
									<a class="newsLink"
										href="<c:url value="/cast/watch/${cast.id}" />"><c:out
											value="${cast.title}" /> </a>
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
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>