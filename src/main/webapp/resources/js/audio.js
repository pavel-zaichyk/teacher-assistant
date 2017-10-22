function playAudio(soundPath) {
	var audio = new Audio();
	audio.preload = 'auto';
	audio.src = soundPath;
	audio.play();
}
