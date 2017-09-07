package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.models.SkipInfo;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);

    public static List<Student> getAll() {
        try (Session session = DBSessionFactory.getSession()) {
            LOGGER.info("Start loading Students from database.");
            Query query = session.createQuery("" +
                "select distinct s " +
                "from Student s " +
                "   left join fetch s.groups g " +
                "where (g.active = true and (g.expirationDate > current_date or g.expirationDate is null)) or size (s.groups) = 0 " +
                "order by s.lastName, s.firstName");
            return query.getResultList();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("End loading Students from database.");
        }
        return null;
    }

    public static Student getByCardUid(String cardUid) {
        Session session = DBSessionFactory.getSession();
        try {
            Query query = session.createQuery("from Student where cardUid = :cardUid");
            query.setParameter("cardUid", cardUid);
            query.setFirstResult(0);
            query.setMaxResults(1);
            return (Student) query.uniqueResult();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }

    public static Map<Integer, Map<String, Integer>> getSkipInfo(int streamId, int lessonId) {
        List<SkipInfo> skipInfoList = null;
        Session session = DBSessionFactory.getSession();
        try {
            Query query = session.createNamedQuery("SkipInfoQuery", SkipInfo.class);
            query.setParameter("streamId", streamId);
            query.setParameter("lessonId", lessonId);
            skipInfoList = query.getResultList();
        } catch (PersistenceException e) {
            LOGGER.error(e.getLocalizedMessage());
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }

        Map<Integer, Map<String, Integer>> skipInfo = new HashMap<>();
        if (skipInfoList != null) {
            for (SkipInfo si : skipInfoList) {
                if (!skipInfo.containsKey(si.getStudentId())) {
                    Map<String, Integer> studentSkipInfoMap = new HashMap<>();
                    studentSkipInfoMap.put(Constants.TOTAL_SKIP, 0);
                    skipInfo.put(si.getStudentId(), studentSkipInfoMap);
                }
                skipInfo.get(si.getStudentId()).put(si.getLessonType().getKey(), si.getCount());
                int total = skipInfo.get(si.getStudentId()).get(Constants.TOTAL_SKIP);
                skipInfo.get(si.getStudentId()).put(Constants.TOTAL_SKIP, total + si.getCount());
            }
        }
        return skipInfo;
    }

    public static List<SkipInfo> getStudentSkipInfo(List<Integer> studentId, int streamId, int lessonId) {
        Session session = DBSessionFactory.getSession();
        try {
            Query query = session.createNamedQuery("StudentSkipInfoQuery", SkipInfo.class);
            query.setParameterList("studentId", studentId);
            query.setParameter("streamId", streamId);
            query.setParameter("lessonId", lessonId);
            return query.getResultList();
        } catch (PersistenceException e) {
            LOGGER.error(e.getLocalizedMessage());
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }

    public static List<Student> getAdditionalStudents(int lessonId) {
        Session session = DBSessionFactory.getSession();
        try {
            Query query = session.createNamedQuery("AdditionalStudents", Student.class);
            query.setParameter("lessonId", lessonId);
            return query.getResultList();
        } catch (PersistenceException e) {
            LOGGER.error(e.getLocalizedMessage());
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }
}
