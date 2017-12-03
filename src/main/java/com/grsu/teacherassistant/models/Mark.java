package com.grsu.teacherassistant.models;

import lombok.Getter;

import java.util.*;

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

    public static Mark getByValue(int value) {
        for (Mark mark : values()) {
            if (mark.getValue() == value) {
                return mark;
            }
        }
        return null;
    }

    public static String average(Collection<Mark> marks) {
        List<Integer> numberMarks = new ArrayList<>();
        int passMarks = 0;
        int failMarks = 0;
        for (Mark mark : marks) {
            if (mark != null) {
                switch (mark) {
                    case PASS:
                        passMarks++;
                        break;
                    case FAIL:
                        failMarks++;
                        break;
                    default:
                        numberMarks.add(mark.getValue());
                }
            }
        }
        if (numberMarks.size() > 0) {
            OptionalDouble result = numberMarks.stream().mapToInt(Integer::intValue).average();
            if (result.isPresent()) {
                return String.format("%.2f", result.getAsDouble());
            }
        }

        if (passMarks > failMarks) {
            return PASS.getFieldValue()[0];
        }
        if (failMarks > 0) {
            return FAIL.getFieldValue()[0];
        }
        return null;
    }

    public boolean isNumberMark() {
        return value != null;
    }
}
