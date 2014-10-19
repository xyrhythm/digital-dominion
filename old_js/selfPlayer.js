var jsonType = "application/json";
var divId = "self";
var actionCanvas = "actionCards";
var cardWidth = Math.floor(document.getElementById(actionCanvas).width / 10);
var cardHeight = Math.floor(document.getElementById(actionCanvas).height);
var selfCanvas = "selfPlayer";

setCanvasSize(selfCanvas, divId);

var canvas = document.getElementById(selfCanvas);
var cntx = canvas.getContext("2d");

// helper function
var drawCard = function() {
	

}

var deckLoad = function() {
	if (this.status == 200) {
		var deck = JSON.parse(this.response);
		var deckSize = deck["size"];
		var imageObj = new Image();
		imageObj.onload = function() {
        	cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, 0, canvas.height - cardHeight, cardWidth, cardHeight);
      	};
      	imageObj.src = cardBackImgSrc;
	}
}

var handLoad = function() {
	if (this.status == 200) {
		console.log(this.response);
		var hand = JSON.parse(this.response);
		var cards = hand["cards"];
		var numCard = cards.length;
		var imgs = [];
		var loaded = 0;
		var loadCallback = function () {
			loaded++;
			if (loaded == imgSrcs.length) {
			    for (var i = 0; i < numCard; i++) {
			    	
			    }
			}
		};

		for (var i = 0; i < numCard; i++) {
			var card = new Card(cards[i]);
			var dx = (1 + i / Math.max(1, (numCard-1))) * cardWidth;
			var dy = canvas.height - cardHeight;
			console.log(dx, dy);



			
		}

		for (var i = 0; i < numCard; i++) {
		    imgs[i] = new Image();
		    imgs[i].addEventListener('load', loadCallback, false);
		    imgs[i].src = imgSrcs[i];
		}

			var cntx = document.getElementById(selfCanvas).getContext("2d");
			var imageObj = new Image();
			(function(dx, card, imageObj) {
				imageObj.onload = function() {
					imgCount++;
					if (imgCount == numCard) {
	  		      		cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, dx, dy, cardWidth, cardHeight);
	  		      	}
  		 		};
  	     		imageObj.src = card.img;
  	     	}) (dx, card, imageObj);
		}
	}
}

var url = window.location.href;
var path = window.location.pathname;
var gameId = pathInfo[3];
var userName = pathInfo[5];

console.log(url);
console.log(path);

var deckUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/deck"));
var handUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/hand"));

xhrGet(deckUrl, deckLoad, jsonType);
xhrGet(handUrl, handLoad, jsonType);