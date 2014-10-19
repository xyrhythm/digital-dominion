// common variables
var actionCanvas = "actionCards";
var treasureCanvas = "treasureCards";
var trashCanvas = "trashCards";
var victoryCanvas = "victoryCards";
var cardBackImgSrc = "/resource/img/cardBack.jpg";
var blankCardImgSrc = "/resource/img/blank.jpg";
var jsonType = "application/json";
var gameTableId = "game_view";
var viewUrl = "http://localhost:8080/view";

// common functions
function xhrGet(reqUri,callback,responseType) {
	var xhr = new XMLHttpRequest();
	var type = typeof responseType !== 'undefined' ? responseType : ''

	xhr.open("GET", reqUri, true);
	xhr.setRequestHeader("Content-type",type);
	xhr.onload = callback;

	xhr.send();
}

function setCanvasSize(canvasName, divName) {
	var canvas = document.getElementById(canvasName);
	canvas.width= document.getElementById(divName).offsetWidth;
	canvas.height = document.getElementById(divName).offsetHeight;
}

function genUrl(id) {
	var str1 = "<a href=/play/game/";
	var str2 = ">Join</a>";
	return str1.concat(id, str2);
}

function Game(obj) {
	this.id = 0;
	this.players = [];
	this.deck = "";
	this.creator = "";
	this.active = false;

	for (var prop in obj) this[prop] = obj[prop];
	this.list = function() {
		var table = document.getElementById(gameTableId);
		var row = table.insertRow(-1);

		var c0 = row.insertCell(0);
		c0.innerHTML = this.id;

		var c1 = row.insertCell(1);
		c1.innerHTML = this.creator;

		var c2 = row.insertCell(2);
		c2.innerHTML = this.players;

		var c3 = row.insertCell(3);
		c3.innerHTML = this.deck;

		var c4 = row.insertCell(4);
		if (this.active === true) {
			c4.innerHTML = "In Play";
		} else {
			c4.innerHTML = genUrl(this.id);
		}
	} 
}

function Card(obj) {
	this.name = "";
	this.cost = 0;
	this.img = "";
	this.desc = "";

	for (var prop in obj) this[prop] = obj[prop];

	this.draw = function(canvasName, idx, cardWidth, cardHeight) {
		var canvas = document.getElementById(canvasName);
		var cntx = canvas.getContext("2d");
		var dx = 0;
		var dy = 0;

		if (canvasName == actionCanvas) {
			dx = (idx % 10) * cardWidth;
			dy = Math.floor(idx / 10) * cardHeight;
		} else if (canvasName == trashCanvas) {
			dx = (idx % 4) * cardWidth;
			dy = Math.floor(idx / 4) * cardHeight;
		} else {
			dx = (idx % 3) * cardWidth;
			dy = Math.floor(idx / 3) * cardHeight;
		}

		var imageObj = new Image();
		imageObj.onload = function() {
        	cntx.drawImage(imageObj, 0, 0, imageObj.width, imageObj.height, dx, dy, cardWidth, cardHeight);
      	};
      	imageObj.src = this.img;
	} 
}

function Message(obj) {
	this.gameStatus = "";
	this.phaseName = "";
	this.cardName = "";

	for (var prop in obj) this[prop] = obj[prop];
}

function getCardXY(canvasName, idx, cardWidth, cardHeight) {

}

function sleep(callback, millis) {
    setTimeout(function()
            { callback(); }
    , millis);
}

