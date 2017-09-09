package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.TestConfiguration;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Stream;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Zaychick
 */
public class StreamDAOTest extends TestConfiguration {
    @Test
    public void getAllTest() {
        List<Stream> s = StreamDAO.getAll();

        assert s != null;
        Assert.assertEquals(2, s.size());
    }

    @Test
    public void getAllShowClosedTest() {
        List<Stream> s = StreamDAO.getAll(true);

        assert s != null;
        Assert.assertEquals(4, s.size());
    }

    @Test
    public void getNamesTest() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(1, "Поток 1");
        expected.put(2, "Поток 2");
        expected.put(3, "Поток 3");
        expected.put(4, "Поток 4");

        Assert.assertEquals(expected, StreamDAO.getNames());
    }

}
