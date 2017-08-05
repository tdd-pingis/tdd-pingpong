var stompClient = null;

function connect() {
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/results', showResults);
    });
}

function showResults(results) {
    var testOutput = JSON.parse(JSON.parse(results.body).testOutput)
    var status = testOutput.status;
    var results = testOutput.testResults;
    
    $("#results").html("<div>"+ status +"</div>");
    
    for(i = 0; i < results.length; i++) {
        var res = results[i]
        var name = res.name;
        var passed = res.passed;
        var points = res.points;
        var errorMessage = res.errorMessage;
        var backtrace = res.backtrace;
        
        $("#results").append("<div>Test name: "+ name +"</div>")
        $("#results").append("<div>Passed: "+ passed +"</div>")
        $("#results").append("<div>Points: "+ points +"</div>")
        $("#results").append("<div>Error message: "+ errorMessage +"</div>")
        
        var traceId = "backtrace"+i
        $("#results").append("<div id="+traceId+">Backtrace: </div>")
        for(j = 0; j < backtrace.length; j++) {
            $("#"+traceId).append("<div> "+ backtrace[j] +"</div>")
        }
    }
}
