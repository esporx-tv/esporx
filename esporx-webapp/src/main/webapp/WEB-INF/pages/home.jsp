<%@ include file="./include/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="home.header" /></title>
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/home.css" />" type="text/css" />
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
<script type="text/javascript" src="${staticRoot}/js/pages/gondola.js"></script>

<!-- HTML5 Navigators Compatibility Settings -->
<!--[if lt IE 7]>
		<div style='border: 1px solid #F7941D; background: #FEEFDA; text-align: center; position: relative;'>    
			<div style='position: absolute; right: 3px; top: 3px; font-family: courier new; font-weight: bold;'>
				<a href='#' onclick='javascript:this.parentNode.parentNode.style.display="none"; return false;'>
				<img src='http://www.ie6nomore.com/files/theme/ie6nomore-cornerx.jpg' style='border: none;' alt='Close this notice'/>
				</a>
			</div>
			<div style='width: 640px; margin: 0 auto; text-align: left; padding: 0; overflow: hidden; color: black;'>
				<div style='width: 75px; float: left;'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-warning.jpg' alt='Warning!'/></div>
				<div style='width: 275px; float: left; font-family: Arial, sans-serif;'>
					<div style='font-size: 14px; font-weight: bold; margin-top: 12px;'>Vous utilisez un navigateur ancien.</div>
					<div style='font-size: 12px; margin-top: 6px; line-height: 12px;'>Pour un bon usage de ce site, nous vous conseillons d'installer l'un des navigateurs récents suivants :</div>
				</div>
				<div style='width: 75px; float: left;'><a href='http://www.firefox.com' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-firefox.jpg' style='border: none;' alt='Get Firefox 3.5'/></a></div>
				<div style='width: 75px; float: left;'><a href='http://www.browserforthebetter.com/download.html' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-ie8.jpg' style='border: none;' alt='Get Internet Explorer 8'/></a></div>
				<div style='width: 73px; float: left;'><a href='http://www.apple.com/safari/download/' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-safari.jpg' style='border: none;' alt='Get Safari 4'/></a></div>
				<div style='float: left;'><a href='http://www.google.com/chrome' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-chrome.jpg' style='border: none;' alt='Get Google Chrome'/></a></div>
			</div>
		</div>
	<![endif]-->

<style>
abbr,article,aside,audio,canvas,datalist,details,figure,dialog,footer,header,hgroup,mark,menu,meter,nav,output,progress,section,time,video
	{
	display: block;
}
</style>

<!--[if lt IE 9]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<!-- HTML5 Navigators Compatibility Settings -->

<link rel="stylesheet/less" type="text/css"
	href="<c:url value="${staticRoot}/css/styles.less"/>">
<script src="<c:url value="${staticRoot}/js/ext/less.js"/>"
	type="text/javascript"></script>
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
								<a class="gLink" href="<c:url value="${slide.link}" />"> <img
									class="gFImg" src="${slide.picture}" />
									<div class="fText">
										<section class="fInfos">
											<h2 class="fTitle">${slide.title}</h2>
											<p class="fTagline">${slide.tagLine}</p>
											<p class="fDesc">${slide.description}</p>
										</section>
										<section class="fDetails">
											<ul class="dList">
												<li class="dLang"><span>Lang: </span><span>${slide.language}</span>
												</li>
												<li class="dPrice"><span>Price: </span><span>${slide.prize}</span>
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
							<article class="evBox oneCol">
								<h4>Hottest Live Event</h4>
								<div class="evContent">
									<img class="gFImg" src="${slot.picture}" />
									<a href="<c:url value="${slot.link}" />">
										<div class="shortDesc">
											<p>${slot.title}</p>
										</div> 
									</a>
								</div>
							</article>
						</c:if>
						<c:if test="${slot.position == 2}">
							<article class="evBox oneCol">
								<h4>Up next</h4>
								<div class="evContent">
									<img class="gFImg" src="${slot.picture}" />
									<a href="<c:url value="${slot.link}" />">
										<div class="shortDesc">
											<p>${slot.title}</p>
										</div> 
									</a>
								</div>
							</article>
						</c:if>
						<c:if test="${slot.position == 3}">
							<article class="evBox twoCols">
								<h4>Event</h4>
								<div class="evContent">
									<img class="eImg" src="${slot.picture}" />
									<div class="shortDesc">
										<a href="<c:url value="${slot.link}" />">
											<h5>${slot.title}</h5>
										</a>
										<p>${slot.description}</p>
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