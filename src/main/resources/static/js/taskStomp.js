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
    stompClient.subscribe('/topic/javaparser/' + taskInstanceId, showErrors);
  });
}

var timer;
window.submissionEditor.getSession().on("change", () => {
  clearTimeout(timer);
  timer = setTimeout(sendCode, 850);
});

function sendCode() {
  $("#submit-button").attr("disabled", false);
  const code = $("#submission-code").val();
  stompClient.send(`/javaparser/${taskInstanceId}`, {}, code);
}

function showErrors(message) {
  if (!message.body) {
    return;
  }

  const errors = JSON.parse(message.body);

  $("#submit-button").attr("disabled", (errors.length > 0));
  window.submissionEditor.getSession().setAnnotations(errors);
}
