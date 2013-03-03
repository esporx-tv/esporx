<%@ include file="../include/commonTaglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="admin.page.title" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/admin.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/admin', 'jqueryui'], function($, admin) {
            $(document).ready(function() {
                admin.trigger();
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">&iexcl;Welcome <c:out value="${adminName}" />!</h1>
			<div id="catContent">
				<div class="adminItem">
                    <div id="tabulations" class="adminContent">
                        <ul class="adminNav">
                            <li class="adminName"><a href="#showSlides">Gondola Slide</a></li>
                            <li class="adminName"><a href="#showSlots">Configurable Slot</a></li>
                            <li class="adminName"><a href="#showEvents">Event</a></li>
                            <li class="adminName"><a href="#showChannels">Channel</a></li>
                            <li class="adminName"><a href="#showCrawled">Crawled</a></li>
                            <li class="adminName"><a href="#showGame">Game</a></li>
                        </ul>
						<div id="showSlides">
							<div class="adminInfos">
								<div id="slidesList">
									<c:forEach var="gondola" items="${gondolas}">
										<div class="gondolaBox">
											<span><strong style="text-transform: uppercase">[<c:out value="${gondola.language}" />]</strong><c:out value="${gondola.title}" /></span>
											<form class="formSlide" action="/admin/slide/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${gondola.id}" />" /> 
												<div class="submit">
													<input class="send" name="send" type="submit" value="Remove" />
												</div>
											</form>
											<a href="<c:url value="/admin/slide/edit/${gondola.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
								<ul>
									<li><a href="/admin/slide/new">[+] Add a new slide</a></li>
								</ul>
							</div>
						</div>
						<div id="showSlots">
							<div class="adminInfos">
								<div id="slotsList">
									<c:forEach var="slot" items="${slots}">
										<div class="slotBox">
											<span><c:out value="${slot.title}" /> </span>
											<form class="formSlot" action="/admin/slot/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${slot.id}" />" /> 
												<div class="submit">
													<input class="send" name="send" type="submit" value="Remove" />
												</div>
											</form>
											<a href="<c:url value="/admin/slot/edit/${slot.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
								<ul>
									<li><a href="<c:url value="/admin/slot/new" />">[+] Add a new configurable slot</a></li>
                                    <li><a href="<c:url value="/admin/slot/layout" />">[+] Lay slots out</a></li>
								</ul>
							</div>
						</div>
						<div id="showEvents">
							<div class="adminInfos">
								<div id="eventsList">
									<c:forEach var="event" items="${events}">
										<div class="eventBox">
											<span><c:out value="${event.title}" /> </span>
											<form class="formEvent" action="/admin/event/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${event.id}" />" /> 
												<div class="submit">
													<input class="send" name="send" type="submit" value="Remove" />
												</div>
											</form>
											<a href="<c:url value="/admin/event/edit/${event.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
                                <ul>
                                    <li><a href="<c:url value="/admin/event/new" />">[+] Add an event</a></li>
                                    <li><a href="<c:url value="/admin/occurrence/manage" />">[+] Plan a broadcast</a></li>
                                </ul>
							</div>
						</div>
						<div id="showChannels">
							<div class="adminInfos">
								<div id="channelsList" >
									<c:forEach var="channel" items="${channels}">
										<div class="channelBox">
											<span><c:out value="${channel.title}" /> </span>
											<form class="formChannel" action="/admin/channel/remove" method="POST">
												<input name="id" type="hidden" value="<c:out value="${channel.id}" />" />
												<div class="submit">
													<input class="send" name="send" type="submit" value="Remove" />
												</div>
											</form>
											<a href="<c:url value="/admin/channel/edit/${channel.id}" />">Edit</a>
										</div>
									</c:forEach>
								</div>
								<ul>
									<li>
										<a href="/admin/channel/new">[+] Add a channel</a>
									</li>
								</ul>
							</div>
						</div>
						<div id="showCrawled">
							<div class="adminInfos">
								<div id="crawledChannelsList" >
								    <div class="channelBox">
								        <table>
								            <c:forEach var="crawledChannel" items="${crawledChannels}">
								            <tr>
											    <td><c:out value="${crawledChannel.name}" /></td>
											    <td><c:out value="${crawledChannel.link}" /></td>
											    <td><form class="formChannel" action="/admin/channel/remove" method="POST">
												    <input name="id" type="hidden" value="12" />
												    <div class="submit">
													<input class="send" name="send" type="submit" value="Remove" />
												    </div>
											    </form></td>
											    <td>
											        <div class="submit">
											        <a class="send" href="<c:url value="/admin/channel/edit/1" />">Edit</a>
											        </div>
											    </td>
											</tr>
											</c:forEach>
										</table>
									</div>
								</div>
								<ul>
									<li>
										<a href="/admin/channel/new">[+] Add a channel</a>
									</li>
								</ul>
							</div>
						</div>
                        <div id="showGame">
                            <div class="adminInfos">
                                <div id="gameList" >
                                    <c:forEach var="game" items="${games}">
                                        <div class="gameBox">
                                            <span><c:out value="${game.title}" /> </span>
                                            <form class="formGame" action="/admin/game/remove" method="POST">
                                                <input name="id" type="hidden" value="<c:out value="${game.id}" />" />
                                                <div class="submit">
                                                    <input class="send" name="send" type="submit" value="Remove" />
                                                </div>
                                            </form>
                                            <a href="<c:url value="/admin/game/edit/${game.id}" />">Edit</a>
                                        </div>
                                    </c:forEach>
                                </div>
                                <ul>
                                    <li>
                                        <a href="/admin/game/new">[+] Add a game</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
					</div>
				</div>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>