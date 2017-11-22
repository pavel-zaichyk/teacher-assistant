package com.grsu.teacherassistant.beans.mode;

import com.grsu.teacherassistant.beans.utility.SerialBean;
import com.grsu.teacherassistant.beans.utility.SerialListenerBean;
import com.grsu.teacherassistant.beans.utility.SessionBean;
import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.*;
import com.grsu.teacherassistant.entities.StudentLesson;
import com.grsu.teacherassistant.models.LessonStudentModel;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.models.Mark;
import com.grsu.teacherassistant.models.SkipInfo;
import com.grsu.teacherassistant.utils.EntityUtils;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.component.inputnumber.InputNumber;
import org.primefaces.component.inputtext.InputText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "studentModeBean")
@ViewScoped
@Data
public class StudentModeBean implements Serializable, SerialListenerBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentModeBean.class);

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	@ManagedProperty(value = "#{serialBean}")
	private SerialBean serialBean;

	private Stream stream;
	private LessonStudentModel lessonStudent;
	private Student student;

//	private Map<Integer, Integer> numberMarks;
//	private Map<String, Integer> symbolMarks;
//	private Double averageMark;
	private List<StudentLesson> studentLessons;
	private List<StudentLesson> attestations;
	private StudentLesson exam;

	private StudentLesson selectedStudentLesson;
	private String newNote;
	private boolean registered;
	private StudentLesson editedStudentLesson;

	private List<Stream> studentStreams;

	public void initStudentMode(Student student, Stream stream) {
		serialBean.setCurrentListener(this);
		clear();
		this.student = student;
		this.stream = stream;

		if (this.student != null) {
			lessonStudent = new LessonStudentModel(student, stream);
			studentStreams = student.getStudentLessons().values().stream()
					.filter(sl -> sl.getLesson() != null)
					.map(sl -> sl.getLesson().getStream())
					.distinct()
					.sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
					.collect(Collectors.toList());
			if (stream == null && studentStreams.size() > 0) {
				this.stream = studentStreams.get(0);
			}

			if (this.stream != null) {
//				updateStudentSkips();

				//init student marks
//				initMarks();

				//init student lessons
				studentLessons = this.stream.getLessons().stream()
						.filter(l -> Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB).contains(l.getType()) && student.getStudentLessons().containsKey(l.getId()))
						.map(l -> student.getStudentLessons().get(l.getId()))
						.collect(Collectors.toList());

				//init attestations
				attestations = this.stream.getLessons().stream()
						.filter(l -> LessonType.ATTESTATION.equals(l.getType()) && student.getStudentLessons().containsKey(l.getId()))
						.map(l -> student.getStudentLessons().get(l.getId()))
						.collect(Collectors.toList());
//				lessonStudent.updateAverageAttestation();
//				lessonStudent.updateTotal();
			}
		}
	}
/*
	private void initMarks() {
		symbolMarks = new HashMap<>();
		numberMarks = new HashMap<>();
		lessonStudent.getStudent().getStudentLessons().values().forEach(sl -> {
			if (stream.getLessons().contains(sl.getLesson())) {
				if (LessonType.EXAM.equals(sl.getLesson().getType())) {
					exam = sl;
					try {
						lessonStudent.setExamMark(Mark.getByFieldValue(sl.getMark()));
					} catch (NumberFormatException ex) {
						lessonStudent.setExamMark(null);
					}
				}

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
	}*/

	public boolean isAdditionalLesson(Lesson lesson) {
		if (lesson.getGroup() == null) {
			for (Group group : lesson.getStream().getGroups()) {
				if (lessonStudent.getStudent().getGroups().contains(group)) {
					return false;
				}
			}
		}
		if (lessonStudent.getStudent().getGroups().contains(lesson.getGroup())) {
			return false;
		}
		return true;
	}

	public void clear() {
		stream = null;
		lessonStudent = null;
//		numberMarks = null;
//		symbolMarks = null;
//		averageMark = null;
		studentLessons = null;
		attestations = null;
		exam = null;

		selectedStudentLesson = null;
		newNote = null;

		studentStreams = null;

	}

	public List<Map.Entry<Integer, Integer>> getNumberMarks() {
		if (lessonStudent.getNumberMarks() != null) {
			return new ArrayList<>(lessonStudent.getNumberMarks().entrySet());
		}
		return null;
	}

	public List<Map.Entry<String, Integer>> getSymbolMarks() {
		if (lessonStudent.getSymbolMarks() != null) {
			return new ArrayList<>(lessonStudent.getSymbolMarks().entrySet());
		}
		return null;
	}
/*
	private void updateAverageAttestation() {
		lessonStudent.setAverageAttestation(null);
		List<String> marks = new ArrayList<>();
		attestations.stream().forEach(a -> {
			if (a.getMark() != null && !a.getMark().isEmpty()) {
				marks.addAll(Arrays.asList(a.getMark().split("[^0-9]")));
			}
		});
		if (marks.size() > 0) {
			lessonStudent.setAverageAttestation(marks.stream().mapToInt(Integer::parseInt).average().getAsDouble());
		}
	}
/*
	private void updateStudentSkips() {
		if (stream != null) {
			List<SkipInfo> studentSkipInfo = StudentDAO.getStudentSkipInfo(Arrays.asList(lessonStudent.getId()), stream.getId(), -1);
			if (studentSkipInfo != null && studentSkipInfo.size() > 0) {
				for (SkipInfo si : studentSkipInfo) {
					switch (si.getLessonType()) {
						case LECTURE:
							lessonStudent.setLectureSkip(si.getCount());
							break;
						case PRACTICAL:
							lessonStudent.setPracticalSkip(si.getCount());
							break;
						case LAB:
							lessonStudent.setLabSkip(si.getCount());
							break;
					}
				}
				if (lessonStudent.getLectureSkip() == null) {
					lessonStudent.setLectureSkip(0);
				}
				if (lessonStudent.getPracticalSkip() == null) {
					lessonStudent.setPracticalSkip(0);
				}
				if (lessonStudent.getLabSkip() == null) {
					lessonStudent.setLabSkip(0);
				}
				lessonStudent.setTotalSkip(lessonStudent.getLectureSkip() + lessonStudent.getPracticalSkip() + lessonStudent.getLabSkip());
			}
		}
	}*/

	public void changeExamMark(ValueChangeEvent event) {
		if (event.getSource() instanceof InputText && "examMark".equals(((InputText) event.getSource()).getId())) {
			lessonStudent.setExamMark((Mark) event.getNewValue());
			lessonStudent.updateTotal();
		}
		if (event.getSource() instanceof InputNumber && "totalMark".equals(((InputNumber) event.getSource()).getId())) {
			lessonStudent.setTotalMark((Integer) event.getNewValue());
			lessonStudent.updateExam();
		}
		if (exam != null) {
			exam.setMark(lessonStudent.getExamMark() != null ? lessonStudent.getExamMark().getKey() : null);
			EntityDAO.save(exam);
		}
	}

	public void changeAttestationMark(ValueChangeEvent event) {
		String clientId = ((InputText) event.getSource()).getClientId();

		String attestationId = clientId.substring(0, clientId.lastIndexOf(":"));
		attestationId = attestationId.substring(attestationId.lastIndexOf(":") + 1);
		StudentLesson attestation = attestations.get(Integer.parseInt(attestationId));
		attestation.setMark(String.valueOf(event.getNewValue()));
		lessonStudent.updateAverageAttestation();
		lessonStudent.updateTotal();
		EntityDAO.save(attestation);
	}

	public void editMark(StudentLesson studentLesson) {
		editedStudentLesson = studentLesson;
	}

	public void saveMark(ValueChangeEvent event) {
		if (event != null) {
			String value = String.valueOf(event.getNewValue());
			value = value != null ? (value.trim().isEmpty() ? null : value.trim()) : null;
			editedStudentLesson.setMark(value);
			EntityDAO.save(editedStudentLesson);
			lessonStudent.initMarks(stream);
		}
		editedStudentLesson = null;
	}

	//NOTES
	public void saveNote() {
		if (newNote != null && !newNote.isEmpty()) {
			Note note = new Note();
			note.setCreateDate(LocalDateTime.now());
			note.setDescription(newNote);
			note.setType(Constants.STUDENT_LESSON);
			note.setEntityId(selectedStudentLesson.getId());
			EntityDAO.save(note);
			selectedStudentLesson.getNotes().add(note);
		}
		newNote = null;
		FacesUtils.closeDialog("notesDialog");
	}

	public void removeNote(Note note) {
		EntityDAO.delete(note);
		selectedStudentLesson.getNotes().remove(note);
	}

	//REGISTERED INFO
	public void saveRegisteredInfo() {
		boolean oldValue = selectedStudentLesson.isRegistered();
		if (oldValue != registered) {
			selectedStudentLesson.setRegistered(registered);
			if (!registered) {
				selectedStudentLesson.setRegistrationTime(null);
				selectedStudentLesson.setRegistrationType(null);
			} else {
				selectedStudentLesson.setRegistrationTime(LocalTime.now());
				selectedStudentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
			}
			EntityDAO.save(selectedStudentLesson);
			lessonStudent.updateSkips(stream);
		}
		FacesUtils.closeDialog("registeredDialog");
	}

	@Override
	public boolean process(String uid) {
		Student student = EntityUtils.getPersonByUid(sessionBean.getStudents(), uid);
		if (student != null) {
			initStudentMode(student, null);

			FacesUtils.push("/register", uid);
			return true;
		} else {
			LOGGER.info("Student not registered. Reason: Uid[ " + uid + " ] not exist in database.");
			return false;
		}
	}
}
