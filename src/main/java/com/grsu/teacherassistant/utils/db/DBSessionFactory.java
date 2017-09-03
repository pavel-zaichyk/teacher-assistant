package com.grsu.teacherassistant.utils.db;

import com.grsu.teacherassistant.utils.FileUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static com.grsu.teacherassistant.utils.PropertyUtils.getProperty;

/**
 * @author Pavel Zaychick
 */
public class DBSessionFactory {
	private static final String DATABASE_URL = "jdbc:" + getProperty("db.protocol") + ":" + FileUtils.DATABASE_PATH;
	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.setProperty(HIBERNATE_CONNECTION_URL, DATABASE_URL);

			String resourceName = getProperty("db.resource.name");
			if (resourceName != null) {
				configuration.configure(resourceName);
			} else {
				configuration.configure();
			}

			sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	public static boolean isConnected() {
		return sessionFactory.isOpen();
	}

}
