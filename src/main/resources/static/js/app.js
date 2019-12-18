var stompClient = null;
var socket = null;

function connect() {
    socket = new SockJS('/stomp');
    stompClient = Stomp.over(socket);
    stompClient.reconnect_delay = 3000;
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/temperature1', t1 => {
            var temperature1 = JSON.parse(t1.body);
            updateTemperature1(temperature1.value);
        });
        stompClient.subscribe('/topic/temperature2', t2 => {
            var temperature2 = JSON.parse(t2.body);
            updateTemperature2(temperature2.value);
        });
        stompClient.subscribe('/topic/set_temperature', st => {
            var setTemperature = JSON.parse(st.body);
            updateSetTemperature(setTemperature.value);
        });
        stompClient.subscribe('/topic/valve', v => {
            var valve = JSON.parse(v.body);
            updateValve(valve.value);
        });
    });
}

function updateTemperature1(value) {
    $("#temperature1").val(value);
}
function updateTemperature2(value) {
    $("#temperature2").val(value);
}
function updateSetTemperature(value) {
    $("#set_temperature").val(value);
}
function updateValve(value) {
    $("#valve").val(value);
}

$(function () {
    $(document).ready(() =>{
        connect();
    });
});
