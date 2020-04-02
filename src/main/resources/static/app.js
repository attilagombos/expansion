
var ws;

function connect() {
    ws = new WebSocket('ws://localhost:8080/expansion/web');
    ws.onmessage = function(message){
        var gameState = JSON.parse(message.data);
        var boardState = gameState.boardState;
        var playerStates = gameState.playerStates;
        var instructions = gameState.instructions;

        $("#container").html("");
        $("#container").append(boardState.colors + boardState.layout + boardState.forces);
        $("#container").append(getPlayersTable(playerStates));

        drawInstructions(instructions);
    }
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

function drawInstructions(instructions) {
    var canvas = $("#canvas").get(0);
    canvas.width = $('body').innerWidth();
    canvas.height = $('body').innerHeight();
    $(canvas).css('position', 'absolute');
    $(canvas).css('pointer-events', 'none');
    $(canvas).css('top', '0');
    $(canvas).css('left', '0');
    $(canvas).css('opacity', '1');
    var context = canvas.getContext('2d');
    context.clearRect(0, 0, canvas.width, canvas.height);

    $.each(instructions, function (color, instruction) {
        $.each(instruction.steps, function (index, step) {
            var target = step.target;
            if (step.type == "MOVE") {
                var source = step.source;
                drawArrowOnTable('table.forces:first', source.x, source.y, target.x, target.y, color.toLowerCase());
            } else if (step.type == "DEPLOY") {
                drawCircleOnTable('table.forces:first', target.x, target.y, color.toLowerCase());
            }
        });
    });
}

// gets the center of a table cell relative to the document
function getCellCenter(table, row, column) {
  var tableRow = $(table).find('tr')[$(table).find('tr').length - row - 1];
  var tableCell = $(tableRow).find('td')[column];

  var offset = $(tableCell).offset();
  var width = $(tableCell).innerWidth();
  var height = $(tableCell).innerHeight();

  return {
    x: offset.left + width / 2 + 1,
    y: offset.top + height / 2 + 2
  }
}

// draws an arrow on the document from the start to the end offsets
function drawArrow(start, end, color) {

    // get the canvas to draw the arrow on
    var canvas = $("#canvas").get(0);

    // get the drawing context
    var context = canvas.getContext('2d');
    context.fillStyle = color;
    context.strokeStyle = color;

    // draw pointer at end of line (needs rotation)
    var angle = Math.atan2(end.y - start.y, end.x - start.x);
    var length = Math.hypot(end.y - start.y, end.x - start.x);
    context.beginPath();
    context.translate(end.x, end.y);
    context.rotate(angle);
    context.moveTo(-length/4, 0);
    context.lineTo(-length/2, -5);
    context.lineTo(-length/2, 5);
    context.lineTo(-length/4, 0);
    context.fill();

    //draw line from start to end
    context.beginPath();
    context.moveTo(-length/2, 1);
    context.lineTo(-length/2, -1);
    context.lineTo(-length/2 - length/4, -3);
    context.lineTo(-length/2 - length/4, 3);
    context.lineTo(-length/2, 1);
    context.fill();

    // reset canvas context
    context.setTransform(1, 0, 0, 1, 0, 0);

    return canvas;
}

// finds the center of the start and end cells, and then calls drawArrow
function drawArrowOnTable(table, sourceColumn, sourceRow, targetColumn, targetRow, color) {
    var sourceCenter = getCellCenter($(table), sourceRow, sourceColumn);
    var targetCenter = getCellCenter($(table), targetRow, targetColumn);

    drawArrow(sourceCenter, targetCenter, color);
}

function drawCircle(end, color) {

    // get the canvas to draw the arrow on
    var canvas = $("#canvas").get(0);

    // get the drawing context
    var context = canvas.getContext('2d');
    context.fillStyle = color;
    context.strokeStyle = color;

    context.beginPath();
    context.arc(end.x, end.y, 10, 0, 2 * Math.PI, false);
    context.lineWidth = 1;
    context.stroke();

    // reset canvas context
    context.setTransform(1, 0, 0, 1, 0, 0);

    return canvas;
}

function drawCircleOnTable(table, targetColumn, targetRow, color) {
    var targetCenter = getCellCenter($(table), targetRow, targetColumn);

    drawCircle(targetCenter, color);
}
