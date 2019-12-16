var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature', t => {
            var temperature = JSON.parse(t.body);
            updateTemperature(temperature.value);
        });
    });
}

function updateTemperature(value) {
    $("#temperature").text(value);
}

$(function () {
    $(document).ready(() =>{
        connect();
    });
});
