package com.grsu.teacherassistant.models;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.entities.StudentLesson;
import com.grsu.teacherassistant.utils.DateUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
@Data
public class LessonModel {
    private Integer id;
    private Lesson lesson;
    private LessonType type;
    private String date;
    private Integer number;

    private List<Student> lessonStudents;
    private List<Student> presentStudents;
    private List<Student> absentStudents;
    private List<Student> additionalStudents;
    private List<LessonStudentModel> students;

    public LessonModel(Lesson lesson) {
        this(lesson, null, false);
    }

    public LessonModel(Lesson lesson, boolean initStudents) {
        this(lesson, null, initStudents);
    }

    public LessonModel(Lesson lesson, Integer number, boolean initStudents) {
        id = lesson.getId();
        this.lesson = lesson;
        type = lesson.getType();
        date = DateUtils.formatDate(lesson.getDate(), DateUtils.FORMAT_DATE_SHORT_YEAR);
        this.number = number;

        if (initStudents) {
            lessonStudents = new ArrayList<>();
            if (lesson.getStream() != null) {
                if (lesson.getGroup() != null) {
                    lessonStudents = lesson.getGroup().getStudents();
                } else {
                    lessonStudents = lesson.getStream().getGroups().parallelStream().flatMap(g -> g.getStudents().parallelStream()).distinct().collect(Collectors.toList());
                }
            }

            presentStudents = new ArrayList<>();
            absentStudents = new ArrayList<>();
            for (StudentLesson studentLesson : lesson.getStudentLessons().values()) {
                if (studentLesson.isRegistered()) {
                    presentStudents.add(studentLesson.getStudent());
                } else {
                    absentStudents.add(studentLesson.getStudent());
                }
            }

            additionalStudents = new ArrayList<>(presentStudents);
            additionalStudents.removeAll(lessonStudents);

            students = lessonStudents.stream().map(s -> new LessonStudentModel(s, lesson.getStream())).collect(Collectors.toList());
            students.addAll(additionalStudents.stream().map(s -> new LessonStudentModel(s, lesson.getStream(), true)).collect(Collectors.toList()));
        }
    }


    @Override
    public String toString() {
        return "LessonModel{" +
            "id=" + id +
            ", lesson=" + lesson +
            ", type=" + type +
            ", date='" + date + '\'' +
            ", number=" + number +
            '}';
    }
}
