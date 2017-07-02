COMMIT;
BEGIN;

-- FILL SCHEDULE
INSERT INTO SCHEDULE (begin, end, number) VALUES ('08:30', '09:50', 1);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('10:05', '11:25', 2);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('11:40', '13:00', 3);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('13:30', '14:50', 4);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('15:05', '16:25', 5);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('16:40', '18:00', 6);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('18:15', '19:35', 7);
INSERT INTO SCHEDULE (begin, end, number) VALUES ('19:50', '21:20', 8);

-- FILL LESSON_TYPE
INSERT INTO LESSON_TYPE (id, name) VALUES (1, 'Лекция');
INSERT INTO LESSON_TYPE (id, name) VALUES (2, 'Практическое занятие');
INSERT INTO LESSON_TYPE (id, name) VALUES (3, 'Лабораторное занятие');
COMMIT;
