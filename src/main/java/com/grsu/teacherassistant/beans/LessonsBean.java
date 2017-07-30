package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.entities.Lesson;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "lessonsBean")
@ViewScoped
public class LessonsBean implements Serializable {
	private List<Lesson> lessons;
	@Getter @Setter
	private Lesson selectedLesson;

	public void removeLesson(Lesson lesson) {
		EntityDAO.delete(lesson);
		lessons.remove(lesson);
	}

	public List<Lesson> getLessons() {
		if (lessons == null) {
			lessons = (new LessonDAO()).getAll();
		}
		return lessons;
	}

}
