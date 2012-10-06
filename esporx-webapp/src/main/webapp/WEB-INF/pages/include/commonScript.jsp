<%@ include file="commonTaglib.jsp"%>
<!--[if lt IE 9]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- /HTML5 Navigators Compatibility Settings -->
<script src="<c:url value="${staticRoot}/js/ext/less.js"/>" type="text/javascript"></script>
<script type="text/javascript" data-main="<c:url value="${staticRoot}/js/main" />" src="<c:url value="${staticRoot}/js/ext/requirejs.js" />"></script>
<script type="text/javascript">
    require.config({
        baseUrl : "<c:url value="${staticRoot}/js" />",
        map : {
            '*' : {
                'text' : 'ext/text',
                'css' : '../css',
                'tpl' : '../tpl'
            }
        }
    });
</script>