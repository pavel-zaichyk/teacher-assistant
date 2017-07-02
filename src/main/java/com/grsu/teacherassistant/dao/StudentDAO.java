package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.models.SkipInfo;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {
	private StudentDAO() {
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
			e.printStackTrace();
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
			System.err.println(e.getLocalizedMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
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
			System.err.println(e.getLocalizedMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
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
			System.err.println(e.getLocalizedMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
}
