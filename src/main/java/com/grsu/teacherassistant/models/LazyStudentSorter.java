package com.grsu.teacherassistant.models;

import org.primefaces.model.SortOrder;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * @author Pavel Zaychick
 */
public class LazyStudentSorter implements Comparator<LessonStudentModel> {
    private String sortField;

    private SortOrder sortOrder;

    public LazyStudentSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(LessonStudentModel student1, LessonStudentModel student2) {
        try {
            Field field1 = LessonStudentModel.class.getDeclaredField(this.sortField);
            field1.setAccessible(true);
            Object value1 = field1.get(student1);
            Field field2 = LessonStudentModel.class.getDeclaredField(this.sortField);
            field2.setAccessible(true);
            Object value2 = field2.get(student2);

            int value;
            if (value1 == null) {
                value = value2 == null ? 0 : -1;
            } else if (value2 == null) {
                value = 1;
            } else {
                value = ((Comparable) value1).compareTo(value2);
            }

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
