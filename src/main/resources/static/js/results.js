var stompClient = null;

function connect() {
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/results', showResults);
    });
}

function showResults(results) {
    $("#results").html(results.body);
}
