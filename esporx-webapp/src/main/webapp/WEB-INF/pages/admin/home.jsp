<%@ include file="../include/common.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="admin.submission.main_header" />
</title>
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/datepicker.css" />"
	type="text/css" />
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
<link rel="stylesheet"
	href="<c:url value="${staticRoot}/css/admin.css" />" type="text/css" />

<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/pages/admin.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/prototype-date-extensions.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/datepicker.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/scriptaculous.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/ext/effects.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
<script type="text/javascript"
	src="<c:url value="${staticRoot}/js/lib/sanityChecker.js" />"></script>

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
				Welcome
				<c:out value="${adminName}" />
			</h1>
			<div id="catContent">
				<div class="adminItem">
					<h2 class="adminName">Gondola Slide</h2>
					<ul class="adminInfos">
						<li><a href="/admin/slide/new">Add a new slide</a>
						</li>
						<li><span id="showSlides">Show all slides</span>
						</li>
						<div id="slidesList" class="displayNone">
							<c:forEach var="gondola" items="${gondolas}">
								<div class="gondolaBox">
									<span><strong style="text-transform: uppercase">[<c:out
												value="${gondola.language}" />]</strong> <c:out
											value="${gondola.title}" />
									</span>
									<form class="formSlide" action="/admin/slide/remove" method="POST">
										<input name="id" type="hidden" value="<c:out value="${gondola.id}" />" />
										 <input class="send" name="send" type="submit" value="Remove" />
									</form>
									<a href="<c:url value="/admin/slide/edit/${gondola.id}" />">Edit</a>
								</div>
							</c:forEach>
						</div>
					</ul>
				</div>
				<div class="adminItem">
					<h2 class="adminName">Configurable slot</h2>
					<ul class="adminInfos">
						<li><a href="<c:url value="/admin/slot/new" />">Add a new
								configurable slot</a>
						</li>
						<li><span id="showSlots">Show all configurable slots</span>
						<li>
							<div id="slotsList" class="displayNone">
								<c:forEach var="slot" items="${slots}">
									<div class="slotBox">
										<span><c:out value="${slot.title}" />
										</span>
										<form class="formSlot" action="/admin/slot/remove" method="POST">
											<input name="id" type="hidden"
												value="<c:out value="${slot.id}" />" /> <input class="send"
												name="send" type="submit" value="Remove" />
										</form>
										<a href="<c:url value="/admin/slot/edit/${slot.id}" />">Edit</a>
									</div>
								</c:forEach>
							</div>
					</ul>
				</div>
				<div class="adminItem">
					<h2 class="adminName">Event</h2>
					<ul class="adminInfos">
						<li><a href="<c:url value="/event/new" />">Add an event</a>
						</li>
						<li><span id="showEvents">Show all events</span>
						</li>
						<div id="eventsList" class="displayNone">
							<c:forEach var="event" items="${events}">
								<div class="eventBox">
									<span><c:out value="${event.title}" />
									</span> <a href="<c:url value="/event/remove/${event.id}" />">Remove</a>
									<a href="<c:url value="/event/edit/${event.id}" />">Edit</a> <a
										href="<c:url value="/event/link/${event.id}" />">Link cast
										to this event</a>
								</div>
							</c:forEach>
						</div>
					</ul>
				</div>
				<div class="adminItem">
					<h2 class="adminName">Cast</h2>
					<ul class="adminInfos">
						<li><a href="/cast/new">Add a cast</a>
						<li>
						<li><span id="showCasts">Show all casts</span>
						</li>
						<div id="castsList" class="displayNone">
							<c:forEach var="cast" items="${casts}">
								<div class="castBox">
									<span><c:out value="${cast.title}" />
									</span> <a href="<c:url value="/cast/remove/${cast.id} "/>">Remove</a>
									<a href="<c:url value="/cast/edit/${cast.id}" />">Edit</a>
								</div>
							</c:forEach>
						</div>
					</ul>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>