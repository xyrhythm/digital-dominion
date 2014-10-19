var jsonType = "application/json";
var divId = "footer";
var path = window.location.pathname;
var pathInfo = path.split("/");
var gameId = pathInfo[3];
var userName = pathInfo[5];
var wsUrl = "ws://localhosr:8080/event/".concat(gameId);
var ws = new WebSocket(wsUrl);

ws.onopen = function() {
    console.log("Opened!");
    ws.send("Hello Server");
};

ws.onmessage = function (evt) {
	console.log(evet.data);
};

ws.onclose = function() {
    console.log("Closed!");
};

ws.onerror = function(err) {
    console.log("Error: " + err);
};


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
				// var xhr = new XMLHttpRequest();
				// xhr.open("POST", gameUrl, true);
				// xhr.onload = function() {
				// 	xhrGet(gameUrl, load, jsonType);
				// }
				// xhr.send();
				//window.location.reload();
				ws.send("start");
				div.innerHtml = "";
			}
			div.appendChild(element);
		} else {
			div.innerHtml = "";
		}
	}
}

xhrGet(gameUrl, load, jsonType);

// TODO: add return to home button?
