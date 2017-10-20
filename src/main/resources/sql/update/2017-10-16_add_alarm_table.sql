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
INSERT INTO ALARM (active, time, description) VALUES (1, 115, 'До окончания пары осталась 5 минут.');
INSERT INTO ALARM (active, time, description) VALUES (1, 119, 'До окончания пары осталась 1 минута.');

COMMIT;
