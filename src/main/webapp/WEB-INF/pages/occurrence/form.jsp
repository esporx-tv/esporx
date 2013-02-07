<%@ include file="../include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />

    <title><spring:message code="event.page.title" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/datepicker.css" />" type="text/css" />
    <link rel="stylesheet" href="<c:url value="${staticRoot}/css/occurrence.css" />" type="text/css" />

    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/occurrence', 'jqueryui', "ext/noty/jquery.noty"], function($, occurrence) {
            $(document).ready(function() {
                occurrence.trigger();
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
            <spring:message code="event.submission.main_header" />
        </h1>
        <div id="catContent">
            <form id="occurrenceCommand" method="POST">
                <h2>Select an event</h2>

                <div class="input">
                    <label for="event">
                        Event
                    </label>
                    <select id="event">
                        <option value="">Select an event...</option>
                        <c:forEach items="${events}" var="event">
                            <option value="${event.id}"><c:out value="${event.title}"></c:out></option>
                        </c:forEach>
                   </select>
                </div>

                <h2>Planning</h2>
                <span id="add_occurrence">Add a new occurrence</span>

                <div id="submit" class="submit">
                    <input type="submit" value="Submit" />
                </div>
            </form>
        </div>
    </div>
</div>
    <c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>