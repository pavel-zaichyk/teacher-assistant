Notification.requestPermission().then(function (result) {
	console.log(result);
});

function spawnNotification(notification) {
	var options = {
		body: notification.body,
		icon: notification.image
	};
	var n = new Notification(notification.title, options);
	setTimeout(n.close.bind(n), notification.timeout);
}
