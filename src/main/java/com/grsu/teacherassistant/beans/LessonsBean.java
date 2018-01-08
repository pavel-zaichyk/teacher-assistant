package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.beans.mode.RegistrationModeBean;
import com.grsu.teacherassistant.beans.utility.MenuBean;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.dao.StreamDAO;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.models.LessonType;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Pavel Zaychick
 */
@Data
@ManagedBean(name = "lessonsBean")
@ViewScoped
public class LessonsBean implements Serializable {
    @ManagedProperty(value = "#{menuBean}")
    private MenuBean menuBean;

    @ManagedProperty(value = "#{registrationModeBean}")
    private RegistrationModeBean registrationModeBean;

    private List<Lesson> lessons;
    private Lesson selectedLesson;

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private boolean closed;
    private boolean past = true;
    private Integer streamId;
    private Integer disciplineId;
    private Integer scheduleId;
    private Integer groupId;
    private Integer month;
    private LessonType type;

    private Map<Integer, String> streamNames;
    private final List<LessonType> lessonTypes =
        new ArrayList<>(Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB, LessonType.EXAM));

    @PostConstruct
    private void init() {
        dateFrom = LocalDate.now().atStartOfDay();
        dateTo = LocalDate.now().plusMonths(1).atStartOfDay();
    }

    public void removeLesson(Lesson lesson) {
        EntityDAO.delete(lesson);
        lessons.remove(lesson);
    }

    public List<Lesson> getLessons() {
        if (lessons == null) {
            lessons = LessonDAO.getAll(dateFrom, dateTo, closed, streamId, past, disciplineId, scheduleId, groupId, type);
        }
        return lessons;
    }

    public void search() {
        lessons = null;
    }

    public Set<Map.Entry<Integer, String>> getStreams() {
        if (streamNames == null) {
            streamNames = StreamDAO.getNames();
        }
        return streamNames.entrySet();
    }

    public void changeMonth(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            dateFrom = LocalDate.now().plusMonths(-((Integer) event.getNewValue())).atStartOfDay();
            dateTo = LocalDate.now().plusMonths(1).atStartOfDay();
        }
    }

    public void changeDate(ValueChangeEvent event) {
        month = null;
    }

    public void openRegistrartionMode() {
        registrationModeBean.initLesson(selectedLesson);
        menuBean.changeView("registrationMode");
        menuBean.hideMenu();
    }
}
