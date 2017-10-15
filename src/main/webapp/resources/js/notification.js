Notification.requestPermission().then(function (result) {
	console.log(result);
});

function spawnNotification(theBody, theIcon, theTitle) {
	var options = {
		body: theBody,
		icon: theIcon
	};
	console.log("body = " + theBody);
	console.log("title = " + theTitle);
	var n = new Notification(theTitle, options);
	setTimeout(n.close.bind(n), 2000);
}
