// divs
var publicCardsDiv = "public";
var selfPlayerDiv = "self";
var otherPlayerDiv = "others";
var footerDiv = "footer";
var playInfoDiv = "playInfo";
var selfPlayDiv = "selfPlay";
var othersPlayDiv = "othersPlay";
var playAreaDiv = "playArea";

// canvas
var actionCanvas = "actionCards";
var treasureCanvas = "treasureCards";
var trashCanvas = "trashCards";
var victoryCanvas = "victoryCards";
var selfCanvas = "selfPlayer";

// text fields
var selectCardTextId = "selectCard";
// var playerScreenTextId = "playerScreen";
var curPlayerStatus = "curPlayer";

// variables
var cardType = ["treasure", "victory", "action"];
var canvasType = [treasureCanvas, victoryCanvas, actionCanvas];
var playButtonId = "playButton";
var phaseButtonId = "phaseButton";
var buyButtonId = "buyButton";
var discardButtonId = "discardButton";
var trashButtonId = "trashButton";
var gainButtonId = "gainButton";
var underAttack = false;

// path info
var path = window.location.pathname;
var pathInfo = path.split("/");
var gameId = pathInfo[3];
var userName = pathInfo[5];

// urls
var cardUrl = "/info/game/".concat(gameId, "/public_cards");
var gameUrl = "/info/game/".concat(gameId, "/stats");
var curPlayerUrl = "/info/game/".concat(gameId, "/cur_player");
var deckUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/deck"));
var handUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/hand"));
var discardUrl = "/info/game/".concat(gameId, "/usr/".concat(userName, "/discard"));

// websocket 
var wsUrl = "ws://localhost:8080/event/"+gameId+"/"+userName;
var ws = new WebSocket(wsUrl);

// game elements
var publicCards;
var playerHand;
var playerHandImgs;
var actionCardNames = [];
var gamePhase = "none";
var selectedCard = "none";
// for example, if a player in buy phase is ready to buy a card from the public
var playerAction = "";


ws.onopen = function() {
    console.log(wsUrl + " Opened!");
};

ws.onmessage = function (evt) {
	var message = new Message(JSON.parse(evt.data));
	console.log(message);

	if (message.type === "log") {
		if (message.logInfo) {
    		insertPlaylog(message.logInfo);
    	}
		if (message.command === "finish") {
		    setTimeout(function(){
    			var finish = confirm(message.logInfo);
    			if (finish == true) {
    				window.location.replace("http://localhost:8080");
    			}
    		}, 100);
    	}
	} else {
    	if (message.gameStatus === "start") {
    		refreshPage();
    	}
    
    	setPhaseInfo(message.phaseName);
    
    	if (message.logInfo) {
    		insertPlaylog(message.logInfo);
    	}
    
    	if (message.command) {
    		if (message.command === "update") {
    			updateHand();
    			updatePublic();
    			updateCurrentPlayer();
    		}
    		
    		if (message.command === "attack") {
    			// alert attack and ask for anti-attack
    			setTimeout(function(){
    				var antiAttack = confirm("Would like to play Anti-Attack?");
        			if (antiAttack == true) {
        				var antiMessage = new Message();
        				antiMessage.playResult = "anti";
        				sendViaWs(JSON.stringify(antiMessage));
        			} else {
        				underAttack = true;
        				setPhaseInfo(gamePhase);
        			}
    			}, 100);
    		}
    		if (message.command === "clear") {
    			setSelectCardInfo("");
    			playerAction = "";
    			setPhaseInfo(gamePhase);
    			// clear attack state
    			if (underAttack) {
    				underAttack = false;
        			setSelectCardInfo("You are off the hook.");
        			setPhaseInfo(gamePhase);
    			}
    			// clear wait playerAction
    			if (playerAction === "wait") {
    			    setSelectCardInfo("Continue...");
    			}
    		}
    	}
    
    	if (message.playResult) {
    		if (message.playResult != "success") {
    			setSelectCardInfo(message.playResult);
    		}
    	}
    	
    	if (message.playerAction) {
    		processPlayerAction(message.playerAction);
    	}
    }
	
};

ws.onclose = function() {
    console.log(wsUrl + " Closed!");
};

ws.onerror = function(err) {
    console.log(wsUrl + " Error: " + err);
};



// canvas setting up
// public cards div
setCanvasSize(actionCanvas, "action");
setCanvasSize(treasureCanvas, "treasure");
setCanvasSize(trashCanvas, "trash");
setCanvasSize(victoryCanvas, "victory");

var cardWidth = Math.floor(document.getElementById(actionCanvas).width / 10);
var cardHeight = Math.floor(document.getElementById(actionCanvas).height);

// self player div
setCanvasSize(selfCanvas, selfPlayerDiv);
var self_canvas = document.getElementById(selfCanvas);
var self_cntx = self_canvas.getContext("2d");

var action_canvas = document.getElementById(actionCanvas);
var treasure_canvas = document.getElementById(treasureCanvas);
var victory_canvas = document.getElementById(victoryCanvas);

// game logic
self_canvas.addEventListener("click", handClick, false);
action_canvas.addEventListener("click", actionClick, false);
treasure_canvas.addEventListener("click", treasureClick, false);
victory_canvas.addEventListener("click", victoryClick, false);

function handClick(event) {
	var x = event.layerX;
    var y = event.layerY;
    var numCard = playerHandImgs.length;
    if (x > cardWidth && x < 4 * cardWidth && y > self_canvas.height - cardHeight) {
    	var idx = Math.min(Math.floor((x - cardWidth) / cardWidth * Math.max(2, (numCard-1)) / 2), numCard-1);
    	if (gamePhase === "action") {
    		selectedCard = playerHandImgs[idx].id;
    		if (playerAction === "discard") {
				setSelectCardInfo("Selected Card to Discard: " + selectedCard + ". Press 'Play' Button to discard.");
			} else if (playerAction === "trash") {
				setSelectCardInfo("Selected Card to Trash: " + selectedCard + ". Press 'Play' Button to trash.");
			} else if (playerAction === "gain") {
				setSelectCardInfo("Please select a card from public cards to gain!");
				selectedCard = "";
			} else if (playerAction === "wait") {
				setSelectCardInfo("Wait for others' move!");
				selectedCard = "";
			} else {
        		if (isActionCard(selectedCard)) {
        			setSelectCardInfo("Selected Card to Play: " + selectedCard + ". Press 'Play' Button to play.");
        		} else {
        			setSelectCardInfo("In Action Phase: Please select an action card.");
        		}
        	}
    	}
    	if (gamePhase === "buy") {
    		selectedCard = playerHandImgs[idx].id;
    		if (playerAction == "" && isTreasureCard(selectedCard)) {
    			setSelectCardInfo("Selected Card to Play: " + selectedCard + ". Press 'Play' Button to play.");
    		} else if (playerAction === "gain") {
				setSelectCardInfo("Please select a card from public cards to gain!");
				selectedCard = "";
    		} else {
    			setSelectCardInfo("In Buy Phase: Please select a treasure card.");
    		}
    	}
    	if (gamePhase === "none" && underAttack) {
    		selectedCard = playerHandImgs[idx].id;
    		if (playerAction === "discard") {
				setSelectCardInfo("Selected Card to Discard: " + selectedCard + ". Press 'Play' Button to discard.");
			} else if (playerAction === "trash") {
				setSelectCardInfo("Selected Card to Trash: " + selectedCard + ". Press 'Play' Button to trash.");
			} else {
				setSelectCardInfo("Invalid move! ATTENTION please! You are under attack!");
        	}
    	}
    }
}

function actionClick(event) {
	var x = event.layerX;
    var y = event.layerY;
    var idx = Math.floor( x / cardWidth );
    if (gamePhase === "buy" && playerAction === "gain") {
    	selectedCard = actionCardNames[idx];
    	setSelectCardInfo("Selected Card to Buy: " + selectedCard + ". Press 'Play' Button to buy.");
    }
    if (gamePhase === "action" && playerAction === "gain") {
    	selectedCard = actionCardNames[idx];
    	setSelectCardInfo("Selected Card to Gain: " + selectedCard + ". Press 'Play' Button to gain.");
    }
}

function treasureClick(event) {
	var x = event.layerX;
    var y = event.layerY;
    var idx = Math.floor( x / cardWidth );
    if (gamePhase === "buy" && playerAction === "gain") {
    	if (idx === 0) selectedCard = "copper";
	    if (idx === 1) selectedCard = "silver";
	    if (idx === 2) selectedCard = "gold";
    	setSelectCardInfo("Selected Card to Buy: " + selectedCard + ". Press 'Play' Button to buy.");
    }
    if (gamePhase === "action" && playerAction === "gain") {
    	if (idx === 0) selectedCard = "copper";
	    if (idx === 1) selectedCard = "silver";
	    if (idx === 2) selectedCard = "gold";
    	setSelectCardInfo("Selected Card to Gain: " + selectedCard + ". Press 'Play' Button to gain.");
    }
}

function victoryClick(event) {
	var x = event.layerX;
    var y = event.layerY;
    var idx = Math.floor( x / cardWidth );
    if (gamePhase === "buy" && playerAction === "gain") {
	    if (idx === 0) selectedCard = "estate";
	    if (idx === 1) selectedCard = "duchy";
	    if (idx === 2) selectedCard = "province";
	    setSelectCardInfo("Selected Card to Buy: " + selectedCard + ". Press 'Play' Button to buy.");
	}
    if (gamePhase === "action" && playerAction === "gain") {
	    if (idx === 0) selectedCard = "estate";
	    if (idx === 1) selectedCard = "duchy";
	    if (idx === 2) selectedCard = "province";
	    setSelectCardInfo("Selected Card to Gain: " + selectedCard + ". Press 'Play' Button to gain.");
	}
}

function playOnClick() {
	var message = new Message();
	message.cardName = selectedCard;
	message.playerAction = playerAction;

	console.log(selectedCard, playerAction, gamePhase);

	if (gamePhase === "action") {
		if (playerAction === "discard" || playerAction === "trash" || playerAction === "gain") {
			sendViaWs(JSON.stringify(message));
		} else {
    		if (isActionCard(selectedCard)) {
    			sendViaWs(JSON.stringify(message), function(){
    				updateCurrentPlayer();}
    			);
    		} else {
    			setSelectCardInfo("In Action Phase: Please select an action card.");
    		}
    	}
	} else if (gamePhase === "buy") {
		if (playerAction === "gain") {
				sendViaWs(JSON.stringify(message), function(){
					updateCurrentPlayer();}
				);
		} else {
			if (isTreasureCard(selectedCard)) {
				sendViaWs(JSON.stringify(message));
			} else {
				setSelectCardInfo("In Buy Phase: Please select a treasure card.");
			}
		}
	} else if (gamePhase === "none" && underAttack) {
		if (playerAction === "discard" || playerAction === "trash") {
			sendViaWs(JSON.stringify(message));
		}
	} else {
		setSelectCardInfo("Invalid move!");
	}
	selectedCard = "";
}

// *********************************************************************
// load functions
var publicCardsLoad = function() {
	if (this.status == 200) {
		// console.log(this.response);
		var cards = JSON.parse(this.response);
		for (var i=0; i<cardType.length; i++) {
			var typeName = cardType[i];
			var cardsOneType = cards[typeName];
			for (var j=0; j<cardsOneType.length; j++) {
				var card = new Card(cardsOneType[j].card);
				card.draw(canvasType[i], j, cardWidth, cardHeight, cardsOneType[j].size);
				if (typeName === "action") {
					actionCardNames.push(card.name);
				}
			}
		}

		var canvas = document.getElementById(trashCanvas);
		var cntx = canvas.getContext("2d");
		var trashCards = cards["trash"];
		var imageObj = new Image();
		imageObj.onload = function() {
			cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, 0, 0, cardWidth, cardHeight);
      	};
      	if (trashCards.length > 0) {
      		imageObj.src = trashCards[trashCards.length-1].img;
      	} else {
      		imageObj.src = blankCardImgSrc;
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
		// console.log(this.response);
		var hand = JSON.parse(this.response);
		playerHand = hand["cards"];
		var numCard = playerHand.length;
		playerHandImgs = [];
		var loaded = 0;
		var loadCallback = function () {			
			loaded++;
			if (loaded == numCard) {
			    for (var i = 0; i < numCard; i++) {
			    	var dx = (1 + 2 * i / Math.max(2, (numCard-1))) * cardWidth;
					var dy = self_canvas.height - cardHeight;
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
		var cards = discard["cards"];
		var imageObj = new Image();
		imageObj.onload = function() {
        	self_cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, 4 * cardWidth, self_canvas.height - cardHeight, cardWidth*0.95, cardHeight);
      	};
      	if (cards.length > 0) {
      		imageObj.src = cards[cards.length-1].img;
      	} else {
      		imageObj.src = blankCardImgSrc;
      	}
	}
}

var curPlayerLoad = function() {
	if (this.status == 200) {
		var status = new PlayerStatus(JSON.parse(this.response));
		console.log(status);
		var div = document.getElementById(curPlayerStatus);
		div.innerHTML = "player: " + status.curPlayer
		    + "; phase: " + status.curPhase
			+ "; # action: " + status.numAction
			+ "; # buy: " + status.numBuy
			+ "; # treasure: " + status.numTreasure;
	}
}

var footerLoad = function() {
	if (this.status == 200) {
		// console.log(this.response);
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
			updateCurrentPlayer();
		}
	}
}

// ***************************************************************
// help functions
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
	if (!document.getElementById(buyButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = buyButtonId;
		element.value = "Ready to Buy?";
		element.disabled = true;
		div.appendChild(element);
	}
	if (!document.getElementById(discardButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = discardButtonId;
		element.value = "End discarding";
		element.disabled = true;
		div.appendChild(element);
	}
	if (!document.getElementById(trashButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = trashButtonId;
		element.value = "End trashing";
		element.disabled = true;
		div.appendChild(element);
	}
	if (!document.getElementById(gainButtonId)) {
		var element = document.createElement("input");
		element.type = "button";
		element.id = gainButtonId;
		element.value = "End gaining";
		element.disabled = true;
		div.appendChild(element);
	}
	
	var playButton = document.getElementById(playButtonId);
	var phaseButtopn = document.getElementById(phaseButtonId);
	var buyButton = document.getElementById(buyButtonId);
	var discardButton = document.getElementById(discardButtonId);
	var trashButton = document.getElementById(trashButtonId);
	var gainButton = document.getElementById(gainButtonId);

	playButton.onclick = playOnClick;
	
	buyButton.onclick = function() {
		playerAction = "gain";
		setPhaseInfo("buy");
	};
	
	var endAction = function() {
		var message = new Message();
		message.phaseName = gamePhase;
		message.playResult = "next";
		sendViaWs(JSON.stringify(message));
		
		if (underAttack) {
			message.playerAction = playerAction;
		} else {
			playerAction = "";
		}
			
		setPhaseInfo(gamePhase);
	};
	
	discardButton.onclick = endAction;
	
	trashButton.onclick = endAction;
	
	gainButton.onclick = endAction;
	
	gamePhase = phaseInfo;

	switch (phaseInfo) {
		case "action":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			buyButton.disabled = true;
			phaseButtopn.value = "End Action Phase";
			phaseButtopn.onclick = actionPhaseOnClick;
			if (playerAction === "discard") {
				discardButton.disabled = false;
			} else {
				discardButton.disabled = true;
			}
			if (playerAction === "trash") {
				trashButton.disabled = false;
			} else {
				trashButton.disabled = true;
			}
			if (playerAction === "gain") {
				gainButton.disabled = false;
			} else {
				gainButton.disabled = true;
			}
			if (playerAction === "wait") {
				playButton.disabled = true;
				phaseButtopn.disabled = true;
			}
			break;
		case "buy":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			phaseButtopn.value = "End Buy Phase";
			phaseButtopn.onclick = buyPhaseOnClick;
			if (playerAction === "gain") {
				buyButton.disabled = true;
			} else {
				buyButton.disabled = false;
			}
			break;
		case "cleanup":
			playButton.disabled = false;
			phaseButtopn.disabled = false;
			buyButton.disabled = true;
			phaseButtopn.value = "End Automated Cleanup Phase";
			phaseButtopn.onclick = cleanupPhaseOnClick;
			break;
		case "none":
			playButton.disabled = true;
			phaseButtopn.disabled = true;
			buyButton.disabled = true;
			phaseButtopn.value = "You are NOT in control YET!";
			if (underAttack) {
				if (playerAction === "discard") {
					discardButton.disabled = false;
				} else {
					discardButton.disabled = true;
				}
				if (playerAction === "trash") {
					trashButton.disabled = false;
				} else {
					trashButton.disabled = true;
				}
				phaseButtopn.value = "You are under ATTACK!";
				playButton.disabled = false;
			}
			break;
		default:
			playButton.disabled = true;
			phaseButtopn.disabled = true;
			buyButton.disabled = true;
			gamePhase = "none";
			if (underAttack) {
				playButton.disabled = false;
			}
			break;
	}
}

function updateHand() {
	self_cntx.clearRect(0, 0, self_canvas.width, self_canvas.height);
	xhrGet(deckUrl, deckLoad, jsonType);
	xhrGet(handUrl, handLoad, jsonType);
	xhrGet(discardUrl, discardLoad, jsonType);
}

function updatePublic() {
	xhrGet(cardUrl, publicCardsLoad, jsonType);
}

function actionPhaseOnClick() {
	var message = new Message();
	message.phaseName = "buy";
	sendViaWs(JSON.stringify(message));
	updateCurrentPlayer();
	selectedCard = "";
	playerAction = "";
}

function buyPhaseOnClick() {
	var message = new Message();
	message.phaseName = "cleanup";
	sendViaWs(JSON.stringify(message));
	updateCurrentPlayer();
	selectedCard = "";
	playerAction = "";
}

function cleanupPhaseOnClick() {
	var message = new Message();
	playerAction = "";
	message.phaseName = "none";
	sendViaWs(JSON.stringify(message));
	updateHand();
	setSelectCardInfo("Sit back and watch others playing");
	selectedCard = "";
	playerAction = "";
}

function updateCurrentPlayer(Info) {
	xhrGet(curPlayerUrl, curPlayerLoad, jsonType);
}

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
	console.log("sending message" + message);
	waitWsConnection(function() {
		ws.send(message);
		if (typeof callback !== 'undefined') {
          callback();
        }
	}, 1000);
}

function PlayerStatus(obj) {
	this.curPlayer = "";
	this.curPhase = "";
	this.numAction = 0;
	this.numBuy = 0;
	this.numTreasure = 0;

	for (var prop in obj) this[prop] = obj[prop];
}

function setSelectCardInfo(info) {
	document.getElementById(selectCardTextId).innerHTML = info;
}

// function setSelectCardInfo(info) {
// 	document.getElementById(playerScreenTextId).innerHTML = info;
// }

function insertPlaylog(info) {
	var div = document.getElementById(playAreaDiv);
	var p = document.createElement("p");
	p.innerHTML = info;
	div.appendChild(p);
}

function updateFooter() {
	xhrGet(gameUrl, footerLoad, jsonType);
}

function processPlayerAction(action) {
	var message = new Message();
	
    if (action === "draw") {
    	message.playResult = "next";
		sendViaWs(JSON.stringify(message), updateHand());
	}
    
	if (gamePhase === "action" && action === "gain") {
		playerAction = action;
		setSelectCardInfo("Please choose a card that you want...");
	}
    
	if (action === "discard") {
		playerAction = action;
		if (gamePhase === "action") {
			setPhaseInfo("action");
		}
		if (gamePhase === "none") {
			setPhaseInfo("none");
		}
		
		setSelectCardInfo("Please discard...");
	}
	
	if (action === "trash") {
		playerAction = action;
		setPhaseInfo("action");
		setSelectCardInfo("Please trash...");
	}
	
	if (action === "wait") {
		playerAction = action;
		setPhaseInfo("action");
		setSelectCardInfo("Wait for others' reaction");
		message.playResult = "next";
		sendViaWs(JSON.stringify(message));
	}
}
		
//
//
//
//

xhrGet(gameUrl, gameLoad, jsonType);
setPhaseInfo("none");

