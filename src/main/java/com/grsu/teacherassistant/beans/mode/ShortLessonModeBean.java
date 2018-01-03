package com.grsu.teacherassistant.beans.mode;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Note;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.entities.StudentLesson;
import com.grsu.teacherassistant.models.*;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "shortLessonModeBean")
@ViewScoped
@Data
public class ShortLessonModeBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortLessonModeBean.class);


    private LessonModel lesson;
    private LazyStudentDataModel studentsLazyModel;
    private List<LessonModel> attestations;

    private LessonStudentModel selectedStudent;
    private String newNote;

    public void init(Lesson lesson) {
        this.lesson = new LessonModel(EntityDAO.get(Lesson.class, lesson.getId()), true);

        studentsLazyModel = new LazyStudentDataModel(this.lesson.getStudents());

        this.attestations = this.lesson.getLesson().getStream().getLessons().stream()
            .filter(l -> LessonType.ATTESTATION.equals(l.getType()))
            .map(LessonModel::new).collect(Collectors.toList());
        attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));
    }

    public void changeMark(ValueChangeEvent event) {
        studentsLazyModel.getRowData().getStudent().getStudentLessons().get(lesson.getId()).setMark((String) event.getNewValue());
        studentsLazyModel.getRowData().initMarks();
    }

    public void updateMarks() {
        FacesUtils.update("shortLessonModeTable:" + studentsLazyModel.getRowIndex() + ":numberMarks");
        FacesUtils.update("shortLessonModeTable:" + studentsLazyModel.getRowIndex() + ":symbolMarks");
    }

    public void changeAttestationMark(ValueChangeEvent event) {
        int attestationId = Integer.parseInt(String.valueOf(event.getComponent().getAttributes().get("attestationId")));
        studentsLazyModel.getRowData().updateAttestationMark(attestationId, (Mark) event.getNewValue());
    }

    public void updateAverageAttestation() {
        FacesUtils.update("shortLessonModeTable:" + studentsLazyModel.getRowIndex() + ":averageAttestation");
    }

    public void changeExamMark(ValueChangeEvent event) {
        studentsLazyModel.getRowData().setExamMark((Mark) event.getNewValue());
        studentsLazyModel.getRowData().updateTotal();
        studentsLazyModel.getRowData().saveExam();
    }

    public void updateTotalMark() {
        FacesUtils.update("shortLessonModeTable:" + studentsLazyModel.getRowIndex() + ":total");
    }

    public void changeTotalMark(ValueChangeEvent event) {
        studentsLazyModel.getRowData().setTotalMark((Mark) event.getNewValue());
        studentsLazyModel.getRowData().updateExam();
        studentsLazyModel.getRowData().saveExam();
    }

    public void updateExamMark() {
        FacesUtils.update("shortLessonModeTable:" + studentsLazyModel.getRowIndex() + ":exam");
    }

    public void saveNote() {
        if (newNote != null && !newNote.isEmpty()) {
            Note note = new Note();
            note.setCreateDate(LocalDateTime.now());
            note.setDescription(newNote);
            note.setType("STUDENT_LESSON");
            note.setEntityId(selectedStudent.getStudent().getStudentLessons().get(lesson.getId()).getId());
            selectedStudent.getStudent().getStudentLessons().get(lesson.getId()).getNotes().add(note);
            EntityDAO.save(note);
            selectedStudent.updateLessonNotes();
            newNote = null;
        }
        FacesUtils.closeDialog("notesDialog");
    }

    public void removeNote(Note note) {
        EntityDAO.delete(note);
        selectedStudent.getStudent().getStudentLessons().get(lesson.getId()).getNotes().remove(note);
    }

    public void createAttestation() {
        Lesson lesson = new Lesson();
        lesson.setDate(LocalDateTime.now());
        lesson.setType(LessonType.ATTESTATION);
        lesson.setStream(this.lesson.getLesson().getStream());
        lesson.setNotes(new ArrayList<>());
        lesson.setIndex(LessonDAO.getNextIndex(this.lesson.getLesson().getStream().getId(), LessonType.ATTESTATION, null));

        EntityDAO.add(lesson);
        this.lesson.getLesson().getStream().getLessons().add(lesson);

        Set<Student> students = new HashSet<>();
        this.lesson.getLesson().getStream().getGroups().forEach(g -> students.addAll(g.getStudents()));
        List<StudentLesson> studentLessons = new ArrayList<>();
        students.forEach(s -> {
            StudentLesson sc = new StudentLesson();
            sc.setStudent(s);
            sc.setLesson(lesson);
            sc.setNotes(new ArrayList<>());
            studentLessons.add(sc);
            s.getStudentLessons().put(lesson.getId(), sc);
        });
        EntityDAO.add(new ArrayList<>(studentLessons));
        lesson.setStudentLessons(new HashMap<>());
        studentLessons.stream().forEach(sc -> lesson.getStudentLessons().put(sc.getStudentId(), sc));
        attestations.add(new LessonModel(lesson));
        attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));

        init(this.lesson.getLesson());
    }

    public void removeAttestation(LessonModel attestation) {
        EntityDAO.delete(attestation.getLesson());
        attestations.remove(attestation);
        attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));
        lesson.getStudents().forEach(LessonStudentModel::updateAverageAttestation);
        lesson.getLesson().getStream().getLessons().remove(attestation.getLesson());
    }

    public void addSkip() {
        StudentLesson studentLesson = selectedStudent.getStudent().getStudentLessons().get(lesson.getId());
        if (studentLesson.isRegistered()) {
            studentLesson.setRegistered(false);
            studentLesson.setRegistrationTime(null);
            studentLesson.setRegistrationType(null);
            EntityDAO.save(studentLesson);
            selectedStudent.updateSkips(lesson.getLesson().getStream());
        }
    }

    public void removeSkip() {
        StudentLesson studentLesson = selectedStudent.getStudent().getStudentLessons().get(lesson.getId());
        if (!studentLesson.isRegistered()) {
            studentLesson.setRegistered(true);
            studentLesson.setRegistrationTime(LocalTime.now());
            studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
            EntityDAO.save(studentLesson);
            selectedStudent.updateSkips(lesson.getLesson().getStream());
        }
    }


}
