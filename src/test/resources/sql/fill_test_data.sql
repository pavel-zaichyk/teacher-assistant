INSERT INTO DEPARTMENT (name, abbreviation) VALUES ('Факультет 1', 'ф1');
INSERT INTO DEPARTMENT (name, abbreviation) VALUES ('Факультет 2', 'ф2');

INSERT INTO [GROUP] (name, department_id, active) VALUES ('Группа 1', 1, 1);
INSERT INTO [GROUP] (name, department_id, active) VALUES ('Группа 2', 1, 0);
INSERT INTO [GROUP] (name, department_id, active) VALUES ('Группа 3', 2, 1);
INSERT INTO [GROUP] (name, department_id, active) VALUES ('Группа 4', 2, 1);
INSERT INTO [GROUP] (name, active, expiration_date) VALUES ('Группа 5', 1, '2017-01-10T00:00:00');


INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent1', '11', 'ИмяСтудент1', 'ФамилияСтудент1', 'ОтчествоСтудент1', 'student1@test.com');
INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent2', '22', 'ИмяСтудент2', 'ФамилияСтудент2', 'ОтчествоСтудент2', 'student2@test.com');
INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent3', '33', 'ИмяСтудент3', 'ФамилияСтудент3', 'ОтчествоСтудент3', 'student3@test.com');
INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent4', '44', 'ИмяСтудент4', 'ФамилияСтудент4', 'ОтчествоСтудент4', 'student4@test.com');
INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent5', '55', 'ИмяСтудент5', 'ФамилияСтудент5', 'ОтчествоСтудент5', 'student5@test.com');
INSERT INTO STUDENT (card_uid, card_id, first_name, last_name, patronymic, email) VALUES ('uidStudent6', '66', 'ИмяСтудент6', 'ФамилияСтудент6', 'ОтчествоСтудент6', 'student6@test.com');

INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (1, 1);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (1, 3);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (2, 1);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (3, 2);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (4, 2);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (5, 3);
INSERT INTO STUDENT_GROUP (student_id, group_id) VALUES (5, 5);


INSERT INTO DISCIPLINE (name, description) VALUES ('Курс 1', 'ОписаниеКурс1');
INSERT INTO DISCIPLINE (name, description) VALUES ('Курс 2', 'ОписаниеКурс2');

INSERT INTO STREAM (name, description, discipline_id, department_id, course, active) VALUES ('Поток 1', 'ОписаниеПоток1', 1, 1, 1, 1);
INSERT INTO STREAM (name, description, discipline_id, department_id, course, active) VALUES ('Поток 2', 'ОписаниеПоток2', 1, 1, 1, 0);
INSERT INTO STREAM (name, description, discipline_id, department_id, course, active) VALUES ('Поток 3', 'ОписаниеПоток3', 1, 1, 1, 1);
INSERT INTO STREAM (name, description, discipline_id, department_id, course, expiration_date, active) VALUES ('Поток 4', 'ОписаниеПоток4', 1, 1, 1, '2017-01-10T00:00:00', 1);

INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (1, 1);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (1, 2);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (2, 3);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (3, 4);
INSERT INTO STREAM_GROUP (stream_id, group_id) VALUES (3, 5);


INSERT INTO LESSON (name, description, stream_id, type_id, date, schedule_id) VALUES ('Занятие1', 'ОписаниеЗанятие1', 1, 1, '2017-01-01', 2);
INSERT INTO LESSON (name, description, stream_id, type_id, date, schedule_id) VALUES ('Занятие2', 'ОписаниеЗанятие2', 1, 1, '2017-01-02', 3);
INSERT INTO LESSON (name, description, stream_id, type_id, group_id, date, schedule_id) VALUES ('Занятие3', 'ОписаниеЗанятие3', 1, 2, 1, '2017-02-02', 2);
INSERT INTO LESSON (name, description, stream_id, type_id, group_id, date, schedule_id) VALUES ('Занятие4', 'ОписаниеЗанятие4', 1, 2, 1, '2017-02-03', 2);
INSERT INTO LESSON (name, description, stream_id, type_id, group_id, date, schedule_id) VALUES ('Занятие5', 'ОписаниеЗанятие5', 2, 2, 1, '2017-02-03', 2);
INSERT INTO LESSON (name, description, stream_id, type_id, group_id, date, schedule_id) VALUES ('Занятие6', 'ОписаниеЗанятие6', 4, 2, 1, '2017-02-02', 2);

INSERT INTO STUDENT_LESSON (student_id, lesson_id, registered, mark) VALUES (1, 2, 1, '!');
INSERT INTO STUDENT_LESSON (student_id, lesson_id, registered, mark) VALUES (2, 2, 1, '+');
