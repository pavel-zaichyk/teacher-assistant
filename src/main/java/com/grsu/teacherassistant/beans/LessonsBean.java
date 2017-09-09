package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.dao.StreamDAO;
import com.grsu.teacherassistant.entities.Lesson;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pavel Zaychick
 */
@Data
@ManagedBean(name = "lessonsBean")
@ViewScoped
public class LessonsBean implements Serializable {
    private List<Lesson> lessons;
    private List<Lesson> filteredLessons;
    private Lesson selectedLesson;

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private boolean showClosed;
    private Integer streamId;

    private Map<Integer, String> streamNames;

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
            lessons = LessonDAO.getAll(dateFrom, dateTo, showClosed, streamId);
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

}
