BEGIN;

  ALTER TABLE LESSON
    ADD COLUMN DATE TEXT;
  ALTER TABLE LESSON
    ADD COLUMN SCHEDULE_ID INTEGER REFERENCES SCHEDULE (id);

  UPDATE LESSON
  SET date = (SELECT date
              FROM CLASS
              WHERE CLASS.lesson_id = LESSON.id);
  UPDATE LESSON
  SET schedule_id = (SELECT schedule_id
                     FROM CLASS
                     WHERE CLASS.lesson_id = LESSON.id);

  DROP TABLE CLASS;

  CREATE TABLE STUDENT_LESSON (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id        INTEGER,
    lesson_id         INTEGER,
    registered        INTEGER DEFAULT 0,
    registration_time TEXT,
    registration_type TEXT,
    mark              TEXT,
    mark_time         TEXT,
    FOREIGN KEY (student_id) REFERENCES STUDENT (id),
    FOREIGN KEY (lesson_id) REFERENCES LESSON (id)
  );

  INSERT INTO STUDENT_LESSON
  (student_id, lesson_id, registered, registration_time, registration_type, mark)
    SELECT
      student_id,
      class_id,
      registered,
      registration_time,
      registration_type,
      mark
    FROM STUDENT_CLASS;

  DROP TABLE STUDENT_CLASS;

  UPDATE NOTE
  SET type = 'STUDENT_LESSON'
  WHERE type = 'STUDENT_LESSON';

  COMMIT;
