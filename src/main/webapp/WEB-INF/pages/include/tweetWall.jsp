<%@ include file="commonTaglib.jsp"%>
<div id="tweets">
    <h3>Tweets</h3>
    <c:choose>
        <c:when test="${not empty param.query || not empty param.accountId}">
            <c:if test="${not empty param.accountId}">
                <p>
                    Follow
                    <a href="http://twitter.com/<c:out value="${fn:substringAfter(param.accountId,'@')}" />">
                        <strong><c:out value="${param.accountId}" /></strong>
                    </a> on Twitter.
                </p>
            </c:if>
            <c:if test="${not empty param.query}">
                <p>
                    Search
                    <a href="http://twitter.com/search?q=<c:out value='${fn:replace(fn:replace(param.query, ",", "%20OR%20"), "#", "%23")}' />">
                        <strong><c:out value="${param.query}" /></strong>
                    </a> on Twitter.
                </p>
            </c:if>
            <ul></ul>
        </c:when>
        <c:otherwise>
            <p>No tweets to display.</p>
        </c:otherwise>
    </c:choose>
</div>