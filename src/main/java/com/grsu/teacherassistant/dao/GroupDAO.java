package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pavel Zaychick
 */
public class GroupDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAO.class);

	public static Group getByName(String name) {
		Session session = DBSessionFactory.getSession();
		try {
			Query query = session.createQuery("from Group where name = :name");
			query.setParameter("name", name);
			query.setFirstResult(0);
			query.setMaxResults(1);
			return (Group) query.uniqueResult();
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			session.close();
		}
		return null;
	}

}
