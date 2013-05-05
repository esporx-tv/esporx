<%@ include file="commonTaglib.jsp" %>
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="<c:url value="/home" />">ESPORX.TV</a>
        <ul class="nav">
            <li><a href="<c:url value="/home" />"><spring:message code="common.menu.home" /></a></li>
            <secu:authorize access="isAuthenticated()">
                <li><a href="<c:url value="/user/profile" />" > Profile</a></li>
            </secu:authorize>
            <li><a href="<c:url value="/calendar/browse" />"><spring:message code="common.menu.calendar" /></a></li>
            <li><a href="<c:url value="/event/browse" />"><spring:message code="common.menu.events" /></a></li>
            <secu:authorize access="hasRole('ROLE_ADMIN')">
                <li><a href="<c:url value="/admin/home" />">Admin</a></li>
            </secu:authorize>
            <secu:authorize access="isAuthenticated()">
                <li class="divider-vertical"></li>
                <li class="pull-right"><a href="<c:url value="/j_spring_security_logout" />" > Logout</a></li>
            </secu:authorize>
        </ul>
    </div>
</div>