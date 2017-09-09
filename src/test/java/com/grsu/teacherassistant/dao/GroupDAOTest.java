package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.TestConfiguration;
import com.grsu.teacherassistant.entities.Group;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Pavel Zaychick
 */
public class GroupDAOTest extends TestConfiguration {
    @Test
    public void getAllTest() {
        List<Group> g = GroupDAO.getAll();

        assert g != null;
        Assert.assertEquals(3, g.size());
    }

    @Test
    public void getAllShowClosedTest() {
        List<Group> g = GroupDAO.getAll(true);

        assert g != null;
        Assert.assertEquals(5, g.size());
    }
}
