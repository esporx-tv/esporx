<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
	<title><spring:message code="event.page.title"
                           arguments="${gameCommand.title}"
                           javaScriptEscape="true"
                           argumentSeparator="|" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="${staticRoot}/css/event.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery'], function($) {
            $(document).ready(function() {
                $(document.body).fadeTo('fast', 1);
            });
        });
    </script>
</head>
<body>
	<div id="pageContent">
		<c:import url="/WEB-INF/pages/include/header.jsp" />
		<div id="catMain">
			<h1 id="mainTitle">
				<spring:message code="game.submission.main_header" />
			</h1>
			<div id="catContent">
				<form:form modelAttribute="gameCommand">
                    <h2>Game</h2>

                    <div class="input">
                        <spring:message code="game.submission.title.placeholder"
                        var="titlePlaceholder" />
                        <form:label path="title" title="${titlePlaceholder}">
                            <spring:message code="game.submission.title" />
                        </form:label>
                        <form:input path="title" placeholder="${titlePlaceholder}" />
                        <form:errors path="title" cssClass="errors" />
                    </div>

                    <div class="input">
                        <spring:message code="game.submission.description.placeholder"
                            var="descriptionPlaceholder" />
                        <form:label path="description" title="${descriptionPlaceholder}">
                            <spring:message code="game.submission.description" />
                        </form:label>
                        <form:textarea path="description"  cssClass="ckeditor" />
                        <form:errors path="description" cssClass="errors" />
                    </div>

                    <div class="input">
                        <spring:message code="game.submission.icon.placeholder"
                            var="descriptionPlaceholder" />
                        <form:label path="iconUrl" title="${descriptionPlaceholder}">
                            <spring:message code="game.submission.icon" />
                        </form:label>
                        <form:input path="iconUrl" />
                        <form:errors path="iconUrl" cssClass="errors" />
                    </div>

					<div id="submit" class="submit">
						<input type="submit" value="<spring:message code="game.submission.submit" />" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
		<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>