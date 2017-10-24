BEGIN;

-- ALARM
CREATE TABLE ALARM (
	id          INTEGER PRIMARY KEY AUTOINCREMENT,
	active      INTEGER DEFAULT 0,
	time        INTEGER,
	description TEXT,
	sound       TEXT
);

INSERT INTO ALARM (active, time, description) VALUES (1, 0, 'Начало пары.');
INSERT INTO ALARM (active, time, description) VALUES (1, 75, 'До окончания пары осталось 5 минут.');
INSERT INTO ALARM (active, time, description) VALUES (1, 79, 'До окончания пары осталось 1 минута.');

COMMIT;
