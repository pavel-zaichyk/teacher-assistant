BEGIN;

-- NOTIFICATION_SETTING
CREATE TABLE NOTIFICATION_SETTING (
  id     INTEGER PRIMARY KEY AUTOINCREMENT,
  type   TEXT,
  active INTEGER             DEFAULT 0,
  data   TEXT,
  volume DECIMAL(1, 1)       DEFAULT 1,
  sound  TEXT
);

-- STUDENT_NOTIFICATION
CREATE TABLE STUDENT_NOTIFICATION (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  student_id  INTEGER,
  active      INTEGER             DEFAULT 0,
  create_date TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
  description TEXT,
  FOREIGN KEY (student_id) REFERENCES STUDENT (id)
);

INSERT INTO NOTIFICATION_SETTING (id, type, active) VALUES (1, 'PRAEPOSTOR', 1);
INSERT INTO NOTIFICATION_SETTING (id, type, active, data) VALUES (2, 'ABSENCE', 1, 3);
INSERT INTO NOTIFICATION_SETTING (id, type, active) VALUES (3, 'STUDENT', 1);

COMMIT;
