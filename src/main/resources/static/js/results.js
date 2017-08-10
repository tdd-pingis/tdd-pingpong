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

function showResults(response) {
    var testOutput = JSON.parse(response.body);

    var status = testOutput.status;
    var testResults = testOutput.testResults;

    var results = $("#results");
    results.html(
            $("<div>")
            .addClass("col-md-6"));
    results.append($("<div>")
            .attr("id", "panel")
            .addClass("panel panel-default"));

    var panel = $("#panel")
    panel.append(
            resultDiv(status));
    panel.append(
            $("<div>")
            .attr("id", "panel-body")
            .addClass("panel-body"));

    var panelBody = $("#panel-body")

    for (i = 0; i < testResults.length; i++) {
        
        var res = testResults[i];
        var name = res.name;
        var passed = res.passed;
        var points = res.points;
        var errorMessage = res.errorMessage;
        var backtrace = res.backtrace;
        var testId = "test" + i;
        var traceId = "backtrace" + i;

        panelBody.append(
                $("<div>")
                .attr("id", testId))

        var test = $("#"+testId);
        test.append($("<div>")
                .text("Test name: " + name));
        test.append($("<div>")
                .text("Passed: " + passed));
        test.append($("<div>")
                .text("Points: " + points));
        test.append($("<div>")
                .text("Error message :" + errorMessage));

        test.append(
                $("<div>")
                .attr("id", traceId)
                .text("Backtrace: "));

        var trace = $("#" + traceId);
        for (j = 0; j < backtrace.length; j++) {
            trace.append(
                    $("<div>")
                    .text(backtrace[j]));
        }
        
        test.append(
                $("<br>"));
    }
}

//Possible statuses are: PASSED, TESTS_FAILED, COMPILE_FAILED, TESTRUN_INTERRUPTED
//See fi.helsinki.cs.tmc.langs.domain.RunResult at testmycode/tmc-langs for more info
function resultDiv(status) {
    var resultDiv = $("<div>");
    
    if (status === 'PASSED') {
        return resultDiv
                .addClass("alert alert-success")
                .text("Success!!");
        
    } else if (status === 'TESTS_FAILED') {
        return resultDiv
                .addClass("alert alert-danger")
                .text("Some tests failed");
        
    } else {
        return resultDiv
                .addClass("alert alert-warning")
                .text("Something happened: "+ status);
    }
}
