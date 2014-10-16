var tableId = "game_view";
var msgId = "game_view_msg";
var jsonType = "application/json";

var load = function() {
	if (this.status == 200) {
		var game = new Game(JSON.parse(this.response));
		game.list();
	}
}

var url = window.location.href;
var path = window.location.pathname;

xhrGet(url, load, jsonType);