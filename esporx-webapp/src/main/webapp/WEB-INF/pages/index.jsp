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

</body>
	<body>
		<div id="wrapper">
			<header>
				<hgroup>
					<canvas id="HTMLogo"></canvas>
					<div id="TEHLogo">
						<span id="Player1"></span>
						<span id="Player2"></span>
						<span id="MidLine"></span>
						<span id="Ball"></span>
					</div>
					<h1 id="LogoTitle">ESPORX.TV</h1>
					<h2 id="LogoBaseline">Premium Live Esports Broadcasts</h2>
				</hgroup>
			</header>
			<section>
				<h2 id="LaunchDate"><img src="${staticRoot}/landing/txtComingSoon.png" alt="Coming 2012" title="" /></h2>			</section>
			<aside>
				<ul class="ShareLinks">
					<li><a class="ShareFB" href="http://www.facebook.com/pages/Esporxtv/148704838534417" rel="external">Share on FACEBOOK</a></li>
					<li><a class="ShareTweet" href="http://twitter.com/#!/esporxtv" rel="external">TWEET US!</a></li>
				</ul>
			</aside>
			<footer></footer>
		</div>
		
		<script type="text/javascript">
		(function() {
            drawSquare("HTMLogo", 0, 0, 42, 132);
            drawSquare("HTMLogo", 86, 0, 12, 180);
            drawSquare("HTMLogo", 102, 72, 36, 36);
            drawSquare("HTMLogo", 142, 48, 42, 139);
		}());
		</script>
	</body>

</html>