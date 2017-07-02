package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class GroupDAO {

	public Group getByName(String name) {
		Session session = DBSessionFactory.getSession();
		try {
			Query query = session.createQuery("from Group where name = :name");
			query.setParameter("name", name);
			query.setFirstResult(0);
			query.setMaxResults(1);
			return (Group) query.uniqueResult();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
