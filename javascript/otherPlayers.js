var jsonType = "application/json";
var divId = "others";


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
		var game = new Game(JSON.parse(this.response));
		console.log(typeof game);
		game.list();
	}

}

var url = window.location.href;
var path = window.location.pathname;

console.log(url);
console.log(path);

// xhrGet(url, load, jsonType);