package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a mechanism for working with Lessons. This class contains methods for getting Lessons from database.
 *
 * @author Pavel Zaychick
 */
public class LessonDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAO.class);

    private LessonDAO() {
    }

    /**
     * Return list of lessons from database only next types: Lecture, Practical, Lab and Exam.
     *
     * @return list of lessons
     * @author Pavel Zaychick
     * @see Lesson
     * @see LessonType
     */
    public static List<Lesson> getAll() {
        Session session = DBSessionFactory.getSession();

        try {
            LOGGER.info("Start loading Lessons from database.");
            Query query = session.createQuery("from Lesson l where l.type in (:types) and l.stream.active = true and (l.stream.expirationDate > current_date or l.stream.expirationDate is null)");
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

    /**
     * Return list of lessons which {@link Lesson#date} from <b>dateFrom</b> to <b>dateTo</b>.
     *
     * @param dateFrom start of search interval
     * @param dateTo   end of search interval
     * @return list of lessons from dateFrom to dateTo
     * @author Pavel Zaychick
     * @see Lesson
     */
    public static List<Lesson> getAll(LocalDateTime dateFrom, LocalDateTime dateTo, boolean showClosed, Integer streamId) {
        StringBuilder queryString = new StringBuilder("select distinct l from Lesson as l " +
            "left join fetch l.schedule " +
            "where l.type in (:types)");

        if (dateFrom != null && dateTo != null) {
            queryString.append(" and l.date between :dateFrom and :dateTo");
        }

        if (!showClosed) {
            queryString.append(" and l.stream.active = true and (l.stream.expirationDate > current_date or l.stream.expirationDate is null)");
        }

        if (streamId != null) {
            queryString.append(" and l.stream.id = :streamId");
        }

        queryString.append(" order by l.date desc, l.schedule.begin desc");

        try (Session session = DBSessionFactory.getSession()) {
            LOGGER.info("Start loading Lessons from database.");
            Query query = session.createQuery(queryString.toString());
            query.setParameterList("types", Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB, LessonType.EXAM));
            if (dateFrom != null && dateTo != null) {
                dateFrom = dateFrom.minusSeconds(1);//TODO: fix bug with from date when date equals from date
                query.setParameter("dateFrom", dateFrom);
                query.setParameter("dateTo", dateTo);
            }
            if (streamId != null) {
                query.setParameter("streamId", streamId);
            }
            return query.getResultList();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("End loading Lessons from database.");

        }
        return null;
    }

    public static Integer getNextIndex(int streamId, LessonType lessonType, Group group) {
        LOGGER.info("Start getting lesson index.");
        try (Session session = DBSessionFactory.getSession()) {
            Query query = session.createQuery("select case when max(l.index) is null THEN 1 ELSE (max(l.index) + 1) end from Lesson as l where l.stream.id = :streamId and l.type = :type and (l.group = :group or :group is null)");
            query.setParameter("streamId", streamId);
            query.setParameter("type", lessonType);
            query.setParameter("group", group);
            return (Integer) query.getSingleResult();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("End getting lesson index.");

        }
        return 0;
    }
}
