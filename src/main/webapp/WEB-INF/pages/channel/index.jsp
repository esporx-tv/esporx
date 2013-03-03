<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="channel.page.title"
                           arguments="${channel.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/channel.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/channel'], function($, channel) {
            $(document).ready(function(){
                channel.fetchTweets(
                        ".channelDetails .tweets",
                        "<c:out value="${channel.twitterId}" />",
                        "<c:out value="${channel.twitterSearch}" />"
                );
                channel.fetchTweets(
                        ".eventDetails .tweets",
                        "<c:out value="${currentEvent.twitterId}" />",
                        "<c:out value="${currentEvent.twitterSearch}" />"
                );
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body<c:if test="${not empty currentGame && not empty currentGame.backgroundUrl}"><c:out value=' style="background-image:url(${currentGame.backgroundUrl})"' escapeXml="false" /></c:if>>
<div id="pageContent">
    <c:import url="/WEB-INF/pages/include/header.jsp" />
    <div id="catMain">
        <h1 id="mainTitle">
            <c:if test="${not empty currentEvent}">
                <c:out value="${currentEvent.title} by" />
            </c:if>
            <c:out value="${channel.title}" />
        </h1>
        <div id="catContent">
            <div class="videoPlayer">
                <c:out value="${embeddedVideo}" escapeXml="false" />
            </div>
            <h2>
                You are watching
                <c:if test="${not empty currentEvent}">
                    <c:out value="${currentEvent.title}" /> brought to you by
                </c:if>
                <c:out value="${channel.title}" />
            </h2>
            <div class="eventDetails<c:if test="${not empty currentEvent}"> half</c:if>">
                <c:if test="${not empty currentEvent}">
                    <p id="eventDescription">
                        <c:out value="${currentEvent.description}" escapeXml="false" />
                    </p>
                    <ul id="eventBroadcasts">
                        <c:forEach var="nextBroadcast" items="${nextBroadcastsByEvent}">
                            <li><pretty:format date="${nextBroadcast.startDateTime}" />
                                <ul>
                                    <c:forEach var="broadcastChannel" items="${nextBroadcast.channels}">
                                        <li><a href="<c:out value="/watch/${broadcastChannel.id}" />">${broadcastChannel.title}</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:import url="/WEB-INF/pages/include/tweetWall.jsp">
                        <c:param name="query" value="${currentEvent.twitterSearch}" />
                        <c:param name="accountId" value="${currentEvent.twitterId}" />
                    </c:import>
                </c:if>
            </div>
            <div class="channelDetails<c:if test="${not empty currentEvent}"> half</c:if>">
                <blockquote>
                    <p><c:out value="${channel.description}" escapeXml="false" /></p>
                </blockquote>

                <ul>
                    <li><spring:message code="channel.details.labels.language" /> <c:choose>
                        <c:when test="${empty channel.language}">
                            Other
                        </c:when>
                        <c:otherwise>
                            <c:out value="${channel.language}" />
                        </c:otherwise>
                    </c:choose></li>
                </ul>

                <c:forEach var="nextBroadcast" items="${nextBroadcastsByChannel}">
                    <li><pretty:format date="${nextBroadcast.startDateTime}" />, watch <c:out value="${nextBroadcast.event.title}" />
                    </li>
                </c:forEach>
                <c:import url="/WEB-INF/pages/include/tweetWall.jsp">
                    <c:param name="query" value="${channel.twitterSearch}" />
                    <c:param name="accountId" value="${channel.twitterId}" />
                </c:import>
            </div>
            <c:if test="${not empty currentBroadcast.channels && fn:length(currentBroadcast.channels) > 1}">
                <p id="alsoBroadcasting">
                    Also broadcasting this event:
                    <c:forEach var="currentBroadcastChannel" items="${currentBroadcast.channels}">
                        <c:if test="${currentBroadcastChannel ne channel}">
                            <a href="<c:out value="/watch/${currentBroadcastChannel.id}" />">${currentBroadcastChannel.title}</a>
                        </c:if>
                    </c:forEach>
                </p>
            </c:if>


            <c:if test="${fn:length(liveNowEvents) >= 1 && fn:length(firstLiveBroadcasts) > 1}">
                <p id="liveNow">
                    Also live now :
                    <c:forEach var="occurrencesPerHour" items="${liveNowEvents}" >
                        <c:forEach var="occurrence" items="${occurrencesPerHour.value}" varStatus="count">
                            <c:set var="event" value="${occurrence.event}" />
                            <ul>
                                <c:forEach var="broadcastChannel" items="${occurrence.channels}">
                                    <c:if test="${broadcastChannel != channel}">
                                        <li><img src="<c:out value="${occurrence.game.iconUrl}" />" alt="<c:out value="${occurrence.game.title}"/>" />
                                            [<c:out value="${event.title}" />] on <a href="<c:url value="/channel/watch/${broadcastChannel.id}" />"><c:out value="${broadcastChannel.title}" /></a></li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </c:forEach>
                    </c:forEach>
                </p>
            </c:if>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>