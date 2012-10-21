<%@ include file="../include/commonTaglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="admin.submission.main_header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/admin.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/admin', 'jqueryui'], function($, admin) {
            $(document).ready(function() {
                admin.trigger();
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				&iexcl;Welcome <c:out value="${adminName}" />!
			</h1>
			<div id="catContent">
				<div class="adminItem">
                    <div id="tabulations" class="adminContent">
                        <ul class="adminNav">
                            <li class="adminName"><a href="#showSlides">Gondola Slide</a></li>
                            <li class="adminName"><a href="#showSlots">Configurable Slot</a></li>
                            <li class="adminName"><a href="#showEvents">Event</a></li>
                            <li class="adminName"><a href="#showChannels">Channel</a></li>

                        </ul>
						<div id="showSlides">
							<div class="adminInfos">
								<a href="/admin/slide/new">Add a new slide</a>
								<div id="slidesList">
									<c:forEach var="gondola" items="${gondolas}">
										<div class="gondolaBox">
											<span><strong style="text-transform: uppercase">[<c:out
														value="${gondola.language}" />]
											</strong> <c:out value="${gondola.title}" /> </span>
											<form class="formSlide" action="/admin/slide/remove"
												method="POST">
												<input name="id" type="hidden"
													value="<c:out value="${gondola.id}" />" /> <input
													class="send" name="send" type="submit" value="Remove" />
											</form>
											<a href="<c:url value="/admin/slide/edit/${gondola.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<div id="showSlots">
							<div class="adminInfos">
								<a href="<c:url value="/admin/slot/new" />">Add a new
									configurable slot</a>
								<div id="slotsList">
									<c:forEach var="slot" items="${slots}">
										<div class="slotBox">
											<span><c:out value="${slot.title}" /> </span>
											<form class="formSlot" action="/admin/slot/remove"
												method="POST">
												<input name="id" type="hidden"
													value="<c:out value="${slot.id}" />" /> <input
													class="send" name="send" type="submit" value="Remove" />
											</form>
											<a href="<c:url value="/admin/slot/edit/${slot.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<div id="showEvents">
							<div class="adminInfos">
                                <ul>
                                    <li><a href="<c:url value="/occurrence/new" />">Plan an event</a></li>
                                    <li><a href="<c:url value="/event/new" />">Add an event</a></li>
                                </ul>
								<div id="eventsList">
									<c:forEach var="event" items="${events}">
										<div class="eventBox">
											<span><c:out value="${event.title}" /> </span>
											<form class="formEvent" action="/event/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${event.id}" />" /> 
												<input class="send" name="send" type="submit" value="Remove" />
											</form>
											<a href="<c:url value="/event/edit/${event.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<div id="showChannels">
							<div class="adminInfos">
								<a href="/channel/new">Add a channel</a>
								<div id="channelsList" >
									<c:forEach var="channel" items="${channels}">
										<div class="channelBox">
											<span><c:out value="${channel.title}" /> </span>
											<form class="formChannel" action="/channel/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${channel.id}" />" /> 
												<input class="send" name="send" type="submit" value="Remove" />
											</form>
											<a href="<c:url value="/channel/edit/${channel.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<c:import url="/WEB-INF/pages/include/footer.jsp" />
		</div>
	</div>
</body>
</html>