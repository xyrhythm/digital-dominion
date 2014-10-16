var jsonType = "application/json";
var divId = "public";
var actionCanvas = "actionCards";
var treasureCanvas = "treasureCards";
var trashCanvas = "trashCards";
var victoryCanvas = "victoryCards";
var cardType = ["treasure", "victory", "action"];
var canvasType = [treasureCanvas, victoryCanvas, actionCanvas];

// set canvas size
setCanvasSize(actionCanvas, "action");
setCanvasSize(treasureCanvas, "treasure");
setCanvasSize(trashCanvas, "trash");
setCanvasSize(victoryCanvas, "victory");

var path = window.location.pathname;
var pathInfo = path.split("/");
var gameId = pathInfo[3];
var userName = pathInfo[5];
var cardUrl = "/info/game/".concat(gameId, "/public_cards");

var load = function() {
	if (this.status == 200) {
		var cards = JSON.parse(this.response);
		var cardWidth = Math.floor(document.getElementById(actionCanvas).width / 10);
		var cardHeight = Math.floor(document.getElementById(actionCanvas).height);
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

xhrGet(cardUrl, load, jsonType);