function showResults(response) {
    var resultMessage = JSON.parse(response.body);
    var status = resultMessage.status;

    makeResultsPanel();

    if (status === 'COMPILE_FAILED') {
        showCompilationError(resultMessage);
    } else if (status === 'PASSED' || status === 'TESTS_FAILED') {
        showTestResults(resultMessage);
    } else {
        $("#results-panel")
                .addClass("alert alert-warning")
                .text("Something happened: " + status);
    }
}

function makeResultsPanel() {
    var results = $("#results");
    results.html(
            $("<div>")
            .addClass("col-md-6"));
    results.append($("<div>")
            .attr("id", "results-panel"));

    var panel = $("#results-panel");
    panel.append(
            $("<div>")
            .attr("id", "panel-heading")
            .addClass("panel-heading"));
    panel.append(
            $("<div>")
            .attr("id", "panel-body")
            .addClass("panel-body"));

}

function showCompilationError(resultMessage) {
    //The standard out contains a lot of unnecessary data
    //All the useful rows start with [javac], but some of
    //the [javac] rows are still unnecessary.
    var lines = resultMessage.stdout.split("\n");
    lines = lines.filter(line => line.includes("[javac] "))
            .map(line => line.replace("[javac] ", ""));

    $("#results-panel").addClass("panel panel-warning");
    $("#panel-heading")
            .text("Oh no! Your submission did not compile!");
    var panelBody = $("#panel-body");
    for (const line of lines) {
        panelBody.append(
                $("<div>")
                .text(line));
    }
}

function showTestResults(resultMessage) {
    if (resultMessage.success) {
        showSuccess();
        showButtonsAndUserFeedback();
    } else {
        showFailure(resultMessage);
    }
}

function showButtonsAndUserFeedback() {
    $("#selectByRandom").show();
    $("#selectFromList").show();
    $("#userFeedBack").show();
    $("#next-task-button").show();
}

function showSuccess() {
    $("#results-panel").addClass("panel panel-success");
    $("#panel-heading").text("Task cleared!");
    $("#panel-body").text("Well done! Head onto the next task!");
    $("#back-button").remove();
}

function showFailure(resultMessage) {
    $("#results-panel").addClass("panel panel-danger");
    var panelHeading = $("#panel-heading");

    if (resultMessage.type === 'TEST') {
        panelHeading
                .text("Hey! You're supposed to make a test that doesn't pass!");
    } else {
        panelHeading
                .text("Uh oh! Your submission didn't pass the tests!");
    }

    if (resultMessage.tests.length > 0) {
        for (i = 0; i < resultMessage.tests.length; i++) {
            showIndividualTestResult(resultMessage, i);
        }
    } else {
        $("#panel-body").text("No tests were found.");
    }
}

function showIndividualTestResult(resultMessage, i) {
    var res = resultMessage.tests[i];
    //The name field is formatted like below,
    //TestClass testMethod
    var names = res.name.split(" ");
    var className = names[0];
    var testName = names[1];
    var errorMessage = res.errorMessage;
    var backtrace = res.backtrace;
    var testId = "test" + i;
    var traceId = "backtrace" + i;

    var test = $("#panel-body").append(
            $("<div>")
            .attr("id", testId));

    test.append($("<div>")
            .text(testName + " (" + className + ")"));

    passedDiv = test.append($("<div>"));
    if (res.passed) {
        passedDiv.text("Passed");
    } else {
        passedDiv.text("Failed");
    }

    if (errorMessage) {
        test.append($("<div>")
                .text("Error message :" + errorMessage));
    }

    if (backtrace.length > 0) {
        test.append($("<br>"));
        var trace = test.append(
                $("<div>")
                .attr("id", traceId)
                .text("Stack trace: "));

        for (j = 0; j < backtrace.length; j++) {
            trace.append(
                    $("<div>")
                    .text(backtrace[j]));
        }
    }

    //If this isn't the last test result, add some space
    //before the next one
    if (i !== resultMessage.tests.length - 1) {
        test.append(
                $("<br>"));
        test.append(
                $("<br>"));
    }
}
