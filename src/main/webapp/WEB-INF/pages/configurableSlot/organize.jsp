<%@ include file="../include/commonTaglib.jsp" %><!DOCTYPE html>
<html lang="en">
<head>
    <c:import url="/WEB-INF/pages/include/commonMeta.jsp" />
    <title><spring:message code="slot.submission.main_header" /></title>

    <c:import url="/WEB-INF/pages/include/commonStyle.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value="${staticRoot}/css/jquery.gridster.css" />" />
    <link rel="stylesheet" type="text/css" href="<c:url value="${staticRoot}/css/organize.css" />" />
    <c:import url="/WEB-INF/pages/include/commonScript.jsp" />
    <script type="text/javascript">
        require(['jquery', 'pages/slot'], function($, slot) {
            $(document).ready(function() {
                slot.trigger();
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
            <spring:message code="slot.organize.main_header" />
        </h1>
        <section class="demo" style="display:block">
            <div class="gridster">
                <ul>
                <c:forEach var="slot" items="${slots}">
                    <li data-row="<c:out value="${slot.ordinate}" />"
                        data-col="<c:out value="${slot.abscissa}" />"
                        data-sizex="<c:out value="${slot.width}" />"
                        data-sizey="1"
                        data-id="<c:out value="${slot.id}" />">
                        <c:out value="${slot.title}" /> [<c:out value="${slot.language}" />]
                    </li>
                </c:forEach>
                </ul>
            </div>
        </section>
        <p id="saveButton">Save this layout !</p>
    </div>
</div>
<c:import url="/WEB-INF/pages/include/footer.jsp" />
</body>
</html>