<%@ include file="../include/common.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title><spring:message code="registration.header" />
</title>
<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />"	type="text/css" />
</head>
<body>
	<c:import url="/WEB-INF/pages/header.jsp" />
	<h1>
		<spring:message code="registration.main_header.common" />
	</h1>
	<div class="registration" id="individualRegistration">
		<c:import url="/WEB-INF/pages/registration/individual.jsp" />	
	</div>
	<div class="registration" id="organizationRegistration">
		<c:import url="/WEB-INF/pages/registration/organization.jsp" />	
	</div>
	<c:import url="/WEB-INF/pages/footer.jsp" />
</body>
</html>