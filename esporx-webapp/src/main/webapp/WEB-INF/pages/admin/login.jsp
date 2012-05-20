<%@ include file="../include/common.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>Restricted area</title>

	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/common.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/admin.css" />"	type="text/css" />

    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/logger.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/sanityChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/ext/modernizr.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/localStorageChecker.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/lib/externalLinkDetector.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/common.js" />"></script>
    <script type="text/javascript" src="<c:url value="${staticRoot}/js/pages/login.js" />"></script>
    
	<link rel="stylesheet/less" type="text/css" href="<c:url value="${staticRoot}/css/styles.less"/>">
    <script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/header.jsp" />
		<div id="adminMain">
            <h3>Restricted area</h3>
			<form name="adminForm" action="<c:url value="j_spring_security_check" />" method="POST">
                <div class="input">
                    <label>Login</label>
				    <input type="text" name="j_username" id="j_username" value="" placeholder="Enter your login..." />
                </div>
                <div class="input">
                    <label>Password</label>
				    <input type="password" name="j_password" placeholder="Shhh! your password..." />
                </div>
                <div class="submit">
					<input type="submit" value="Submit" />
				</div>
			</form>
		</div>
		<c:import url="/WEB-INF/pages/footer.jsp" />
	</div>
</body>
</html>