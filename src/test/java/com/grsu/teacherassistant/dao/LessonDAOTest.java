package com.grsu.teacherassistant.dao;


import com.grsu.teacherassistant.TestConfiguration;
import com.grsu.teacherassistant.entities.Lesson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
public class LessonDAOTest extends TestConfiguration {

    private List<Lesson> lessons;

    @Before
    public void setUp() {
        lessons = new ArrayList<>();

        Lesson lesson1 = new Lesson();
        lesson1.setId(3);
        lesson1.setName("Занятие3");
        lesson1.setDescription("ОписаниеЗанятие3");
        lesson1.setDate(LocalDateTime.parse("2017-02-02T00:00"));
        lessons.add(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setId(4);
        lesson2.setName("Занятие4");
        lesson2.setDescription("ОписаниеЗанятие4");
        lesson2.setDate(LocalDateTime.parse("2017-02-03T00:00"));
        lessons.add(lesson2);
    }

    @Test
    public void getAll() {
        List<Lesson> l = LessonDAO.getAll();

        assert l != null;
        Assert.assertEquals(4, l.size());
    }

    @Test
    public void getAllWithFromAndEndDateTest() {
        List<Lesson> l = LessonDAO.getAll(LocalDateTime.parse("2017-02-02T00:00"), LocalDateTime.parse("2017-02-03T00:00"), false, null);

        assert l != null;
        Assert.assertEquals(2, l.size());
        Assert.assertEquals(lessons.get(0), l.get(0));
        Assert.assertEquals(lessons.get(1), l.get(1));
    }

    @Test
    public void getAllWithShowClosedTest() {
        List<Lesson> l = LessonDAO.getAll(null, null, true, null);

        assert l != null;
        Assert.assertEquals(6, l.size());
    }

    @Test
    public void getAllWithStreamTest() {
        List<Lesson> l = LessonDAO.getAll(null, null, false, 1);

        assert l != null;
        Assert.assertEquals(4, l.size());
    }

}
