package com.grsu.teacherassistant.models;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.*;
import com.grsu.teacherassistant.utils.EntityUtils;
import com.grsu.teacherassistant.utils.Utils;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.utils.ApplicationUtils.attestationMarkWeight;
import static com.grsu.teacherassistant.utils.ApplicationUtils.examMarkWeight;

/**
 * @author Pavel Zaychick
 */
@Data
public class LessonStudentModel implements Serializable {
    private Integer id;
    private String name;

    private Integer totalSkip;
    private Integer lectureSkip;
    private Integer practicalSkip;
    private Integer labSkip;

    private LocalTime registrationTime;

    private Student student;
    private boolean additional;

    private String averageAttestation;
    private Mark examMark;
    private Mark totalMark;
    private Map<Integer, Mark> attestationsMark;
    private Map<Integer, Integer> numberMarks;
    private Map<String, Integer> symbolMarks;
    private Double averageMark;

    private List<StudentLesson> attestations;
    private List<StudentLesson> studentLessons;
    private StudentLesson exam;
    private List<StudentLesson> additionalLessons;
    private List<Lesson> skipLessons;

    private List<Note> lessonsNotes;

    private Group group;
    private Stream stream;

    public LessonStudentModel(Student student) {
        this(student, null, false);
    }

    public LessonStudentModel(Student student, Stream stream) {
        this(student, stream, false);
    }

    public LessonStudentModel(Student student, Stream stream, boolean additional) {
        this.student = student;
        this.id = student.getId();
        this.name = student.getFullName();
        this.additional = additional;

        init(stream);
    }

    public void init(Stream stream) {
        this.stream = stream;
        if (stream != null) {

            for (Group g : stream.getGroups()) {
                if (student.getGroups().contains(g)) {
                    group = g;
                    break;
                }
            }

            attestationsMark = new HashMap<>();
            attestations = new ArrayList<>();
            studentLessons = new ArrayList<>();
            additionalLessons = new ArrayList<>();
            lessonsNotes = new ArrayList<>();

            stream.getLessons().forEach(l -> {
                StudentLesson sl = student.getStudentLessons().get(l.getId());
                if (sl != null) {
                    switch (l.getType()) {
                        case ATTESTATION:
                            attestationsMark.put(sl.getId(), Mark.getByFieldValue(sl.getMark()));
                            attestations.add(sl);
                            break;
                        case EXAM:
                            examMark = Mark.getByFieldValue(sl.getMark());
                            exam = sl;
                            break;
                        default:
                            studentLessons.add(sl);
                            if (isAdditionalLesson(sl.getLesson())) {
                                additionalLessons.add(sl);
                            }
                    }
                    lessonsNotes.addAll(sl.getNotes());
                }
            });
            lessonsNotes.sort(Comparator.comparing(Note::getCreateDate));

            updateAverageAttestation();
            updateTotal();
            updateSkips(stream);
            initMarks();
        }
    }

    private boolean isAdditionalLesson(Lesson lesson) {
        if (lesson.getGroup() == null) {
            for (Group group : lesson.getStream().getGroups()) {
                if (student.getGroups().contains(group)) {
                    return false;
                }
            }
        }
        if (student.getGroups().contains(lesson.getGroup())) {
            return false;
        }
        return true;
    }

    public void updateAverageAttestation() {
        averageAttestation = Mark.average(attestationsMark.values());
    }

    public void updateSkips(Stream stream) {
        totalSkip = 0;
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

        if (totalSkip > 0) {
            skipLessons = new ArrayList<>();
            for (Lesson lesson : stream.getLessons()) {
                if (!LessonType.ATTESTATION.equals(lesson.getType()) && !LessonType.EXAM.equals(lesson.getType())) {
                    StudentLesson studentLesson = lesson.getStudentLessons().get(id);
                    if (studentLesson != null && !studentLesson.isRegistered()) {
                        skipLessons.add(lesson);
                    }
                }
            }
            skipLessons.sort(Comparator.comparing(Lesson::getDate));
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

    public void initMarks() {
        symbolMarks = new HashMap<>();
        numberMarks = new HashMap<>();
        studentLessons.forEach(sl -> {

            if (sl.getMark() != null) {
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
        });

        averageMark = numberMarks.entrySet().stream().map(n -> n.getKey() * n.getValue()).mapToInt(Integer::intValue).sum()
            / (double) numberMarks.values().stream().mapToInt(Integer::intValue).sum();
        if (averageMark.equals(Double.NaN)) {
            averageMark = null;
        } else {
            averageMark = new BigDecimal(averageMark).setScale(2, RoundingMode.UP).doubleValue();
        }
    }

    public List<Map.Entry<Integer, Integer>> getNumberMarks() {
        if (numberMarks != null) {
            return new ArrayList<>(numberMarks.entrySet());
        }
        return null;
    }

    public List<Map.Entry<String, Integer>> getSymbolMarks() {
        if (symbolMarks != null) {
            return new ArrayList<>(symbolMarks.entrySet());
        }
        return null;
    }

    public void updateTotal() {
        if (examMark == null || averageAttestation == null) {
            if (examMark == null) {
                totalMark = null;
            } else {
                totalMark = examMark;
            }
        } else {
            if (examMark.isNumberMark()) {
                totalMark = Mark.getByValue((int) Math.round(Utils.parseDouble(averageAttestation, 0) * attestationMarkWeight() + examMark.getValue() * examMarkWeight()));
            } else {
                totalMark = examMark;
            }
        }
    }

    public void updateExam() {
        if (totalMark == null) {
            examMark = null;
        } else {
            if (averageAttestation == null) {
                examMark = totalMark;
            } else {
                if (totalMark.isNumberMark()) {
                    int mark = (int) Math.round((totalMark.getValue() - Utils.parseDouble(averageAttestation, 0) * attestationMarkWeight()) / examMarkWeight());
                    if (mark < 0 || mark > 10) {
                        examMark = null;
                    } else {
                        examMark = Mark.getByValue(mark);
                    }
                } else {
                    examMark = totalMark;
                }
                updateTotal();
            }
        }
    }

    public void updateAttestationMark(Integer attestationId, Mark mark) {
        StudentLesson attestation = EntityUtils.getEntityById(attestations, attestationId);
        if (attestation == null) {
            return;
        }
        if (mark != null) {
            attestation.setMark(mark.getKey());
        } else {
            attestation.setMark(null);
        }
        attestationsMark.put(attestationId, mark);
        updateAverageAttestation();
        updateTotal();
        EntityDAO.save(attestation);
    }

    public void saveExam() {
        if (exam != null) {
            exam.setMark(examMark != null ? examMark.getKey() : null);
            EntityDAO.save(exam);
        }
    }

    public void updateLessonNotes() {
        lessonsNotes = new ArrayList<>();

        stream.getLessons().forEach(l -> {
            StudentLesson sl = student.getStudentLessons().get(l.getId());
            if (sl != null) {
                lessonsNotes.addAll(sl.getNotes());
            }
        });
        lessonsNotes.sort(Comparator.comparing(Note::getCreateDate));
    }
}
