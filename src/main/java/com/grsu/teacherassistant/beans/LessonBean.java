package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.entities.*;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "lessonBean")
@ViewScoped
@Data
public class LessonBean implements Serializable {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    private Lesson lesson;
    private boolean createMode;

    private final List<LessonType> lessonTypes =
        new ArrayList<>(Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB, LessonType.EXAM));

    public void initLesson(Lesson lesson) {
        if (lesson != null) {
            this.lesson = lesson;
            createMode = false;
        } else {
            this.lesson = new Lesson();
            this.lesson.setDate(LocalDateTime.now());
            createMode = true;
        }

        FacesUtils.showDialog("lessonDialog");
    }

    public void exit() {
        lesson = null;
        update("views");
        closeDialog("lessonDialog");
    }

    public void save() {
        if (createMode) {
            if (lesson.getType() == null || lesson.getType() == LessonType.LECTURE) {
                lesson.setGroup(null);
            }
            lesson.setIndex(LessonDAO.getNextIndex(lesson.getStream().getId(), lesson.getType()));

            EntityDAO.add(lesson);

            List<Group> groups;

            if (lesson.getGroup() == null) {
                groups = lesson.getStream().getGroups();
            } else {
                groups = Arrays.asList(lesson.getGroup());
            }

            Set<Student> students = new HashSet<>();
            for (Group group : groups) {
                students.addAll(group.getStudents());
            }

            List<StudentLesson> studentLessons = new ArrayList<>();
            for (Student student : students) {
                StudentLesson sc = new StudentLesson();
                sc.setStudent(student);
                sc.setLesson(lesson);
                studentLessons.add(sc);
            }
            EntityDAO.add(new ArrayList<>(studentLessons));

            sessionBean.updateStudents();
        } else {
            EntityDAO.save(lesson);
        }
        exit();
    }

}
