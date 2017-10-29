BEGIN;

-- ALARM
CREATE TABLE ALARM (
  id     INTEGER PRIMARY KEY AUTOINCREMENT,
  active INTEGER             DEFAULT 0,
  time   INTEGER,
  volume DECIMAL(1, 1),
  sound  TEXT
);

INSERT INTO ALARM (active, time, volume) VALUES (1, 0, 1);
INSERT INTO ALARM (active, time, volume) VALUES (1, 75, 1);
INSERT INTO ALARM (active, time, volume) VALUES (1, 79, 1);

COMMIT;
