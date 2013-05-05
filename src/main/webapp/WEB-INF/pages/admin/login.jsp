<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="admin.page.title" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
</head>
<body>
    <div class="container-fluid">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
        <div class="offset3 span6">
            <c:if test="${not empty flash}">
                <p class="well"><strong><c:out value="${flash}" /></strong></p>
            </c:if>
			<form class="form-horizontal" name="adminForm" action="<c:url value="j_spring_security_check" />" method="POST">
                <legend>Restricted area</legend>
                <div class="control-group">
                    <label class="control-label">Login</label>
                    <div class="controls">
                        <input type="text" name="j_username" id="j_username" value="" placeholder="Enter your login..." />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Password</label>
                    <div class="controls">
                        <input type="password" name="j_password" placeholder="Shhh! your password..." />
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <a class="btn" href="<c:url value="/user/register" />">Sign up</a>
                        <input class="btn btn-primary btn-large" type="submit" value="Submit" />
                    </div>
				</div>
			</form>
		</div>
        <c:import url="/WEB-INF/pages/include/footer.jsp" />
	</div>

    <div id="reason" style="height:0; width:0;">
        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
    </div>

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/login'], function($, login) {
            login.focus("#j_username");
            $(document.body).fadeTo('fast', 1);
        });
    </script>
</body>
</html>