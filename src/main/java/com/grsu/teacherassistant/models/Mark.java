package com.grsu.teacherassistant.models;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

/**
 * @author Pavel Zaychick
 */
@Getter
public enum Mark {
    POINT_0("0", 0, "0"),
    POINT_1("1", 1, "1"),
    POINT_2("2", 2, "2"),
    POINT_3("3", 3, "3"),
    POINT_4("4", 4, "4"),
    POINT_5("5", 5, "5"),
    POINT_6("6", 6, "6"),
    POINT_7("7", 7, "7"),
    POINT_8("8", 8, "8"),
    POINT_9("9", 9, "9"),
    POINT_10("10", 10, "10"),
    PASS("+", null, "зачтено", "+", "зачет"),
    FAIL("-", null, "не зачтено", "-", "незачет");

    private String key;
    private Integer value;
    private String[] fieldValue;

    Mark(String key, Integer value, String... fieldValue) {
        this.key = key;
        this.value = value;
        this.fieldValue = fieldValue;
    }

    public static Mark getByFieldValue(String fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        for (Mark mark : values()) {
            for (String v : mark.fieldValue) {
                if (v.equals(fieldValue)) {
                    return mark;
                }
            }
        }
        return null;
    }

    public static Double average(Collection<Mark> marks) {
        OptionalDouble result = marks.stream().filter(Objects::nonNull).mapToInt(m -> m.value).average();
        if (result.isPresent()) {
            return result.getAsDouble();
        }
        return null;
    }
}
