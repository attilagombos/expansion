
var ws;

function connect() {
    ws = new WebSocket('ws://localhost:8080/expansion/web');
    ws.onmessage = function(message){
        var gameState = JSON.parse(message.data);
        var boardState = gameState.boardState;

        $("#container").html(boardState.colors + boardState.layout + boardState.forces);
    }
}

