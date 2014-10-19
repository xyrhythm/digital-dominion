// divs
var publicCardsDiv = "public";
var selfPlayerDiv = "self";
var otherPlayerDiv = "others";
var footerDiv = "footer";
var playingInfoDiv = "playingInfo";

// canvas
var actionCanvas = "actionCards";
var treasureCanvas = "treasureCards";
var trashCanvas = "trashCards";
var victoryCanvas = "victoryCards";
var selfCanvas = "selfPlayer";

// variables
var cardType = ["treasure", "victory", "action"];
var canvasType = [treasureCanvas, victoryCanvas, actionCanvas];
var playButtonId = "playButton";
var phaseButtonId = "phaseButton";

// path info
var path = window.location.pathname;
var pathInfo = path.split("/");
var gameId = pathInfo[3];
var userName = pathInfo[5];

// urls
var cardUrl = "/info/game/".concat(gameId, "/public_cards");
var gameUrl = "/info/game/".concat(gameId, "/stats");
var deckUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/deck"));
var handUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/hand"));
var discardUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/discard"));

// websocket 
var wsUrl = "ws://localhost:8080/event/"+gameId+"/"+userName;
console.log(wsUrl);
var ws = new WebSocket(wsUrl);

ws.onopen = function() {
    console.log("Opened!");
};

ws.onmessage = function (evt) {
	console.log(evt.data);
	var message = new Message(JSON.parse(evt.data));
	console.log(message);
	if (message.gameStatus === "start") {
		refreshPage();
	}
	setPhaseInfo(message.phaseName);
};

ws.onclose = function() {
    console.log("Closed!");
};

ws.onerror = function(err) {
    console.log("Error: " + err);
};



// help functions
function waitWsConnection(callback, interval) {
	if (ws.readyState === 1) {
        callback();
    } else {
        setTimeout(function () {
            waitWsConnection(callback);
        }, interval);
    }
}

function sendViaWs(message, callback) {
	waitWsConnection(function() {
		ws.send(message);
		if (typeof callback !== 'undefined') {
          callback();
        }
	}, 1000);
}

// game elements
var publicCards;
var playerHand;
var playerHandImgs;
var gamePhase = "none";
var selectedCard = "none";


// public cards div
setCanvasSize(actionCanvas, "action");
setCanvasSize(treasureCanvas, "treasure");
setCanvasSize(trashCanvas, "trash");
setCanvasSize(victoryCanvas, "victory");

var cardWidth = Math.floor(document.getElementById(actionCanvas).width / 10);
var cardHeight = Math.floor(document.getElementById(actionCanvas).height);

var publicCardsLoad = function() {
	if (this.status == 200) {
		var cards = JSON.parse(this.response);
		for (var i=0; i<cardType.length; i++) {
			var typeName = cardType[i];
			var cardsOneType = cards[typeName];
			for (var j=0; j<cardsOneType.length; j++) {
				var card = new Card(cardsOneType[j].card);
				card.draw(canvasType[i], j, cardWidth, cardHeight);
			}
		}

		// draw trash cards: TODO
		var canvas = document.getElementById(trashCanvas);
		var cntx = canvas.getContext("2d");
		cntx.textAlign = "center";
		cntx.font = '30pt Calibri';
      	cntx.fillText("Trash Pile", canvas.width / 2, canvas.height / 2);
	}
}

// self player div
setCanvasSize(selfCanvas, selfPlayerDiv);
var self_canvas = document.getElementById(selfCanvas);
var self_cntx = self_canvas.getContext("2d");
self_canvas.addEventListener("click", handClick, false);

function handClick(event) {
	var x = event.layerX;
    var y = event.layerY;
    var numCard = playerHandImgs.length;
    console.log(gamePhase);
    if (x > cardWidth && x < 4 * cardWidth && y > self_canvas.height - cardHeight) {
    	var idx = Math.min(Math.floor((x - cardWidth) / cardWidth * Math.max(1, (numCard-1)) / 2), numCard-1);
    	if (gamePhase === "action") {
    		selectedCard = playerHandImgs[idx].id;
    	}
    	if (gamePhase === "buy") {
    		selectedCard = playerHandImgs[idx].id;
    	}
    }
}

var deckLoad = function() {
	if (this.status == 200) {
		var deck = JSON.parse(this.response);
		var deckSize = deck["size"];
		var imageObj = new Image();
		imageObj.onload = function() {
        	self_cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, 0, self_canvas.height - cardHeight, cardWidth, cardHeight);
      	};
      	imageObj.src = cardBackImgSrc;
	}
}

var handLoad = function() {
	if (this.status == 200) {
		console.log(this.response);
		var hand = JSON.parse(this.response);
		playerHand = hand["cards"];
		var numCard = playerHand.length;
		playerHandImgs = [];
		var loaded = 0;
		var loadCallback = function () {			
			loaded++;
			if (loaded == numCard) {
			    for (var i = 0; i < numCard; i++) {
			    	var dx = (1 + 2 * i / Math.max(1, (numCard-1))) * cardWidth;
					var dy = self_canvas.height - cardHeight;
					console.log(dx, dy);
			    	self_cntx.drawImage(playerHandImgs[i], 0, 0, playerHandImgs[i].width, playerHandImgs[i].height, dx, dy, cardWidth, cardHeight);
			    }
			}
		};
		for (var i = 0; i < numCard; i++) {
		    playerHandImgs[i] = new Image();
		    playerHandImgs[i].addEventListener('load', loadCallback, false);
		    playerHandImgs[i].src = playerHand[i].img;
		    playerHandImgs[i].id = playerHand[i].name;
		}
	}
}

var discardLoad = function() {
	if (this.status == 200) {
		var discard = JSON.parse(this.response);
		console.log(discard);
		var discardSize = discard["size"];
		var imageObj = new Image();
		imageObj.onload = function() {
        	self_cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, 4 * cardWidth, self_canvas.height - cardHeight, cardWidth, cardHeight);
      	};
      	if (discardSize > 0) {
      		imageObj.src = discard[discardSize-1].img;
      	} else {
      		imageObj.src = blankCardImgSrc;
      	}
	}
}

// other player div

// footer div
var footerLoad = function() {
	if (this.status == 200) {
		console.log(this.response);
		var game = JSON.parse(this.response);
		var div = document.getElementById(footerDiv);
		var button = document.getElementById("startButton");
		div.innerHtml = "";
		if (button != null) div.removeChild(button);

		var element = document.createElement("input");
		element.type = "button";
		element.id = "startButton";
		if (game.active === false) {
			if (game.creator == userName) {
				element.value = "Start Game";
				element.onclick = function() {
					// this.disabled = true;
					// this.value = "Playing Game";
					var message = new Message();
					message.gameStatus = "init";
					sendViaWs(JSON.stringify(message));
				}
			} else {
				element.value = "Wait for Creator to Begine";
				element.disabled = true;
			}
		} else {
			element.value = "Playing Game";
			element.disabled = true;
		}
		div.appendChild(element);
	}
}

var gameLoad = function() {
	if (this.status == 200) {
		var game = JSON.parse(this.response);

		if (game.active === false) {
			xhrGet(gameUrl, footerLoad, jsonType);
		} else {
			xhrGet(cardUrl, publicCardsLoad, jsonType);
			xhrGet(deckUrl, deckLoad, jsonType);
			xhrGet(handUrl, handLoad, jsonType);
			xhrGet(discardUrl, discardLoad, jsonType);
			xhrGet(gameUrl, footerLoad, jsonType);
		}
	}
}

// TODO: add return to home button?


// Game logic
function refreshPage() {
	xhrGet(gameUrl, gameLoad, jsonType);
}

function setPhaseInfo(phaseInfo) {
	var div = document.getElementById(footerDiv);
	if (!document.getElementById(playButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = playButtonId;
		element.value = "Play";
		element.disabled = true;
		div.appendChild(element);

	}
	if (!document.getElementById(phaseButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = phaseButtonId;
		element.value = "Phase Info";
		element.disabled = true;
		div.appendChild(element);
	}
	
	var playButton = document.getElementById(playButtonId);
	var phaseButtopn = document.getElementById(phaseButtonId);
	playButton.onclick = playOnClick;

	gamePhase = phaseInfo;

	switch (phaseInfo) {
		case "action":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			phaseButtopn.value = "End Action Phase";
			phaseButtopn.onclick = actionPhaseOnClick;
			break;
		case "buy":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			phaseButtopn.value = "End Buy Phase";
			phaseButtopn.onclick = buyPhaseOnClick;
			break;
		case "cleanup":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			phaseButtopn.value = "End Automated Cleanup Phase";
			phaseButtopn.onclick = cleanupPhaseOnClick;
			break;
		case "none":
			playButton.disabled = true;
			phaseButtopn.disabled = true;
			phaseButtopn.value = "You are NOT in control YET!";
			break;
		default:
			playButton.disabled = true;
			phaseButtopn.disabled = true;
			gamePhase = "none";
			break;
	}
}

function playOnClick() {
	var message = new Message();
	message.cardName = selectedCard;
	sendViaWs(JSON.stringify(message), updateHand);
}

function updateHand() {
	sleep(function()
		{ xhrGet(handUrl, handLoad, jsonType); }
		, 1000);
}

function actionPhaseOnClick() {
	var message = new Message();
	message.phaseName = "buy";
	sendViaWs(JSON.stringify(message));
}

function buyPhaseOnClick() {
	var message = new Message();
	message.phaseName = "cleanup";
	sendViaWs(JSON.stringify(message));
}

function cleanupPhaseOnClick() {
	var message = new Message();
	message.phaseName = "none";
	sendViaWs(JSON.stringify(message));
}














xhrGet(gameUrl, gameLoad, jsonType);
setPhaseInfo("none");
