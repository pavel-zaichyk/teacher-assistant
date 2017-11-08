package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.beans.utility.UtilityBean;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.models.LessonType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pavel Zaychick
 */
public class UtilityBeanTest {
    @Test
    public void getLessonCountTest() {
        Lesson lesson = new Lesson();
        lesson.setIndex(10);
        lesson.setType(LessonType.LECTURE);
        Stream stream = new Stream();
        lesson.setStream(stream);

        Assert.assertEquals("(10 из XX)", new UtilityBean().getLessonCount(lesson));

        stream.setLectureCount(20);
        Assert.assertEquals("(10 из 20)", new UtilityBean().getLessonCount(lesson));
    }
}
