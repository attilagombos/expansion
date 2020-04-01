
var ws;

function connect() {
    ws = new WebSocket('ws://localhost:8080/expansion/web');
    ws.onmessage = function(message){
        var gameState = JSON.parse(message.data);
        var boardState = gameState.boardState;
        var playerStates = gameState.playerStates;
        var instructions = gameState.instructions;

        $("#container").html(boardState.colors + boardState.layout + boardState.forces);
        $("#container").append(getPlayersTable(playerStates));
        $("#container").append(getInstructionsTable(instructions));
    }
}

function getInstructionsTable(instructions) {
    var instructionsTableBody = document.createElement("tbody");

    $.each(instructions, function (color, instruction) {
        console.log(color);

        $.each(instruction, function (index, step) {
            console.log(step);


        });
    });

    var instructionsTable = $("<table />");
    instructionsTable.append(instructionsTableBody);
    instructionsTable.className = "instructions";

    return instructionsTable;
}

function getPlayersTable(playerStates) {
    var playersTableBody = document.createElement("tbody");

    var headerNames = ["Name", "Territory", "Bases", "Mines", "Lands", "Forces", "Active", "Reinforcements"]

    var headerRow = playersTableBody.insertRow();

    $.each(headerNames, function (index, value) {
        var headerCell = document.createElement("th");
        headerCell.innerHTML = value;
        headerRow.appendChild(headerCell);
    });

    $.each(playerStates, function (index, value) {
        var row = playersTableBody.insertRow();
        row.className = value.color.toLowerCase();
        var cell = row.insertCell();
        cell.className = "name";
        cell.appendChild(document.createTextNode(value.name));
        cell = row.insertCell();
        cell.className = "territory";
        cell.appendChild(document.createTextNode(value.territory));
        cell = row.insertCell();
        cell.className = "bases";
        cell.appendChild(document.createTextNode(value.bases));
        cell = row.insertCell();
        cell.className = "mines";
        cell.appendChild(document.createTextNode(value.mines));
        cell = row.insertCell();
        cell.className = "lands";
        cell.appendChild(document.createTextNode(value.lands));
        cell = row.insertCell();
        cell.className = "forces";
        cell.appendChild(document.createTextNode(value.forces));
        cell = row.insertCell();
        cell.className = "active";
        cell.appendChild(document.createTextNode((value.forces - value.territory)));
        cell = row.insertCell();
        cell.className = "reinforcements";
        cell.appendChild(document.createTextNode("+" + value.reinforcements));
    });

    var playersTable = $("<table />");
    playersTable.append(playersTableBody);
    playersTable.className = "players";

    return playersTable;
}

