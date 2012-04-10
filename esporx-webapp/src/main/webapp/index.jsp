<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="robots" content="index,nofollow" />
		<title>ESPORX.TV - Premium Live Esports Broadcasts</title>
		<meta name="description" content="ESPORX.TV is TEH Premium Live Esports Broadcasts Service platform. Coming live winter 2011."/>
		<meta name="keywords" content="ESPORX.TV, Premium, Live, Esports, Broadcasts, Esport, Broadcast, Starcraft, Game, Games, Store, Gamer, Gamers, Gaming, ProGaming, Panda"/>
		<link rel="stylesheet" media="screen" type="text/css" href="landing.css" />
<!--		<link rel="stylesheet" media="screen" type="text/css" href="animations.css" />  -->
		<script type="text/javascript" src="prototype.js"></script>

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
				<h2 id="LaunchDate"><img src="txtComingSoon.png" alt="Coming Winter 2011" title="" /></h2>			</section>
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
			document.observe('dom:loaded', function() {
				drawSquare("HTMLogo", 0, 0, 42, 132);
				drawSquare("HTMLogo", 86, 0, 12, 180);
				drawSquare("HTMLogo", 102, 72, 36, 36);
				drawSquare("HTMLogo", 142, 48, 42, 139);
				
				$$('a[rel="external"]').each(function(link){
					if(link.readAttribute('href') != '' && link.readAttribute('href') != '#'){
						link.writeAttribute('target','_blank');
					}
				});
			});
		}())
		</script>
	</body>

</html>