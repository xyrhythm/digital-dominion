var jsonType = "application/json";
var divId = "public";

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

var load = function() {
	if (this.status == 200) {
		console.log(this.response);
		var game = JSON.parse(this.response);
		console.log(typeof game);
	}

}

var url = window.location.href;
var path = window.location.pathname;
var pathInfo = path.split("/");

console.log(url);
console.log(path);
console.log(pathInfo);

var gameId = pathInfo[3];
var userName = pathInfo[5];

xhrGet("/info/game/".concat(gameId, "/public_cards"), load, jsonType);