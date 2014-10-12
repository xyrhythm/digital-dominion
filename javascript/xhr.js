function xhrGet(reqUri,callback,responseType) {
	var xhr = new XMLHttpRequest();
	xhr.responseType = typeof responseType !== 'undefined' ? responseType : '';

	xhr.open("GET", reqUri, true);
	xhr.onload = callback;

	xhr.send();
}