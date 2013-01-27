<%@ include file="commonTaglib.jsp"%>
<!--[if lt IE 9]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- /HTML5 Navigators Compatibility Settings -->
<script type="text/javascript" data-main="<c:url value="${staticRoot}/js/main" />" src="<c:url value="${staticRoot}/js/ext/require-jquery.js" />"></script>
<script type="text/javascript">
    // global configuration
    jQuery.extend({
        withBaseImgUrl : function(url) {
            return "<c:url value="${staticRoot}/img" />/" + url;
        }
    });

    // loader configuration
    require.config({
        baseUrl : "<c:url value="${staticRoot}/js" />",
        map : {
            '*' : {
                'text' : 'ext/text',
                'css' : '../css',
                'tpl' : '../tpl'
            }
        },
        paths : {
            jqueryui: 'ext/jquery-ui-custom',
            underscore: 'ext/underscore'
        }
    });
</script>
<c:if test="${disableAnalytics != true}">
        <script type="text/javascript">
          var _gaq = _gaq || [];
          _gaq.push(['_setAccount', 'UA-36250971-1']);
          _gaq.push(['_trackPageview']);

          (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
          })();

        </script>
</c:if>