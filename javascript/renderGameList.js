var load = function() {
	if (this.status === 200) {
		console.log(this.response);
		var games = JSON.parse(this.response);
		for (var i=0; i<games.length; i++) {
			var game = new Game(games[i]);
			game.list();
		}
	}
}

xhrGet(viewUrl, load, jsonType);