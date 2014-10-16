var ws = new WebSocket("ws://localhost:8080/event/1");

var url = window.location.href;
var path = window.location.pathname;
var pathInfo = path.split("/");

console.log(url);
console.log(path);
console.log(pathInfo);

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