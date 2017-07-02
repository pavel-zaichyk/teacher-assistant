package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.Class;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Note;
import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.entities.StudentClass;
import com.grsu.teacherassistant.models.LazyStudentDataModel;
import com.grsu.teacherassistant.models.LessonModel;
import com.grsu.teacherassistant.models.LessonStudentModel;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.event.CellEditEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zaychick-pavel on 3/3/17.
 */
@ManagedBean(name = "lessonModeBean")
@ViewScoped
@Data
public class LessonModeBean implements Serializable {

	private Stream stream;
	private Lesson lesson;
	private List<LessonModel> lessons;
	private List<LessonModel> attestations;
	private List<LessonModel> exams;

	private List<Note> notes;
	private String newNote;
	private Integer entityId;

	private List<LessonStudentModel> students;
	private LessonStudentModel selectedStudent;
	private LazyStudentDataModel studentsLazyModel;

	private LessonModel selectedLesson;

	private Integer selectedCell;
	private String selectedClientId;
	private String selectedType;
	private String selectedLessonType;

	private boolean registered;
	private boolean showAttestations = false;
	private boolean showSkips = true;

	public void initLessonMode() {
		initLessonStudents();
	}

	public void clear() {
		stream = null;
//		lesson = null;
		lessons = null;
		attestations = null;
		exams = null;

		notes = null;
		newNote = null;
		entityId = null;

		students = null;
		selectedStudent = null;
		studentsLazyModel = null;

		selectedType = null;
		selectedCell = null;
		selectedClientId = null;
		selectedLessonType = null;

		selectedLesson = null;

		showAttestations = false;
		showSkips = true;
	}

	private void initLessonStudents() {
		List<Lesson> lessons = new ArrayList<>();
		Set<Student> studentSet = new HashSet<>();
		if (stream != null && lesson != null) {
			if (lesson.getGroup() != null) {
				studentSet.addAll(lesson.getGroup().getStudents());
				lessons = stream.getLessons().stream().filter(l -> l.getGroup() == null || (lesson.getGroup().equals(l.getGroup()))).collect(Collectors.toList());
			} else {
				stream.getGroups().stream().forEach(g -> studentSet.addAll(g.getStudents()));
				lessons = stream.getLessons();
			}
		}

		//int lessons
		this.lessons = lessons.stream()
				.filter(l -> Arrays.asList(LessonType.LECTURE, LessonType.PRACTICAL, LessonType.LAB).contains(l.getType()))
				.sorted((l1, l2) -> {
					if (l1.getClazz().getDate().isAfter(l2.getClazz().getDate())) return -1;
					if (l1.getClazz().getDate().isBefore(l2.getClazz().getDate())) return 1;
					return 0;
				})
				.map(LessonModel::new).collect(Collectors.toList());

		//init attestations
		this.attestations = lessons.stream()
				.filter(l -> LessonType.ATTESTATION.equals(l.getType()))
				.map(LessonModel::new).collect(Collectors.toList());
		attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));

		//init exams
		this.exams = lessons.stream()
				.filter(l -> LessonType.EXAM.equals(l.getType()))
				.map(LessonModel::new).collect(Collectors.toList());

		//init additional students
		List<LessonStudentModel> additionalStudents = StudentDAO.getAdditionalStudents(lesson.getId()).stream()
				.map(LessonStudentModel::new).collect(Collectors.toList());
		additionalStudents.stream().forEach(s -> s.setAdditional(true));

		students = studentSet.stream().map(LessonStudentModel::new).collect(Collectors.toList());
		students.addAll(additionalStudents);
		students = students.stream().sorted(Comparator.comparing(s -> s.name)).collect(Collectors.toList());
		students.stream().forEach(this::updateAverageAttestation);

		Map<Integer, Map<String, Integer>> skipInfo = StudentDAO.getSkipInfo(stream.getId(), lesson.getId());
		students.stream().forEach(s -> {
			if (skipInfo.containsKey(s.getId())) {
				s.setTotalSkip(skipInfo.get(s.getId()).get(Constants.TOTAL_SKIP));
			}
			exams.stream().forEach(e -> {
				StudentClass exam = s.getStudent().getStudentClasses().get(e.getLesson().getClazz().getId());
				if (exam != null) {
					try {
						s.setExamMark(Integer.valueOf(exam.getMark()));
					} catch (NumberFormatException ex) {
						s.setExamMark(null);
					}
				}
			});
		});

		studentsLazyModel = new LazyStudentDataModel(students);
	}

	private void updateAverageAttestation(LessonStudentModel student) {
		student.setAverageAttestation(null);
		List<String> marks = new ArrayList<>();
		attestations.stream().forEach(lesson -> {
			StudentClass sc = student.getStudent().getStudentClasses().get(lesson.getId());
			if (sc != null && sc.getMark() != null) {
				marks.addAll(Arrays.asList(sc.getMark().split("[^0-9]")));
			}
		});
		if (marks.size() > 0) {
			student.setAverageAttestation(marks.stream().mapToInt(Integer::parseInt).average().getAsDouble());
		}
	}

	public void initRegisteredDialog() {
		if (Constants.STUDENT_CLASS.equals(selectedType)) {
			selectedLesson = calculateSelectedLesson();
			registered = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId()).getRegistered();
		} else {
			selectedLesson = null;
		}
	}

	public void initNotes() {
		notes = null;
		selectedLesson = calculateSelectedLesson();

		switch (selectedType) {
			case Constants.STUDENT_CLASS:
				StudentClass sc = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId());
				if (sc != null) {
					notes = sc.getNotes();
					entityId = sc.getId();
				}
				break;
			case Constants.STUDENT:
				notes = selectedStudent.getStudent().getNotes();
				entityId = selectedStudent.getStudent().getId();
				break;
			case Constants.LESSON:
				notes = selectedLesson.getLesson().getNotes();
				entityId = selectedLesson.getId();
				break;
		}
	}

	private LessonModel calculateSelectedLesson() {
		if (selectedCell != null) {
			int column = selectedCell;
			if (Constants.OTHER.equals(selectedLessonType)) {
				return lessons.get(selectedCell);
			}
			if (Constants.ATTESTATION.equals(selectedLessonType)) {
				column--;
				if (showSkips) {
					column--;
				}
				if (exams.size() > 0) {
					column--;
				}
				return attestations.get(column);
			}
		}
		return null;
	}

	public void onCellEdit(CellEditEvent event) {
		Integer id;
		if (event.getColumn().getColumnKey().contains("attestation")) {
			id = attestations.get(((DynamicColumn) event.getColumn()).getIndex()).getId();
			updateAverageAttestation(studentsLazyModel.getRowData());
		} else {
			id = lessons.get(((DynamicColumn) event.getColumn()).getIndex()).getId();
		}
		EntityDAO.update(studentsLazyModel.getRowData().getStudent().getStudentClasses().get(id));
	}

	public void saveNote() {
		if (newNote != null && !newNote.isEmpty()) {
			Note note = new Note();
			note.setCreateDate(LocalDateTime.now());
			note.setDescription(newNote);
			note.setType(selectedType);
			note.setEntityId(entityId);
			notes.add(note);
			EntityDAO.save(note);
			newNote = null;
		}
		FacesUtils.closeDialog("notesDialog");

	}

	public void saveRegisteredInfo() {
		boolean oldValue = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId()).getRegistered();
		if (oldValue != registered) {
			StudentClass studentClass = selectedStudent.getStudent().getStudentClasses().get(selectedLesson.getId());
			studentClass.setRegistered(registered);
			if (!registered) {
				studentClass.setRegistrationTime(null);
				studentClass.setRegistrationType(null);

				FacesUtils.execute("$('#" + selectedClientId.replaceAll("\\:", "\\\\\\\\:") + "').closest('td').addClass('skip');");
			} else {
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				FacesUtils.execute("$('#" + selectedClientId.replaceAll("\\:", "\\\\\\\\:") + "').closest('td').removeClass('skip')");
			}
			EntityDAO.save(studentClass);
		}
		FacesUtils.closeDialog("registeredDialog");
		selectedLesson = null;
	}

	public void closeDialog() {
		newNote = null;
		notes = null;
		selectedType = null;
		selectedCell = null;
		selectedLesson = null;
		System.out.println("selectedClientId " + selectedClientId);
		if (selectedClientId != null) {
			FacesUtils.update(selectedClientId);
		}
		selectedClientId = null;
	}

	public void removeNote(Note note) {
		EntityDAO.delete(note);
		notes.remove(note);
	}

	public void createAttestation() {
		Lesson lesson = new Lesson();
		Class cl = new Class();
		cl.setDate(LocalDateTime.now());
		cl.setLesson(lesson);
		lesson.setClasses(new HashSet<>());
		lesson.getClasses().add(cl);
		lesson.setType(LessonType.ATTESTATION);
		lesson.setStream(this.stream);
		lesson.setGroup(this.lesson.getGroup());
		lesson.setNotes(new ArrayList<>());

		EntityDAO.add(lesson);
		EntityDAO.add(lesson.getClazz());
		stream.getLessons().add(lesson);

		List<StudentClass> studentClasses = new ArrayList<>();
		students.stream().forEach(s -> {
			StudentClass sc = new StudentClass();
			sc.setStudent(s.getStudent());
			sc.setClazz(lesson.getClazz());
			sc.setNotes(new ArrayList<>());
			studentClasses.add(sc);
			s.getStudent().getStudentClasses().put(lesson.getClazz().getId(), sc);
		});
		EntityDAO.add(new ArrayList<>(studentClasses));
		lesson.getClazz().setStudentClasses(new HashMap<>());
		studentClasses.stream().forEach(sc -> lesson.getClazz().getStudentClasses().put(sc.getStudentId(), sc));
		attestations.add(new LessonModel(lesson));
		attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));
	}

	public int frozenColumns() {
		int frozenColumns = 2;
		if (showAttestations) {
			frozenColumns += attestations.size();
			if (attestations.size() > 1) {
				frozenColumns++;
			}
			if (exams.size() > 0) {
				frozenColumns++;
			}
		}
		return frozenColumns;
	}

	public void removeAttestation(LessonModel lesson) {
		EntityDAO.delete(lesson.getLesson());
		attestations.remove(lesson);
		attestations.forEach(a -> a.setNumber(attestations.indexOf(a) + 1));
		students.stream().forEach(this::updateAverageAttestation);
	}

}
