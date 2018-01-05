COMMIT;
BEGIN;

-- 1 LECTURER
CREATE TABLE LECTURER (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    card_uid   TEXT,
    card_id    INTEGER,
    first_name TEXT,
    last_name  TEXT,
    patronymic TEXT,
    phone      TEXT,
    email      TEXT,
    image      BLOB
);

-- 2 SCHEDULE_VERSION
CREATE TABLE SCHEDULE_VERSION (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    start_date TEXT,
    end_date   TEXT
);

-- 3 SCHEDULE
CREATE TABLE SCHEDULE (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    begin      TEXT,
    end        TEXT,
    number     INTEGER,
    version_id INTEGER,
    FOREIGN KEY (version_id) REFERENCES SCHEDULE_VERSION (id)
);

-- 4 DEPARTMENT
CREATE TABLE DEPARTMENT (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT,
    abbreviation TEXT
);

-- 5 STREAM
CREATE TABLE STREAM (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    name            TEXT,
    description     TEXT,
    create_date     TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    lecturer_id     INTEGER,
    discipline_id   INTEGER,
    department_id   INTEGER,
    course          INTEGER,
    active          INTEGER,
    expiration_date TEXT,
    lecture_count   INTEGER,
    practical_count INTEGER,
    lab_count       INTEGER,
    FOREIGN KEY (lecturer_id) REFERENCES LECTURER (id),
    FOREIGN KEY (discipline_id) REFERENCES [DISCIPLINE] (id),
    FOREIGN KEY (department_id) REFERENCES DEPARTMENT (id)
);

-- 6 STUDENT
CREATE TABLE STUDENT (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    card_uid   TEXT,
    card_id    INTEGER,
    first_name TEXT,
    last_name  TEXT,
    patronymic TEXT,
    phone      TEXT,
    email      TEXT,
    gender     TEXT,
    image      BLOB
);

-- 7 GROUP_TYPE
CREATE TABLE GROUP_TYPE (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT
);

-- 8 GROUP (used brackets because GROUP is reserved keyword)
CREATE TABLE [GROUP] (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    name            TEXT,
    department_id   INTEGER,
    type_id         INTEGER,
    active          INTEGER,
    expiration_date TEXT,
    praepostor_id   INTEGER,
    FOREIGN KEY (praepostor_id) REFERENCES STUDENT (id),
    FOREIGN KEY (department_id) REFERENCES DEPARTMENT (id),
    FOREIGN KEY (type_id) REFERENCES GROUP_TYPE (id)
);

-- 9 DISCIPLINE
CREATE TABLE DISCIPLINE (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    name            TEXT,
    description     TEXT,
    create_date     TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    active          INTEGER,
    expiration_date TEXT
);

-- 10 LESSON_TYPE
CREATE TABLE LESSON_TYPE (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT
);

-- 11 LESSON
CREATE TABLE LESSON (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    description  TEXT,
    stream_id    INTEGER,
    create_date  TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    type_id      INTEGER,
    group_id     INTEGER,
    date         TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    schedule_id  INTEGER,
    index_number INTEGER,
    FOREIGN KEY (stream_id) REFERENCES STREAM (id),
    FOREIGN KEY (type_id) REFERENCES LESSON_TYPE (id),
    FOREIGN KEY (group_id) REFERENCES [GROUP] (id),
    FOREIGN KEY (schedule_id) REFERENCES SCHEDULE (id)
);

-- 12 NOTE
CREATE TABLE NOTE (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    lecturer_id INTEGER,
    type        TEXT,
    entity_id   INTEGER,
    description TEXT,
    create_date TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    FOREIGN KEY (lecturer_id) REFERENCES LECTURER (id)
);

-- 13 STUDENT-GROUP
CREATE TABLE STUDENT_GROUP (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER,
    group_id   INTEGER,
    FOREIGN KEY (student_id) REFERENCES STUDENT (id),
    FOREIGN KEY (group_id) REFERENCES [GROUP] (id)
);

-- 14 STREAM-GROUP
CREATE TABLE STREAM_GROUP (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    stream_id INTEGER,
    group_id  INTEGER,
    FOREIGN KEY (stream_id) REFERENCES STREAM (id),
    FOREIGN KEY (group_id) REFERENCES [GROUP] (id)
);

-- 15 STUDENT-LESSON
CREATE TABLE STUDENT_LESSON (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id        INTEGER,
    lesson_id         INTEGER,
    registered        INTEGER             DEFAULT 0,
    registration_time TEXT,
    registration_type TEXT,
    mark              TEXT,
    mark_time         TEXT,
    FOREIGN KEY (student_id) REFERENCES STUDENT (id),
    FOREIGN KEY (lesson_id) REFERENCES LESSON (id)
);

-- 16 ALARM
CREATE TABLE ALARM (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    active INTEGER             DEFAULT 0,
    time   INTEGER,
    volume DECIMAL(1, 1),
    sound  TEXT
);

-- 17 NOTIFICATION_SETTING
CREATE TABLE NOTIFICATION_SETTING (
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    type   TEXT,
    active INTEGER             DEFAULT 0,
    data   TEXT,
    volume DECIMAL(1, 1)       DEFAULT 1,
    sound  TEXT
);

-- 18 STUDENT_NOTIFICATION
CREATE TABLE STUDENT_NOTIFICATION (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id  INTEGER,
    active      INTEGER             DEFAULT 0,
    create_date TEXT                DEFAULT (strftime('%Y-%m-%dT%H:%M:%S', 'now', 'localtime')),
    description TEXT,
    FOREIGN KEY (student_id) REFERENCES STUDENT (id)
);

COMMIT;
