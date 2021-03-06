function getBinApiUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/bin";
}

function getBinSkuApiUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/bin/binSku";
}

function addBin(event) {
    var $form = $("#bin-form");
    var json = toJson($form);

    var bins = $("#binQuantity").val();
    var url = getBinApiUrl();

    $.ajax({
        url: url,
        type: 'POST',
        data: bins,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $('#binCreateModal').modal('toggle');
            getSuccessSnackbar("Success");
            displayNewBinInfo(response);
        },
        error: handleAjaxError
    });
    return false;
}

function processData() {
    var file = $('#binSkuFile')[0].files[0];
    readFileData(file, readFileDataCallback);
    return false;
}

function readFileDataCallback(results) {
    fileData = results.data;
    uploadRows();
}

function uploadRows() {
    var formList = [];
    var processCount;
    for (processCount = 0; processCount < fileData.length; processCount++) {
        formList.push(fileData[processCount]);
    }

    validateCsv(formList);
}

function validateCsv(formList) {

    var url = getBinSkuApiUrl() + "/validate";
    var json = JSON.stringify(formList);

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            uploadBinInventory(formList);
        },
        error: function(response) {
            sessionStorage.setItem("BinInventoryCSVError", JSON.stringify(JSON.parse(response.responseText)['message']));
            errorButtonActivate();
            alert("Errors in CSV");
        }
    });
    return false;
}

function uploadBinInventory(formList) {
    var url = getBinSkuApiUrl() + "/list";
    var json = JSON.stringify(formList);

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $('#binInventoryModal').modal('toggle');
            getSuccessSnackbar("Success");
        },
        error: function(response) {
            alert(response.responseText);
        }
    });
    return false;
}

function errorButtonActivate() {
    document.getElementById("download-errors").style.visibility = "visible";
    document.getElementById("download-errors").disabled = false;
}

function resetModal() {
    sessionStorage.setItem("BinInventoryCSVError", '');
    document.getElementById("errorDetails").style.display = "none";
    document.getElementById("download-errors").disabled = true;
    document.getElementById("download-errors").style.visibility = "hidden";
}

function displayNewBinInfo(response) {
    var $tbody = $('#new-bin-info-table').find('tbody');
    $tbody.empty();

    var arrayLength = response.length;
    for (var i = 0; i < arrayLength; i++) {
        console.log(i, response, response[i]);
        var row = '<tr>' +
            '<td style="text-align:center; font-weight: bold;">' + response[i] + '</td>' +
            '</tr>';
        $tbody.append(row);
    }

    $('#binIdModal').modal('show');
}

function getSearchBinInventoryList() {
    var $form = $("#search-param-form");
    var json = JSON.parse(toJson($form));

    if (json['binId'] == '' && json['globalSkuId'] == '') {
        alert('All search params cannot be empty');
        return false;
    }

    json['quantity'] = "0";
    json = JSON.stringify(json);
    var url = getBinSkuApiUrl() + '/search';

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            displayBinInventoryData(response);
            getSuccessSnackbar("Success");
        },
        error: handleAjaxError
    });
    return false;

}

function displayBinInventoryData(data) {
    var $tbody = $('#binSku-table').find('tbody');
    $tbody.empty();

    if (data.length == 0) {
        var row = '<tr>' +
            '<td style="text-align:center; font-weight: bold; background-color:#ffebe8;" colspan="4">No Items</td>' +
            '</tr>';
        $tbody.append(row);
    }
    console.log("Data: ", data);
    for (var i in data) {
        var e = data[i];
        var row = '<tr>' +
            '<td>' + e.binId + '</td>' +
            '<td>' + e.globalSkuId + '</td>' +
            '<td style="text-align:center;">' + e.quantity + '</td>' +
            '</tr>';
        console.log(row);
        $tbody.append(row);
    }

    document.getElementById("binSku-table").style.visibility = "visible";
    document.getElementById("refresh-data").disabled = false;
    getSuccessSnackbar("Success");
    return false;
}

function displayProductCsvErrors() {
    document.getElementById('errorDetails').innerHTML = sessionStorage.getItem("BinInventoryCSVError");
    document.getElementById("errorDetails").style.display = "block";
    return false;
}

function updateFileName() {
    var $file = $('#binSkuFile');
    var fileName = $file.val();
    $('#binSkuFileName').html(fileName);
}

function init() {
    $('#bin-form').submit(addBin);
    $('#binSku-form').submit(processData);
    $('#search-param-form').submit(getSearchBinInventoryList);
    $('#download-errors').click(displayProductCsvErrors);
    $('#refresh-data').click(getSearchBinInventoryList);
    $('#binSkuFile').on('change', updateFileName);

    document.getElementById("refresh-data").disabled = true;
    document.getElementById("download-errors").disabled = true;
}

$(document).ready(init);