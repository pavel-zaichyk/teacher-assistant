PrimeFaces.locales ['ru'] = {
	closeText: 'Закрыть',
	prevText: 'Назад',
	nextText: 'Вперёд',
	monthNames: ['Январь', 'Февраль' , 'Март' , 'Апрель' , 'Май' , 'Июнь' , 'Июль' , 'Август' , 'Сентябрь','Октябрь','Ноябрь','Декабрь' ],
	monthNamesShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек' ],
	dayNames: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Субота'],
	dayNamesShort: ['Воск','Пон' , 'Вт' , 'Ср' , 'Четв' , 'Пят' , 'Суб'],
	dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср ', 'Чт', 'Пт', 'Сб'],
	weekHeader: 'Неделя',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix:'',
	timeOnlyTitle: 'Только время',
	timeText: 'Время',
	hourText: 'Час',
	minuteText: 'Минута',
	secondText: 'Секунда',
	currentText: 'Сегодня',
	ampm: false,
	month: 'Месяц',
	week: 'Неделя',
	day: 'День',
	allDayText: 'Весь день'
};

function resizeDialogHeightByPercent(id, percent) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.height = Math.floor(window.innerHeight*(percent/100))+"px";
}

function resizeDialogWidthByPercent(id, percent) {
	var htmlTag = document.getElementById(id);
	htmlTag.style.width = Math.floor(window.innerWidth*(percent/100))+"px";
}

function resizeDialogByPercent(id, percent) {
	resizeDialogHeightByPercent(id, percent);
	resizeDialogWidthByPercent(id, percent);
}

function applySelectedCell(event) {
	var cell = $(event.target);

	var id = cell[0].parentElement.id;
	if (id == '' && cell[0].children.length > 0) {
		id = cell[0].children[0].id;
	}

	if (cell.closest('th').length > 0) {
		cell = cell.closest('th');
	} else {
		if (!cell.is('td')) {
			cell = cell.closest('td');
		}
	}
	var row = cell.closest('tr');
	var selectedCell = row.children().index(cell);
	console.log('selectedCell-' + selectedCell);

	var type = null;
	var lessonType = null;
	if (cell.is('td')) {
		if (cell.hasClass('student')) {
			type = 'STUDENT';
		}
		if (cell.hasClass('attestation')) {
			type = 'STUDENT_LESSON';
			id = cell[0].children[0].id;
			lessonType = 'ATTESTATION';
		}
		if (cell.hasClass('lesson')) {
			type = 'STUDENT_LESSON';
			id = cell[0].children[0].id;
			lessonType = 'OTHER';
		}
	}
	if (cell.is('th')) {
		if (cell.hasClass('attestation')) {
			type = 'LESSON';
			id = "lessonModeTable";
			lessonType = 'ATTESTATION';
		}
		if (cell.hasClass('lesson')) {
			type = 'LESSON';
			id = "lessonModeTable";
			lessonType = 'OTHER';
		}
	}

	// if (cell.closest('#lessonModeTable_scrollableTbody').length > 0) {
	// 	type = 'STUDENT_LESSON';
	// 	id = cell[0].children[0].id;
	// }
	// if (cell.closest('#lessonModeTable_frozenTbody').length > 0) {
	// 	type = 'STUDENT';
	// }
	// if (cell.closest('#lessonModeTable_scrollableThead').length > 0) {
	// 	type = 'LESSON';
	// 	id = "lessonModeTable";
	// }

	console.log("id - " + id);
	$('#selectedClientId').val(id);
	$('#selectedCell').val(selectedCell);
	$('#selectedType').val(type);
	$('#selectedLessonType').val(lessonType);

	//selectCell();
}
