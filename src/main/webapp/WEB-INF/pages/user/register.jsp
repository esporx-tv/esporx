<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
        <title><spring:message code="user.registration.page.title"
                               javaScriptEscape="true" /></title>

        <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
        <link rel="stylesheet" type="text/css" href="<c:url value="${staticRoot}/css/form.css" />" />
    </head>
    <body>
        <div class="container-fluid">
            <c:import url="/WEB-INF/pages/include/header.jsp" />
            <div class="offset3 span6">
                <form:form cssClass="form-horizontal" modelAttribute="userCommand">
                    <legend><spring:message code="user.registration.page.main_header" /></legend>

                    <div class="control-group">
                        <spring:message code="user.registration.page.email.placeholder" var="emailPlaceholder"/>
                        <form:label cssClass="control-label" path="email" title="${emailPlaceholder}">
                            <spring:message code="user.registration.page.email" />
                        </form:label>
                        <div class="controls controls-row">
                            <form:input path="email" id="email" placeholder="${emailPlaceholder}" />
                            <div class="help-inline alert-error pull-right"><form:errors path="email" /></div>
                        </div>
                    </div>

                    <div class="control-group">
                        <spring:message code="user.registration.page.password.placeholder"
                                        var="passwordPlaceholder"/>
                        <form:label cssClass="control-label" path="password" title="${passwordPlaceholder}">
                            <spring:message code="user.registration.page.password" />
                        </form:label>
                        <div class="controls controls-row">
                            <form:password path="password" placeholder="${passwordPlaceholder}" />
                            <div class="help-inline alert-error pull-right"><form:errors path="password" /></div>
                        </div>
                    </div>

                    <div class="control-group">
                        <spring:message code="user.registration.page.password.repeat.placeholder"
                                        var="passwordRepeatPlaceholder"/>
                        <form:label cssClass="control-label" path="passwordConfirmation" title="${passwordRepeatPlaceholder}">
                            <spring:message code="user.registration.page.password.repeat" />
                        </form:label>
                        <div class="controls controls-row">
                            <form:password path="passwordConfirmation" placeholder="${passwordRepeatPlaceholder}" />
                            <div class="help-inline alert-error pull-right"><form:errors path="passwordConfirmation" /></div>
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <a class="btn" href="<c:url value="/admin/login" />">Sign in</a>
                            <input class="btn btn-primary btn-large" type="submit" id="submit" value="<spring:message code="user.registration.submit" />" />
                        </div>
                    </div>
                </form:form>
            </div>
            <c:import url="/WEB-INF/pages/include/footer.jsp" />
        </div>

        <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
        <script type="text/javascript">
            require(['jquery', 'pages/user'], function($, user) {
                user.trigger();
                $(document.body).fadeTo('fast', 1);
            });
        </script>
    </body>
</html>