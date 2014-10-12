var tableId = "game_view";
var msgId = "game_view_msg";
var jsonType = "application/json";

// helper functions
function xhrGet(reqUri,callback,responseType) {
	var xhr = new XMLHttpRequest();
	var type = typeof responseType !== 'undefined' ? responseType : ''

	xhr.open("GET", reqUri, true);
	xhr.setRequestHeader("Content-type",type);
	xhr.onload = callback;

	xhr.send();
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

	for (var prop in obj) this[prop] = obj[prop];
	this.list = function() {
		var table = document.getElementById(tableId);
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
		c4.innerHTML = genUrl(this.id);
	} 
}

var load = function() {
	if (this.status == 200) {
		console.log(this.response);
		var game = new Game(JSON.parse(this.response));
		console.log(typeof game);
		game.list();
	}

}

var url = window.location.href;
var path = window.location.pathname;

console.log(url);
console.log(path);

xhrGet(url, load, jsonType);