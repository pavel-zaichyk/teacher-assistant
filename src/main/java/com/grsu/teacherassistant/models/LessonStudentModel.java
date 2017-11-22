package com.grsu.teacherassistant.models;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.entities.StudentLesson;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
@Data
public class LessonStudentModel {
    private Integer id;
    private String name;

    private Integer totalSkip;
    private Integer lectureSkip;
    private Integer practicalSkip;
    private Integer labSkip;

    private LocalTime registrationTime;

    private Student student;
    private boolean additional;

    private Double averageAttestation;
    private Mark examMark;
    private Integer totalMark;
    private Map<Integer, Mark> attestationsMark;
    private Map<Integer, Integer> numberMarks;
    private Map<String, Integer> symbolMarks;
    private Double averageMark;

    public LessonStudentModel(Student student) {
        this(student, null);
    }

    public LessonStudentModel(Student student, Stream stream) {
        this.student = student;
        this.id = student.getId();
        this.name = student.getFullName();

        if (stream != null) {
            attestationsMark = new HashMap<>();
            stream.getLessons().forEach(l -> {
                if (LessonType.ATTESTATION.equals(l.getType())) {
                    StudentLesson sl = student.getStudentLessons().get(l.getId());
                    if (sl != null) {
                        attestationsMark.put(sl.getId(), Mark.getByFieldValue(sl.getMark()));
                    }
                }
                if (LessonType.EXAM.equals(l.getType())) {
                    StudentLesson sl = student.getStudentLessons().get(l.getId());
                    if (sl != null) {
                        examMark = Mark.getByFieldValue(sl.getMark());
                    }
                }
            });

            updateAverageAttestation();
            updateTotal();
            updateSkips(stream);
            initMarks(stream);
        }
    }

    public void updateAverageAttestation() {
//        averageAttestation = attestationsMark.values().parallelStream().mapToInt(Mark::getValue).average().getAsDouble();
        averageAttestation = Mark.average(attestationsMark.values());
    }

    public void updateSkips(Stream stream) {
        List<SkipInfo> studentSkipInfo = StudentDAO.getStudentSkipInfo(Collections.singletonList(id), stream.getId(), -1);
        if (studentSkipInfo != null && studentSkipInfo.size() > 0) {
            lectureSkip = 0;
            practicalSkip = 0;
            labSkip = 0;
            for (SkipInfo si : studentSkipInfo) {
                switch (si.getLessonType()) {
                    case LECTURE:
                        lectureSkip = si.getCount();
                        break;
                    case PRACTICAL:
                        practicalSkip = si.getCount();
                        break;
                    case LAB:
                        labSkip = si.getCount();
                        break;
                }
            }
            totalSkip = lectureSkip + practicalSkip + labSkip;
        }
    }

    public String getSkips() {
        String skips = "";
        if (lectureSkip != null || practicalSkip != null || labSkip != null) {
            return (lectureSkip == null ? "0" : lectureSkip) + "/" +
                (practicalSkip == null ? "0" : practicalSkip) + "/" +
                (labSkip == null ? "0" : labSkip);
        }
        return skips;
    }

    public void initMarks(Stream stream) {
        symbolMarks = new HashMap<>();
        numberMarks = new HashMap<>();
        student.getStudentLessons().values().forEach(sl -> {
            if (stream.getLessons().contains(sl.getLesson())) {

                if (sl.getMark() != null && !(LessonType.ATTESTATION.equals(sl.getLesson().getType()) || LessonType.EXAM.equals(sl.getLesson().getType()))) {
                    Arrays.stream(sl.getMark().split(Constants.MARK_DELIMETER)).filter(m -> !m.trim().isEmpty()).forEach(m -> {

                            List<String> numbers = Arrays.stream(m.split("[^0-9]"))
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());
                            if (numbers.size() > 0) {
                                numbers.stream().forEach(n -> {
                                    Integer key = Integer.parseInt(n);
                                    if (!numberMarks.containsKey(key)) {
                                        numberMarks.put(key, 0);
                                    }
                                    numberMarks.put(key, numberMarks.get(key) + 1);
                                });
                            } else {
                                if (!symbolMarks.containsKey(m)) {
                                    symbolMarks.put(m, 0);
                                }
                                symbolMarks.put(m, symbolMarks.get(m) + 1);
                            }
                        }
                    );
                }
            }
        });

        averageMark = numberMarks.entrySet().stream().map(n -> n.getKey() * n.getValue()).mapToInt(Integer::intValue).sum()
            / (double) numberMarks.values().stream().mapToInt(Integer::intValue).sum();
        if (averageMark.equals(Double.NaN)) {
            averageMark = null;
        } else {
            averageMark = new BigDecimal(averageMark).setScale(2, RoundingMode.UP).doubleValue();
        }
    }

    public void updateTotal() {
        if (examMark == null || averageAttestation == null) {
            if (examMark == null) {
                totalMark = null;
            } else {
                totalMark = examMark.getValue();
            }
        } else {
            totalMark = (int) Math.round(averageAttestation * Constants.MARK_ATTESTATION_WEIGHT + examMark.getValue() * Constants.MARK_EXAM_WEIGHT);
        }
    }

    public void updateExam() {
        if (totalMark == null) {
            examMark = null;
        } else {
            if (averageAttestation == null) {
                examMark = Mark.getByFieldValue(String.valueOf(totalMark));
            } else {
                int mark = (int) Math.round((totalMark - averageAttestation * Constants.MARK_ATTESTATION_WEIGHT) / Constants.MARK_EXAM_WEIGHT);
                if (mark < 0) {
                    examMark = Mark.POINT_0;
                } else if (mark > 10) {
                    examMark = Mark.POINT_10;
                } else {
                    examMark = Mark.getByFieldValue(String.valueOf(mark));
                }
                updateTotal();
            }
        }
    }

}
