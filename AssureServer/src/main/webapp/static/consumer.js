function getConsumerApiUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/party";
}

function addConsumer(event) {
    var $form = $("#consumer-form");
    var json = toJson($form);
    var url = getConsumerApiUrl();

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {'Content-Type': 'application/json'},
        success: function(response) {
            $('#exampleModal').modal('toggle');
            getSuccessSnackbar("Success");
        },
        error: handleAjaxError
    });
    return false;
}

function getSearchConsumerList() {
    var $form = $("#search-param-form");
    var json = JSON.parse(toJson($form));
    var url = getConsumerApiUrl() + '/' + json['type'];

    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayConsumerList(data);
        },
        error: handleAjaxError
    });
    return false;
}

function displayConsumerList(data) {
    var $tbody = $('#consumer-table').find('tbody');
    $tbody.empty();

    if (data.length == 0) {
        var row = '<tr>' +
            '<td style="text-align:center; font-weight: bold; background-color:#ffebe8;" colspan="3">No Parties Created</td>' +
            '</tr>';
        $tbody.append(row);
    }

    for (var i in data) {
        var e = data[i];
        var row = '<tr>' +
            '<td style="text-align:center; font-weight: bold;">' + e.id + '</td>' +
            '<td>' + e.name + '</td>' +
            '</tr>';
        $tbody.append(row);
    }

    document.getElementById("consumer-table").style.visibility = "visible";
    document.getElementById("refresh-data").disabled = false;
    getSuccessSnackbar("Success");
    return false;
}

function init() {
    $('#consumer-form').submit(addConsumer);
    $('#refresh-data').click(getSearchConsumerList);
    $('#search-param-form').submit(getSearchConsumerList);
}

$(document).ready(init);