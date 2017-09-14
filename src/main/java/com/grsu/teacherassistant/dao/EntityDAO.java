package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.AssistantEntity;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.entities.StudentLesson;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
public class EntityDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityDAO.class);

	public static void add(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.save(entity);
			transaction.commit();
			LOGGER.info("[ " + entity + " ] successfully added to database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void add(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count = 0;
			for (AssistantEntity entity : entities) {
				session.save(entity);
				if (++count % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			LOGGER.info("[ " + entities + " ] successfully added to database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void delete(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.delete(entity);
			transaction.commit();
			LOGGER.info("[ " + entity + " ] successfully deleted from database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void delete(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count = 0;
			for (AssistantEntity entity : entities) {
				session.delete(entity);
				if (++count % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			LOGGER.info("[ " + entities + " ] successfully deleted from database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void update(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.update(entity);
			transaction.commit();
			LOGGER.info("[ " + entity + " ] successfully updated in database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void update(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count = 0;
			for (AssistantEntity entity : entities) {
				session.update(entity);
				if (++count % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			LOGGER.info("[ " + entities + " ] successfully updated in database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
	}

	public static void save(AssistantEntity entity) {
		if (entity.getId() == null) {
			add(entity);
		} else {
			update(entity);
		}
	}

	public static <T extends AssistantEntity> T get(Class<T> entityType, int id) {
		Session session = DBSessionFactory.getSession();

		try {
			return session.get(entityType, id);
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
		return null;
	}

	public static <T extends AssistantEntity> List<T> getAll(Class<T> entityType) {
		Session session = DBSessionFactory.getSession();

		try {
			LOGGER.info("Start loading [ " + entityType + " ] from database.");
			return session.createQuery("from " + entityType.getSimpleName()).list();
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			LOGGER.info("End loading [ " + entityType + " ] from database.");
			session.close();
		}
		return null;
	}

	public static Student initialize() {
        Transaction transaction = null;
        Session session = DBSessionFactory.getSession();
        Student student = null;

        try {
            transaction = session.beginTransaction();

            student = session.get(Student.class, 1);
            Hibernate.initialize(student.getGroups());
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return student;
    }
}
