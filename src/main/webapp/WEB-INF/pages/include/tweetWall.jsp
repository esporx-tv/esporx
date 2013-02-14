<%@ include file="commonTaglib.jsp"%>
<h3>Tweets</h3>
<div id="tweets">
    <c:choose>
        <c:when test="${param.query ne '' || param.accountId ne ''}">
            <ul></ul>
        </c:when>
        <c:otherwise>
            <p>No tweets to display.</p>
        </c:otherwise>
    </c:choose>
</div>