function startTest() {
	createSubmissionAjax(function(data, status) {
		var submissionID = data.result.submissionDbId;
		var submissionDOM = submissionID.substring(0, 8);
		$("#log").append('<p id=' + submissionDOM + '></p>');
		submitSubmissionAjax(submissionID, function(data, status) {})
	});
}

function connect() {
	var socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/status', handleStatusMessage);
	});
}

function handleStatusMessage(data){
	var status = JSON.parse(data.body);
	var message = "";
	if (status.stage == 'NEW_SUBMISSION') {
		message = 'Sending samples to ' + status.vendorName;
	}else if (status.stage == 'WAITING_FOR_VENDOR') {
		message = status.vendorName + ' is working. Status: ' + status.status;
	}else if (status.stage == 'SENT_TO_GENOTYPE_DB') {
		message = 'Sending results to Genotype Database';
	}else if (status.stage == 'WAITING_FOR_GENOTYPE_DATABASE') {
		message = 'Genotype Database is working. Status: ' + status.status;
	}else if (status.stage == 'WAITING_FOR_USER') {
		message = status.errorMsg;
	}else if (status.stage == 'ERROR') {
		message = 'An error occured: ' + status.errorMsg;
	} else {
		message = status.stage + ' -> ' + status.status;
	}
	printLog(status.id.substring(0, 8), message);
}

function printLog(submissionDOM, message) {
	var mes = submissionDOM + ' - ' + message
	$("#" + submissionDOM).html(mes);
	// console.log(mes);
}

function updateVendorList() {
	var vendors_array = JSON.parse(sessionStorage.getItem("vendors_array"));
	var optionsHTML = '';
	for (var i = 0; i < vendors_array.length; i++) {
		var vendor = vendors_array[i];
		optionsHTML += '<option value="';
		optionsHTML += vendor.vendorDbId;
		optionsHTML += '">';
		optionsHTML += vendor.vendorName;
		optionsHTML += '</option>\n';
	}
	$("#vendor_select").html(optionsHTML);
	$("#geno_select").html(optionsHTML);
	updateVendorServiceList();
}

function updateVendorServiceList() {
	var vendors_array = JSON.parse(sessionStorage.getItem("vendors_array"));
	var optionsHTML = '';
	for (var i = 0; i < vendors_array.length; i++) {
		if (vendors_array[i].vendorDbId == $("#vendor_select").val()) {
			for (var j = 0; j < vendors_array[i].vendorServices.length; j++) {
				var service = vendors_array[i].vendorServices[j];
				optionsHTML += '<option value="';
				optionsHTML += service.serviceDbId;
				optionsHTML += '">';
				optionsHTML += service.serviceName;
				optionsHTML += '</option>\n';
			}
		}
	}
	$("#vendor_service_select").html(optionsHTML);
}

function getVendorsAjax(successCallback) {
	$.ajax({
		url : "api/vendors",
		context : document.body,
		contentType : "application/json",
		method : "GET",
		success : successCallback
	});
}

function createSubmissionAjax(successCallback) {
	var request = {
		"sampleGroupId" : "1A",
		"vendorDbId" : $("#vendor_select").val(),
		"vendorServiceDbId" : $("#vendor_service_select").val()
	}
	$.ajax({
		url : "api/submission",
		context : document.body,
		contentType : "application/json",
		data : JSON.stringify(request),
		method : "POST",
		success : successCallback
	});
}

function submitSubmissionAjax(submissionID, successCallback) {
	$.ajax({
		url : "api/submission/" + submissionID + "/submit",
		context : document.body,
		contentType : "application/json",
		data : "{}",
		method : "POST",
		success : successCallback
	});
}

function statusSubmissionAjax(submissionID, successCallback) {
	$.ajax({
		url : "api/submission/" + submissionID + "/status",
		context : document.body,
		contentType : "application/json",
		success : successCallback
	});
}


