<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="user.profile.page.title"
                           javaScriptEscape="true" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />

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
            <spring:message code="user.profile.page.main_header" />
        </h1>

        <div id="catContent">
            <c:if test="${not empty flash}">
                <p><c:out value="${flash}" /></p>
            </c:if>
            <form:form modelAttribute="userCommand">
                <div class="input" style="display:none">
                    <form:hidden path="email" id="email" />
                    <form:errors path="email" cssClass="errors" />
                    <%-- in case of usurpation attempt --%>
                    <c:if test="${not empty err_email}">
                        <div class="errors"><c:out value="${err_email}" /></div>
                    </c:if>
                </div>

                <div class="input">
                    <spring:message code="user.profile.page.old_password" var="oldPasswordPlaceholder"/>
                    <label title="${oldPasswordPlaceholder}">
                        <spring:message code="user.profile.page.old_password" />
                    </label>
                    <input type="password" name="oldPassword" id="oldPassword" placeholder="${oldPasswordPlaceholder}" />
                    <c:if test="${not empty err_oldPassword}">
                        <div class="errors"><c:out value="${err_oldPassword}" /></div>
                    </c:if>
                </div>

                <div class="input">
                    <spring:message code="user.registration.page.password.placeholder"
                                    var="passwordPlaceholder"/>
                    <form:label path="password" title="${passwordPlaceholder}">
                        <spring:message code="user.profile.page.new_password" />
                    </form:label>
                    <form:password path="password" placeholder="${passwordPlaceholder}" />
                    <form:errors path="password" cssClass="errors" />
                </div>

                <div class="input">
                    <spring:message code="user.registration.page.password.repeat.placeholder"
                                    var="passwordRepeatPlaceholder"/>
                    <form:label path="passwordConfirmation" title="${passwordRepeatPlaceholder}">
                        <spring:message code="user.profile.page.new_password_bis" />
                    </form:label>
                    <form:password path="passwordConfirmation" placeholder="${passwordRepeatPlaceholder}" />
                    <form:errors path="passwordConfirmation" cssClass="errors" />
                </div>

                <div class="submit">
                    <input type="submit" id="submit" value="<spring:message code="user.profile.page.submit" />" />
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>