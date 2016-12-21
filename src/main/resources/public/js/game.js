
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/players");
webSocket.onopen = function() { sendMessage("REGISTER_PLAYER", {"playerName": "laur", "gameName": "jammailu"}) };

webSocket.onmessage = function (msg) { receiveData(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

function receiveData(msg) {
    console.log(msg)
}

function sendMessage(type, content) {
    console.log(content)
    var message =  {"type":type, "content":content}
    webSocket.send(JSON.stringify(message));
    console.log(message)
}
