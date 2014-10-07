var tableId = "currentGameList";

var Game {
	gameId : 0;
	creator : "";
	playerList : [];

	list : function() {
		var table = document.getElementById(tableId);
		var row = table.insertRow(-1);

		var c0 = row.insertCell(0);
		c0.innerHTML = gameId;

		var c1 = row.insertCell(1);
		c1.innerHTML = creator;

		var c2 = row.insertCell(2);
		c2.innerHTML = playerList;
	}
}