<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title>Restricted area</title>

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
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>