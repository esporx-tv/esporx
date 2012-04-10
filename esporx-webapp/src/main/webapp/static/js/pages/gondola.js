var alreadyrunflag=0; //flag to indicate whether target function has already been run

if (document.addEventListener) {
	document.addEventListener("DOMContentLoaded", function() { alreadyrunflag=1; gondolaLaunch();}, false);
} else if (document.all && !window.opera){
	document.write('<script type="text/javascript" id="contentloadtag" defer="defer" src="javascript:void(0)"><\/script>');
	var contentloadtag=document.getElementById("contentloadtag");

	contentloadtag.onreadystatechange=function(){
		if (this.readyState == "complete"){
	  		alreadyrunflag = 1;
	  		gondolaLaunch();
		}
	};
}

window.onload = function() {
  setTimeout( "if (!alreadyrunflag){ gondolaLaunch();}", 0);
};

var docWidth;
var slidesStyleWidth;
var slideStyleWidth;
var slideCurrent = 1;
var nbSlides;
var slideInterval = 6000;
var animSpeed = 50; //the lower the faster
var animState = 0;
var slideNext = true;
var slideTimer;
var slideTransTimer;

function gondolaLaunch() {
	nbSlides = document.getElementById("gSlide").getElementsByTagName("article");
	slidesStyleWidth =  nbSlides.length * 100 +"%";
	var clickPrev = document.getElementById("arrowLeft");
	var clickNext = document.getElementById("arrowRight");
				
	docWidth = document.getElementById("pageContent").offsetWidth;
	document.getElementById("gSlide").style.width = slidesStyleWidth; // set Gondole full width (included hidden slides) from the number of slides.
	slideStyleWidth = 100 / nbSlides.length +"%";
	for (i=0; i<nbSlides.length; i++) { nbSlides[i].style.width = slideStyleWidth;}
	slideTimer = setInterval( gondolAnim, slideInterval);
	
	clickPrev.onclick = clickNext.onclick = function() {
		if (animState == 0) {
			if (this == clickPrev) slideNext = false;
			clearInterval(slideTimer);
			slideTransTimer = setTimeout( gondolAnim, 0);
			slideTimer = setInterval( gondolAnim, slideInterval);
		}
	};
	
}

function gondolAnim() {
//	alert(slideCurrent +" "+ nbSlides.length +" "+ docWidth)
	
	if (slideNext) {
		if (slideCurrent == nbSlides.length) {
			slideCurrent = 1;
			document.getElementById("gSlide").style.marginLeft = "0";
		} else {
			slideTransTimer = setInterval( slideTransition, animSpeed);
		}
	} else {
		if (slideCurrent==1) {
			slideCurrent = nbSlides.length;
			var lastSlide = -(slideCurrent-1) * 100 + "%";
			document.getElementById("gSlide").style.marginLeft = lastSlide;
		} else {
			slideTransTimer = setInterval( slideTransition, animSpeed);
		}
	}
}

function slideTransition() {
	var slideStyleMarginleft;
	animState+=10;
	
	if (slideNext) {
		slideStyleMarginleft = -(((slideCurrent-1)*100) + animState) + "%";
	}
	else {
		slideStyleMarginleft = -(((slideCurrent-1)*100) - animState) + "%";
	}
		
	if (animState <= 100) { document.getElementById("gSlide").style.marginLeft = slideStyleMarginleft;}
	if (animState == 100) { 
		clearInterval(slideTransTimer);
		if (slideNext) {
			slideCurrent++;
		}
		else {
			slideCurrent--;
		}
		slideNext = true;
		animState = 0;
	}
	
	
}

