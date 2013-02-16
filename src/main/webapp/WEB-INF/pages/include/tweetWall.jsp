<%@ include file="commonTaglib.jsp"%>
<h3>Tweets</h3>
<div id="tweets">
    <c:choose>
        <c:when test="${param.query ne '' || param.accountId ne ''}">
            <c:if test="${param.accountId ne ''}">
                <p>
                    Follow
                    <a href="http://twitter.com/<c:out value="${fn:substringAfter(param.accountId,'@')}" />">
                        <strong><c:out value="${param.accountId}" /></strong>
                    </a> on Twitter.

                </p>
            </c:if>
            <c:if test="${param.query ne ''}">
                <p>
                    Search
                    <a href="http://twitter.com/search?q=<c:out value='${fn:replace(param.query, "#", "%23")}' />">
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