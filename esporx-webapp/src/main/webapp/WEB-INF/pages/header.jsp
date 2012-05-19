<%@ include file="./include/common.jsp" %>	
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
			<li><a href="<c:url value="/event/list" />"><spring:message code="common.menu.events" /></a></li>
			<li>
                <a href="<c:url value="/cast/browse" />"><spring:message code="common.menu.casts" /></a>
				<ul id="castNav" class="subNavLvl1">
					<li><a href="#"><spring:message code="common.menu.latest_casts" /></a></li>
					<li><a href="<c:url value="/cast/new" />"><spring:message code="common.menu.add_cast" /></a></li>
					<li><a href="<c:url value="/event/new" />"><spring:message code="common.menu.add_event" /></a></li>
				</ul>
			</li>
			<li><a href="<c:url value="/timeline/browse" />"><spring:message code="common.menu.calendar" /></a></li>
			<li><a href="<c:url value="/admin/home" />">Admin</a></li>
		</ul>
	</nav>
</header>