var jsonType = "application/json";
var divId = "footer";

// helper functions

var path = window.location.pathname;
var pathInfo = path.split("/");
console.log(path);
console.log(pathInfo);

var gameId = pathInfo[3];
var userName = pathInfo[5];

var gameUrl = "/info/game/".concat(gameId, "/stats");

var load = function() {
	if (this.status == 200) {
		console.log(this.response);
		var game = JSON.parse(this.response);
		var div = document.getElementById(divId);
		if (game.active == false && game.creator == userName) {
			var element = document.createElement("input");
			element.type = "button";
			element.value = "Start Game";
			element.onclick = function() {
				var xhr = new XMLHttpRequest();
				xhr.open("POST", gameUrl, true);
				xhr.onload = function() {
					xhrGet(gameUrl, load, jsonType);
				}
				xhr.send();
				window.location.reload();
			}
			div.appendChild(element);
		} else {
			div.innerHtml = "";
		}
	}
}

xhrGet(gameUrl, load, jsonType);

// TODO: add return to home button?
