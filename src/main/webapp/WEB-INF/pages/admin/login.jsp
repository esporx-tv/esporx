<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="admin.page.title" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/admin.css" />"	type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/login'], function($, login) {
            $(document).ready(function() {
                login.focus("j_username");
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
        <div id="adminMain">
            <h3>Restricted area</h3>
            <c:if test="${not empty flash}">
                <p><strong><c:out value="${flash}" /></strong></p>
            </c:if>
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
                    <a href="<c:url value="/user/register" />">No account yet? Join now!</a>
					<input type="submit" value="Submit" />
				</div>
			</form>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />

    <div id="reason" style="height:0; width:0;">
        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
    </div>
</body>
</html>