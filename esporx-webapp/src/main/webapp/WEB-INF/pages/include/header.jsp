<%@ include file="commonTaglib.jsp" %>
 <c:import url="/WEB-INF/pages/include/noIE6.jsp" />
<header id="pageHeader">
	<hgroup id="logoGroup">
		<div id="TEHLogo">
			<span id="Player1"></span>
			<span id="Player2"></span>
			<span id="MidLine"></span>
			<span id="Ball"></span>
		</div>
		<h1 id="LogoTitle">ESPORX.TV</h1>
		<h2 id="LogoBaseline">Premium Live Esports Broadcasts</h2>
	</hgroup>
	<nav id="siteNav">
		<ul>
			<li><a href="<c:url value="/home" />"><spring:message code="common.menu.home" /></a></li>
			<li><a href="<c:url value="/calendar/browse" />"><spring:message code="common.menu.calendar" /></a></li>
			<li><a href="<c:url value="/event/browse" />"><spring:message code="common.menu.events" /></a></li>
			<li><a href="<c:url value="/admin/home" />">Admin</a></li>
		</ul>
	</nav>
</header>