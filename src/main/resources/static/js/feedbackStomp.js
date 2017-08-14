var stompClient = null;
//Requests the CSRF token and makes
//the connection after receiving a response
function connect() {
    jQuery.ajax({
        url: '/csrf',
        success: connectStomp
    });
}

function connectStomp(csrf) {
    var headerName = csrf.headerName;
    var token = csrf.token;

    var headers = {};
    headers[headerName] = token;

    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect(headers, function () {
        stompClient.subscribe('/topic/results', showResults);
    });
}
