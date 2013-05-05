<%@ include file="commonTaglib.jsp"%>
<div class="tweets">
    <h3>Tweets</h3>
    <c:choose>
        <c:when test="${not empty param.query || not empty param.accountId}">
            <c:if test="${not empty param.accountId}">
                <p class="lead">
                    Follow
                    <a href="http://twitter.com/<c:out value="${fn:substringAfter(param.accountId,'@')}" />"><strong><c:out value="${param.accountId}" /></strong></a>
                    on Twitter.
                </p>
            </c:if>
            <c:if test="${not empty param.query}">
                <p class="lead">
                    Search
                    <a href="http://twitter.com/search?q=<c:out value='${fn:replace(fn:replace(param.query, ",", "%20OR%20"), "#", "%23")}' />"><strong><c:out value="${param.query}" /></strong></a>
                    on Twitter.
                </p>
            </c:if>
            <div id="tweetWall" class="carousel vertical slide">
                <ol class="carousel-indicators">

                </ol>
                <div class="carousel-inner offset1 span10">
                </div>
                <a class="carousel-control left" href="#tweetWall" data-slide="prev" target="_blank">&uarr;</a>
                <a class="carousel-control right" href="#tweetWall" data-slide="next" target="_blank">&darr;</a>
            </div>
        </c:when>
        <c:otherwise>
            <p>No tweets to display.</p>
        </c:otherwise>
    </c:choose>
</div>