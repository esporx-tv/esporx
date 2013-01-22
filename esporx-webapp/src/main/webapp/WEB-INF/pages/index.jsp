<%@ include file="include/commonTaglib.jsp" %>
<!DOCTYPE html>
<html>
	<head>
        <c:import url="./include/commonMeta.jsp" />
        <title>ESPORX.TV - Premium Live Esports Broadcasts</title>
		<meta name="robots" content="index,nofollow" />

        <link rel="stylesheet" media="screen" type="text/css" href="<c:url value="${staticRoot}/landing/landing.css"/>" />
        <link rel="stylesheet" media="screen" type="text/css" href="animations.css" />

        <c:import url="./include/commonScript.jsp" />
        <script type="text/javascript">
			function drawSquare(logoId, topLeftCornerX, topLeftCornerY, width, height){
				var canvas = document.getElementById(logoId);
				var context = canvas.getContext("2d");

				context.beginPath();
				context.rect(topLeftCornerX, topLeftCornerY, width, height);
				context.fillStyle = "#444";
				context.fill();
				context.lineWidth = 0;
			};
		</script>
	</head>

<body>
	<div id="wrapper">
		<aside>
			<ul class="ShareLinks">
				<li><a class="ShareFB" href="http://www.facebook.com/pages/Esporxtv/148704838534417" rel="external">Share on FACEBOOK</a></li>
				<li><a class="ShareTweet" href="http://twitter.com/#!/esporxtv" rel="external">TWEET US!</a></li>
			</ul>
		</aside>
	</div>
</body>

</html>