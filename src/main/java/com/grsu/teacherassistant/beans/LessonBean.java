package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.*;
import com.grsu.teacherassistant.entities.Class;
import com.grsu.teacherassistant.models.LazyStudentDataModel;
import com.grsu.teacherassistant.models.LessonStudentModel;
import com.grsu.teacherassistant.models.LessonType;
import com.grsu.teacherassistant.models.SkipInfo;
import com.grsu.teacherassistant.utils.EntityUtils;
import com.grsu.teacherassistant.utils.FacesUtils;
import com.grsu.teacherassistant.utils.LocaleUtils;
import lombok.Data;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "lessonBean")
@ViewScoped
@Data
public class LessonBean implements Serializable, SerialListenerBean {
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	@ManagedProperty(value = "#{lessonModeBean}")
	private LessonModeBean lessonModeBean;

	@ManagedProperty(value = "#{serialBean}")
	private SerialBean serialBean;

	private Lesson selectedLesson;
	private Student processedStudent;

	private List<Student> presentStudents;
	private List<Student> absentStudents;
	private List<Student> additionalStudents;

	private List<Student> allStudents;
	private List<Student> filteredAllStudents;

	private Set<Student> lessonStudents;


	private List<LessonStudentModel> lessonPresentStudents;
	private List<LessonStudentModel> lessonAbsentStudents;

	private LazyStudentDataModel presentStudentsLazyModel;
	private LazyStudentDataModel absentStudentsLazyModel;

	private List<LessonStudentModel> selectedPresentStudents;
	private List<LessonStudentModel> selectedAbsentStudents;


	private boolean selectedAllPresentStudents;
	private boolean selectedAllAbsentStudents;

	private Map<Integer, Map<String, Integer>> skipInfo;

	private long timer;
	private boolean camera = false;
	private List<Note> notes;

	public void initLesson(Lesson lesson) {
		serialBean.setCurrentListener(this);
		serialBean.startRecord();

		this.selectedLesson = lesson;
		calculateTimer();
		if (selectedLesson != null) {
			initStudents();
			initAllStudents();
			initLessonStudents();
			initAdditionalStudents();

			if (selectedLesson.getStream() != null) {
				skipInfo = StudentDAO.getSkipInfo(selectedLesson.getStream().getId(), selectedLesson.getId());

				lessonAbsentStudents = new ArrayList<>();
				lessonPresentStudents = new ArrayList<>();

				updateLessonStudents();

				absentStudentsLazyModel = new LazyStudentDataModel(lessonAbsentStudents);
				presentStudentsLazyModel = new LazyStudentDataModel(lessonPresentStudents);
			}
		}
	}

	public void returnToLessons() {
		clear();
		sessionBean.setActiveView("lessons");
	}

	public void openLessonMode() {
		lessonModeBean.setStream(selectedLesson.getStream());
		lessonModeBean.setLesson(selectedLesson);
		sessionBean.setActiveView("lessonMode");
		lessonModeBean.initLessonMode();
	}

	public void openPhotoMode() {
		lessonModeBean.setStream(selectedLesson.getStream());
		lessonModeBean.setLesson(selectedLesson);
		sessionBean.setActiveView("photoMode");
		lessonModeBean.initLessonMode();
	}

	public void clear() {
		selectedLesson = null;
		processedStudent = null;
		presentStudents = null;
		absentStudents = null;
		lessonStudents = null;
		allStudents = null;
		filteredAllStudents = null;
		skipInfo = null;
		additionalStudents = null;
		timer = 0;
		camera = false;
		notes = null;
	}

	private void initStudents() {
		if (selectedLesson == null || selectedLesson.getStream() == null) {
			presentStudents = null;
			absentStudents = null;
		} else {
			List<StudentClass> studentClasses = new ArrayList<>();
			for (Class cls : selectedLesson.getClasses()) {
				studentClasses.addAll(cls.getStudentClasses().values());
			}
			presentStudents = new ArrayList<>();
			absentStudents = new ArrayList<>();
			for (StudentClass studentClass : studentClasses) {
				if (studentClass.getRegistered()) {
					presentStudents.add(studentClass.getStudent());
				} else {
					absentStudents.add(studentClass.getStudent());
				}
			}
		}
	}

	public void initAllStudents() {
		List<Student> allStudents = new ArrayList<>(sessionBean.getStudents());
		if (presentStudents != null) {
			allStudents.removeAll(presentStudents);
		}
		if (absentStudents != null) {
			allStudents.removeAll(absentStudents);
		}
		this.allStudents = allStudents;
	}

	private void initLessonStudents() {
		lessonStudents = new HashSet<>();
		if (selectedLesson.getStream() != null) {
			if (selectedLesson.getGroup() != null) {
				lessonStudents = new HashSet<>(selectedLesson.getGroup().getStudents());
			} else {
				for (Group group : selectedLesson.getStream().getGroups()) {
					lessonStudents.addAll(group.getStudents());
				}
			}
		}
	}

	private void initAdditionalStudents() {
		additionalStudents = new ArrayList<>();
		if (lessonStudents != null && presentStudents != null) {
			additionalStudents = new ArrayList<>(this.presentStudents);
			additionalStudents.removeAll(this.lessonStudents);
		}
	}

	@Override
	public boolean process(String uid) {
		Student student = EntityUtils.getPersonByUid(absentStudents, uid);
		if (student == null) {
			if (EntityUtils.getPersonByUid(presentStudents, uid) != null) {
				System.out.println("Student not registered. Reason: Uid[ " + uid + " ] already exists.");
				return false;
			} else {
				student = EntityUtils.getPersonByUid(allStudents, uid);
			}
		}
		if (student != null) {
			return processStudent(student);
		} else {
			System.out.println("Student not registered. Reason: Uid[ " + uid + " ] not exist in database.");
			return false;
		}
	}

	private boolean processStudent(Student student) {
		presentStudents.add(student);
		if (absentStudents.contains(student)) {
			absentStudents.remove(student);
		} else {
			additionalStudents.add(student);
		}

		if (student.getStudentClasses() != null) {
			StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClazz().getId());
			if (studentClass == null) {
				studentClass = new StudentClass();
				studentClass.setStudent(student);
				studentClass.setClazz(selectedLesson.getClazz());
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
				EntityDAO.add(studentClass);
				student.getStudentClasses().put(studentClass.getClazz().getId(), studentClass);
				selectedLesson.getClazz().getStudentClasses().put(studentClass.getStudent().getId(), studentClass);
			} else {
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
				EntityDAO.update(studentClass);
				updateSkipInfo(Arrays.asList(student));
			}
		}

		processedStudent = student;
		updateLessonStudents();
		FacesUtils.push("/register", processedStudent);
		System.out.println("Student registered");
		return true;
	}

	public void addStudent(Student student) {
		presentStudents.add(student);
		if (absentStudents.contains(student)) {
			absentStudents.remove(student);
		} else {
			additionalStudents.add(student);
			allStudents.remove(student);
			if (filteredAllStudents != null) {
				filteredAllStudents.remove(student);
			}
		}

		if (student.getStudentClasses() != null) {
			StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClazz().getId());
			if (studentClass == null) {
				studentClass = new StudentClass();
				studentClass.setStudent(student);
				studentClass.setClazz(selectedLesson.getClazz());
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				EntityDAO.add(studentClass);
				student.getStudentClasses().put(studentClass.getClazz().getId(), studentClass);
				selectedLesson.getClazz().getStudentClasses().put(studentClass.getStudent().getId(), studentClass);
			} else {
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				EntityDAO.update(studentClass);
				updateSkipInfo(Arrays.asList(student));
			}

		}

		processedStudent = student;
		updateLessonStudents();
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	public void removeStudent(Student student) {
		StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClazz().getId());
		if (lessonStudents.contains(student)) {

			studentClass.setRegistrationType(null);
			studentClass.setRegistrationTime(null);
			studentClass.setRegistered(false);
			EntityDAO.update(studentClass);
			absentStudents.add(student);
			updateSkipInfo(Arrays.asList(student));
		} else {
			student.getStudentClasses().remove(selectedLesson.getClazz().getId());
			selectedLesson.getClazz().getStudentClasses().remove(student.getId());
			EntityDAO.delete(studentClass);
			allStudents.add(student);
		}

		presentStudents.remove(student);
		additionalStudents.remove(student);
		updateLessonStudents();

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}


	public void removeLesson(Lesson lesson) {
		EntityDAO.delete(lesson);
		sessionBean.getLessons().remove(lesson);
	}

	public void addAbsentStudents() {
		if (selectedAbsentStudents != null && selectedAbsentStudents.size() > 0) {
			List<Student> selectedStudents = null;
			if (selectedAllAbsentStudents) {
				selectedStudents = lessonAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			} else {
				selectedStudents = selectedAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			}

			List<StudentClass> studentClassList = new ArrayList<>();
			for (Student student : selectedStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClazz().getId());
				studentClass.setRegistered(true);
				studentClass.setRegistrationTime(LocalTime.now());
				studentClass.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
				studentClassList.add(studentClass);
			}
			EntityDAO.update(new ArrayList<>(studentClassList));
			updateSkipInfo(selectedStudents);

			presentStudents.addAll(selectedStudents);
			absentStudents.removeAll(selectedStudents);
			allStudents.removeAll(selectedStudents);

			selectedAbsentStudents.clear();

			updateLessonStudents();
		}

		processedStudent = null;
		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	public void removePresentStudents() {
		if (selectedPresentStudents != null && selectedPresentStudents.size() > 0) {
			List<Student> selectedStudents = null;
			if (selectedAllPresentStudents) {
				selectedStudents = lessonPresentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			} else {
				selectedStudents = selectedPresentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
			}


			List<StudentClass> updateStudentClassList = new ArrayList<>();
			List<StudentClass> removeStudentClassList = new ArrayList<>();

			for (Student student : selectedStudents) {
				StudentClass studentClass = student.getStudentClasses().get(selectedLesson.getClazz().getId());

				if (lessonStudents.contains(student)) {

					studentClass.setRegistrationType(null);
					studentClass.setRegistrationTime(null);
					studentClass.setRegistered(false);
					updateStudentClassList.add(studentClass);
					absentStudents.add(student);
				} else {
					student.getStudentClasses().remove(selectedLesson.getClazz().getId());
					selectedLesson.getClazz().getStudentClasses().remove(student.getId());
					allStudents.add(student);
					removeStudentClassList.add(studentClass);
				}
			}
			EntityDAO.update(new ArrayList<>(updateStudentClassList));
			EntityDAO.delete(new ArrayList<>(removeStudentClassList));
			updateSkipInfo(selectedStudents);

			presentStudents.removeAll(selectedStudents);
			additionalStudents.removeAll(selectedStudents);
			selectedPresentStudents.clear();

			updateLessonStudents();
		}

		FacesUtils.execute("PF('aStudentsTable').clearFilters()");
		FacesUtils.execute("PF('pStudentsTable').clearFilters()");
	}

	private void updateSkipInfo(List<Student> students) {
		List<Integer> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());

		List<SkipInfo> studentSkipInfo = StudentDAO.getStudentSkipInfo(studentIds, selectedLesson.getStream().getId(), selectedLesson.getId());

		for (Integer id : studentIds) {
			skipInfo.remove(id);
		}

		if (studentSkipInfo != null && studentSkipInfo.size() > 0) {
			for (SkipInfo si : studentSkipInfo) {

				Map<String, Integer> studentSkipInfoMap = skipInfo.get(si.getStudentId());
				if (studentSkipInfoMap == null) {
					studentSkipInfoMap = new HashMap<>();
					studentSkipInfoMap.put(Constants.TOTAL_SKIP, 0);
					skipInfo.put(si.getStudentId(), studentSkipInfoMap);
				}

				studentSkipInfoMap.put(si.getLessonType().getKey(), si.getCount());
				int total = studentSkipInfoMap.get(Constants.TOTAL_SKIP);
				studentSkipInfoMap.put(Constants.TOTAL_SKIP, total + si.getCount());
			}
		}


	}

	/* LESSON STUDENTS TABLES */

	private void generateLessonStudents(List<LessonStudentModel> lessonStudentModelList, List<Student> students) {
		lessonStudentModelList.clear();
		for (Student st : students) {
			LessonStudentModel lessonStudentModel = new LessonStudentModel(st);
			lessonStudentModel.setRegistrationTime(
					st.getStudentClasses().get(selectedLesson.getClazz().getId()).getRegistrationTime());
			Map<String, Integer> stSkipInfo = skipInfo.get(st.getId());
			if (stSkipInfo != null) {
				lessonStudentModel.setTotalSkip(stSkipInfo.get(Constants.TOTAL_SKIP));
				lessonStudentModel.setLectureSkip(stSkipInfo.get(LessonType.LECTURE.getKey()));
				lessonStudentModel.setPracticalSkip(stSkipInfo.get(LessonType.PRACTICAL.getKey()));
				lessonStudentModel.setLabSkip(stSkipInfo.get(LessonType.LAB.getKey()));
			}

			lessonStudentModelList.add(lessonStudentModel);
		}
	}

	private void updateLessonStudents() {
		generateLessonStudents(lessonAbsentStudents, absentStudents);
		generateLessonStudents(lessonPresentStudents, presentStudents);
	}

	public void addLessonStudent(LessonStudentModel lessonStudentModel) {
		addStudent(lessonStudentModel.getStudent());
	}

	public void removeLessonStudent(LessonStudentModel lessonStudentModel) {
		removeStudent(lessonStudentModel.getStudent());
	}


	public String getStudentSkip(Student student) {
		LocaleUtils localeUtils = new LocaleUtils();
		Map<String, Integer> studentSkipInfoMap = skipInfo.get(student.getId());
		if (studentSkipInfoMap != null) {
			Integer total = studentSkipInfoMap.get(Constants.TOTAL_SKIP);
			Integer lecture = studentSkipInfoMap.get(LessonType.LECTURE.getKey());
			Integer practical = studentSkipInfoMap.get(LessonType.PRACTICAL.getKey());
			Integer lab = studentSkipInfoMap.get(LessonType.LAB.getKey());

			String skip = (lecture == null ? "0" : lecture) + "/" +
					(practical == null ? "0" : practical) + "/" +
					(lab == null ? "0" : lab);

			return localeUtils.getMessage("label.skips") + ": " + total + "&nbsp;&nbsp;&nbsp;" + skip;
		}
		return localeUtils.getMessage("lesson.visit.noSkip");
	}

	public void onStudentRowSelect(SelectEvent event) {
		processedStudent = ((LessonStudentModel) event.getObject()).getStudent();
		notes = new ArrayList<>();
		notes.addAll(processedStudent.getNotes());
		processedStudent.getStudentClasses().values().forEach(sc -> notes.addAll(sc.getNotes()));
	}

	public void onPresentStudentsSelect(ToggleSelectEvent event) {
		selectedAllPresentStudents = event.isSelected();
	}

	public void onAbsentStudentsSelect(ToggleSelectEvent event) {
		selectedAllAbsentStudents = event.isSelected();
	}

	public void calculateTimer() {
		timer = 0;

		LocalDateTime date = selectedLesson.getClazz().getDate();
		LocalTime begin = selectedLesson.getClazz().getSchedule().getBegin();
		LocalDateTime beginDate = date.plus(begin.toNanoOfDay(), ChronoUnit.NANOS);
		LocalDateTime now = LocalDateTime.now();
		if (beginDate.isAfter(now)) {
			timer = now.until(beginDate, ChronoUnit.SECONDS);
		}
		if (timer == 0) {
			LocalTime end = selectedLesson.getClazz().getSchedule().getEnd();
			LocalDateTime endDate = date.plus(end.toNanoOfDay(), ChronoUnit.NANOS);
			if (endDate.isAfter(now)) {
				timer = now.until(endDate, ChronoUnit.SECONDS);
			}
		}
	}


	public void exitStudents() {
		setFilteredAllStudents(null);
		update("views");
		closeDialog("addStudentsDialog");
	}

}
