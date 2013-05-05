<%@ include file="commonTaglib.jsp"%>

<div id="hottest" class="carousel slide">
    <%-- slides --%>
    <ol class="carousel-indicators">
        <c:forEach var="slide" items="${gondolaSlides}" varStatus="loop">
            <li <c:if test="${loop.index == 0}">class="active"</c:if>
                    data-target="#hottest" data-slide-to="${1+loop.index}"></li>
        </c:forEach>
    </ol>

    <%-- carousel contents --%>
    <div class="carousel-inner">
        <c:forEach var="slide" items="${gondolaSlides}" varStatus="loop">
            <div class="item<c:if test='${loop.index == 0}'> active</c:if>">
                <!--<a href="<c:url value="${slide.link}" />">-->
                <img src="${slide.picture}" />
                <!--</a>-->
                <div class='carousel-caption'>
                    <h4>${slide.tagLine}</h4>
                    <p><c:out value='${slide.description}' escapeXml='false' /></p>
                </div>
            </div>
        </c:forEach>
    </div>

    <%-- carousel navigation --%>
    <a class="carousel-control left" href="#hottest" data-slide="prev">&lsaquo;</a>
    <a class="carousel-control right" href="#hottest" data-slide="next">&rsaquo;</a>
</div>