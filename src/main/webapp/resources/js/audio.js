function playAudio(soundPath, volume) {
	var audio = new Audio();
	audio.preload = 'auto';
	audio.src = soundPath;
	audio.volume = volume;
	audio.type = "audio/mpeg";
	audio.play();
}

function playSound(sound) {
    playAudio(sound.data, sound.volume)
}
