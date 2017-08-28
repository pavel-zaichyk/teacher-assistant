package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Pavel Zaychick
 */
public class StreamDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamDAO.class);

    private StreamDAO() {
    }

    public static List<Stream> getAll() {

        try (Session session = DBSessionFactory.getSession()) {
            LOGGER.info("Start loading Streams from database.");
            Query query = session.createQuery("from Stream where active = true and (expirationDate > current_date or expirationDate is null)");
            return query.getResultList();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("End loading Streams from database.");
        }
        return null;
    }

    public static Map<Integer, String> getNames() {
        try (Session session = DBSessionFactory.getSession()) {
            LOGGER.info("Start loading Stream names from database.");
            Query query = session.createQuery("select s.id, s.name from Stream s order by s.name");
            List<Object[]> queryResult = query.getResultList();
            Map<Integer, String> result = new HashMap<>();
            queryResult.forEach(e -> result.put((Integer) e[0], (String) e[1]));
            return result;
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("End loading Stream names from database.");
        }
        return Collections.emptyMap();
    }

}
