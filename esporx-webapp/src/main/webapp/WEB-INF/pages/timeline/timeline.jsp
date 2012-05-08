<%@ include file="../include/common.jsp"%><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="home.header" /></title>
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/timeline.css" />" type="text/css" />
<script type="text/javascript" src="${staticRoot}/js/ext/prototype.js"></script>

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

							<c:forEach var="column" items="${timeline.columns}"
								varStatus="status">
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
													<div class="eventDetails">
														<img class="gFImg"
															src="<c:url value="${staticRoot}/img/events/SC2Event.png" />" />
														<p>
															<c:out value="${event.description}" escapeXml="false" />
														</p>
													</div>
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
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>