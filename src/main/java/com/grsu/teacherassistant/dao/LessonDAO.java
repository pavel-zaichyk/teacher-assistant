package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pavel on 3/26/17.
 */
public class LessonDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAO.class);

	public List<Lesson> getAll() {
		Session session = DBSessionFactory.getSession();

		try {
			LOGGER.info("Start loading Lessons from database.");
			Query query = session.createQuery("from Lesson where type in (:types)");
			query.setParameterList("types", Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB, LessonType.EXAM));
			return query.getResultList();
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			LOGGER.info("End loading Lessons from database.");
			session.close();
		}
		return null;
	}
}
