package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Lesson;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "utility")
@ViewScoped
public class UtilityBean implements Serializable {
    public String getLessonCount(Lesson lesson) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(lesson.getIndex()).append(" из ");

        Integer typeCount = null;
        switch (lesson.getType()) {
            case LECTURE:
                typeCount = lesson.getStream().getLectureCount();
                break;
            case PRACTICAL:
                typeCount = lesson.getStream().getPracticalCount();
                break;
            case LAB:
                typeCount = lesson.getStream().getLabCount();
                break;
        }

        if (typeCount != null) {
            messageBuilder.append(typeCount);
        } else {
            messageBuilder.append("XX");
        }

        return messageBuilder.toString();
    }

    public String getGroupNames(List<Group> groups) {
        return String.join(", ", groups.stream().map(Group::getName).collect(Collectors.toList()));
    }

    public LocalDateTime concatDateTime(LocalDateTime date, LocalTime time) {
        if (date == null || time == null) {
            return date;
        }
        return LocalDateTime.of(date.toLocalDate(), time);
    }

    public boolean afterNow(LocalDateTime date, LocalTime time) {
        if (date == null || time == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(LocalDateTime.of(date.toLocalDate(), time));
    }
}
