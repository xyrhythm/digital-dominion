var url = window.location.href;
var path = window.location.pathname;
var pathInfo = path.split("/");

console.log(url);
console.log(path);
console.log(pathInfo);

var gameId = pathInfo[3];
var userName = pathInfo[5];
var eventUrl = "/event/game/".concat(gameId, "/public_cards");