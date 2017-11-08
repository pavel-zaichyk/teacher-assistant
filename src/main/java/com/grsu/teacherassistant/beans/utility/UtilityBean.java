package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.entities.Lesson;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "utility")
@ViewScoped
public class UtilityBean implements Serializable {
    public String getLessonCount(Lesson lesson) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("(").append(lesson.getIndex()).append(" из ");

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

        messageBuilder.append(")");
        return messageBuilder.toString();
    }
}
