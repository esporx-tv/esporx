<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="user.registration.page.title"
                           javaScriptEscape="true" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/user'], function($, user) {
            $(document).ready(function() {
                user.trigger();
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
            <spring:message code="user.registration.page.main_header" />
        </h1>

        <div id="catContent">
            <form:form modelAttribute="userCommand">
                <div class="input">
                    <spring:message code="user.registration.page.email.placeholder" var="emailPlaceholder"/>
                    <form:label path="email" title="${emailPlaceholder}">
                        <spring:message code="user.registration.page.email" />
                    </form:label>
                    <form:input path="email" id="email" placeholder="${emailPlaceholder}" />
                    <form:errors path="email" cssClass="errors" />
                </div>

                <div class="input">
                    <spring:message code="user.registration.page.password.placeholder"
                                    var="passwordPlaceholder"/>
                    <form:label path="password" title="${passwordPlaceholder}">
                        <spring:message code="user.registration.page.password" />
                    </form:label>
                    <form:password path="password" placeholder="${passwordPlaceholder}" />
                    <form:errors path="password" cssClass="errors" />
                </div>

                <div class="input">
                    <spring:message code="user.registration.page.password.repeat.placeholder"
                                    var="passwordRepeatPlaceholder"/>
                    <form:label path="passwordConfirmation" title="${passwordRepeatPlaceholder}">
                        <spring:message code="user.registration.page.password.repeat" />
                    </form:label>
                    <form:password path="passwordConfirmation" placeholder="${passwordRepeatPlaceholder}" />
                    <form:errors path="passwordConfirmation" cssClass="errors" />
                </div>

                <div class="submit">
                    <input type="submit" id="submit" value="<spring:message code="user.registration.submit" />" />
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>