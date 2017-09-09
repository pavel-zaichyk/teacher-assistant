package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.TestConfiguration;
import com.grsu.teacherassistant.entities.Student;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Pavel Zaychick
 */
public class StudentDAOTest extends TestConfiguration {
    @Test
    public void getAllTest() {
        List<Student> s = StudentDAO.getAll();

        assert s != null;
        Assert.assertEquals(4, s.size());
    }
}
