COMMIT;
BEGIN;

DROP TABLE IF EXISTS NOTE_TYPE;

DROP TABLE IF EXISTS NOTE;


CREATE TABLE NOTE (
	id          INTEGER PRIMARY KEY AUTOINCREMENT,
	lecturer_id INTEGER,
	type        TEXT,
	entity_id   INTEGER,
	description TEXT,
	create_date TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
	FOREIGN KEY (lecturer_id) REFERENCES LECTURER (id)
);

COMMIT;
