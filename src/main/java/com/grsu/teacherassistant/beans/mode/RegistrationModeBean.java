package com.grsu.teacherassistant.beans.mode;

import com.grsu.teacherassistant.beans.*;
import com.grsu.teacherassistant.beans.utility.*;
import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.*;
import com.grsu.teacherassistant.models.*;
import com.grsu.teacherassistant.utils.EntityUtils;
import com.grsu.teacherassistant.utils.FacesUtils;
import com.grsu.teacherassistant.utils.LocaleUtils;
import lombok.Data;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "registrationModeBean")
@ViewScoped
@Data
public class RegistrationModeBean implements Serializable, SerialListenerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationModeBean.class);

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @ManagedProperty(value = "#{lessonModeBean}")
    private LessonModeBean lessonModeBean;

    @ManagedProperty(value = "#{studentModeBean}")
    private StudentModeBean studentModeBean;

    @ManagedProperty(value = "#{serialBean}")
    private SerialBean serialBean;

    @ManagedProperty(value = "#{imageBean}")
    private ImageBean imageBean;

    @ManagedProperty(value = "#{localeBean}")
    private LocaleBean localeBean;

    @ManagedProperty(value = "#{alarmBean}")
    private AlarmBean alarmBean;

    @ManagedProperty(value = "#{notificationSettingsBean}")
    private NotificationSettingsBean notificationSettingsBean;

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

    private boolean reRegistration;
    private boolean fastRegistration;
    private Lesson lastLesson;
    private boolean studentNotExist;

    public void initLesson(Lesson lesson) {
        alarmBean.setAlarms();
        serialBean.setCurrentListener(this);
        serialBean.startRecord();

        if (lesson != null) {
            this.selectedLesson = EntityDAO.get(Lesson.class, lesson.getId());
            calculateTimer();
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

            initLastLesson();
        }
    }

    private void initLastLesson() {
        fastRegistration = false;
        if (lessonPresentStudents.size() == 0) {
            lastLesson = null;
            List<Lesson> lessons = LessonDAO.getAll(selectedLesson.getDate(), selectedLesson.getDate(), false, selectedLesson.getStream().getId());
            for (Lesson lesson : lessons) {
                if (selectedLesson.getSchedule().getBegin().isAfter(lesson.getSchedule().getBegin())) {
                    lastLesson = lesson;
                    break;
                }
            }

            if (lastLesson != null) {
                fastRegistration = true;
            }
        }
    }

    public void fastRegistration() {
        selectedLesson.getStudentLessons().values().forEach(sl -> {
            if (!sl.isRegistered()) {
                StudentLesson studentLesson = lastLesson.getStudentLessons().get(sl.getStudentId());
                if (studentLesson != null && studentLesson.isRegistered()) {
                    sl.setRegistered(studentLesson.isRegistered());
                    sl.setRegistrationTime(LocalTime.now());
                    sl.setRegistrationType("MANUAL");
                }
            }
        });
        EntityDAO.update(new ArrayList<>(selectedLesson.getStudentLessons().values()));
        fastRegistration = false;
        initLesson(selectedLesson);
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
        reRegistration = false;
        studentNotExist = false;
    }

    private void initStudents() {
        if (selectedLesson == null || selectedLesson.getStream() == null) {
            presentStudents = null;
            absentStudents = null;
        } else {
            presentStudents = new ArrayList<>();
            absentStudents = new ArrayList<>();
            for (StudentLesson studentLesson : selectedLesson.getStudentLessons().values()) {
                if (studentLesson.isRegistered()) {
                    presentStudents.add(studentLesson.getStudent());
                } else {
                    absentStudents.add(studentLesson.getStudent());
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
        final long t = System.currentTimeMillis();
        LOGGER.info("==> process(); uid = " + uid);
        reRegistration = false;
        studentNotExist = false;
        Student student = EntityUtils.getPersonByUid(absentStudents, uid);
        if (student == null) {
            student = EntityUtils.getPersonByUid(presentStudents, uid);
            if (student != null) {
                reRegistration = true;
                LOGGER.info("Student not registered. Reason: Uid[ " + uid + " ] already registered.");
//				return false;
            } else {
                student = EntityUtils.getPersonByUid(allStudents, uid);
            }
        }
        if (student == null) {
            LOGGER.info("Student not registered. Reason: Uid[ " + uid + " ] not exist in database.");
            studentNotExist = true;
            student = new Student();
            student.setCardUid(uid);
        }
        LOGGER.info("<== process(); reRegistration = " + reRegistration + "; studentNotExist " + studentNotExist + "; student = " + student + "; " + (System.currentTimeMillis() - t));
        return processStudent(student);
    }

    private boolean processStudent(Student student) {
        final long t = System.currentTimeMillis();
        LOGGER.info("==> processStudent();");
        if (!reRegistration && !studentNotExist) {
            presentStudents.add(student);
            if (absentStudents.contains(student)) {
                absentStudents.remove(student);
            } else {
                additionalStudents.add(student);
            }

            if (student.getStudentLessons() != null) {
                StudentLesson studentLesson = student.getStudentLessons().get(selectedLesson.getId());
                if (studentLesson == null) {
                    studentLesson = new StudentLesson();
                    studentLesson.setStudent(student);
                    studentLesson.setLesson(selectedLesson);
                    studentLesson.setRegistered(true);
                    studentLesson.setRegistrationTime(LocalTime.now());
                    studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
                    EntityDAO.add(studentLesson);
                    student.getStudentLessons().put(selectedLesson.getId(), studentLesson);
                    selectedLesson.getStudentLessons().put(studentLesson.getStudent().getId(), studentLesson);
                } else {
                    studentLesson.setRegistered(true);
                    studentLesson.setRegistrationTime(LocalTime.now());
                    studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_AUTOMATIC);
                    studentLesson.setNotes(new ArrayList<>());
                    EntityDAO.update(studentLesson);
                    updateSkipInfo(Arrays.asList(student));
                }
            }

            selectStudent(student);
            updateLessonStudents();
            FacesUtils.push("/register", processedStudent.getCardUid());
            checkStudentNotifications(student);
            pushStudentDesktopNotification();
            LOGGER.info("Student registered");
            LOGGER.info("<== processStudent(); registered = true" + (System.currentTimeMillis() - t));
            return true;
        } else {
            selectStudent(student);
            FacesUtils.push("/register", processedStudent.getCardUid());
            pushStudentDesktopNotification();
            LOGGER.info("Student not registered");
            LOGGER.info("<== processStudent(); registered = false; " + (System.currentTimeMillis() - t));
            return false;
        }
    }

    public void addStudent(Student student) {
        reRegistration = false;
        studentNotExist = false;
        if (!selectedLesson.getStudentLessons().containsKey(student.getId())) {
            student = EntityDAO.get(Student.class, student.getId());
        }
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

        if (student.getStudentLessons() != null) {
            StudentLesson studentLesson = student.getStudentLessons().get(selectedLesson.getId());
            if (studentLesson == null) {
                studentLesson = new StudentLesson();
                studentLesson.setStudent(student);
                studentLesson.setLesson(selectedLesson);
                studentLesson.setRegistered(true);
                studentLesson.setRegistrationTime(LocalTime.now());
                studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
                EntityDAO.add(studentLesson);
                student.getStudentLessons().put(selectedLesson.getId(), studentLesson);
                selectedLesson.getStudentLessons().put(studentLesson.getStudent().getId(), studentLesson);
            } else {
                studentLesson.setRegistered(true);
                studentLesson.setRegistrationTime(LocalTime.now());
                studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
                studentLesson.setNotes(new ArrayList<>());
                EntityDAO.update(studentLesson);
                updateSkipInfo(Arrays.asList(student));
            }
            checkStudentNotifications(student);
        }

        selectStudent(student);
        updateLessonStudents();
        FacesUtils.execute("PF('aStudentsTable').clearFilters()");
        FacesUtils.execute("PF('pStudentsTable').clearFilters()");
    }

    public void removeStudent(Student student) {
        StudentLesson studentLesson = student.getStudentLessons().get(selectedLesson.getId());
        if (lessonStudents.contains(student)) {

            studentLesson.setRegistrationType(null);
            studentLesson.setRegistrationTime(null);
            studentLesson.setRegistered(false);
            EntityDAO.update(studentLesson);
            absentStudents.add(student);
            updateSkipInfo(Arrays.asList(student));
        } else {
            student.getStudentLessons().remove(selectedLesson.getId());
            selectedLesson.getStudentLessons().remove(student.getId());
            EntityDAO.delete(studentLesson);
            allStudents.add(student);
        }

        presentStudents.remove(student);
        additionalStudents.remove(student);
        updateLessonStudents();

        FacesUtils.execute("PF('aStudentsTable').clearFilters()");
        FacesUtils.execute("PF('pStudentsTable').clearFilters()");
    }

    public void addAbsentStudents() {
        if (selectedAbsentStudents != null && selectedAbsentStudents.size() > 0) {
            List<Student> selectedStudents = null;
            if (selectedAllAbsentStudents) {
                selectedStudents = lessonAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
            } else {
                selectedStudents = selectedAbsentStudents.stream().map(LessonStudentModel::getStudent).collect(Collectors.toList());
            }

            List<StudentLesson> studentLessonList = new ArrayList<>();
            for (Student student : selectedStudents) {
                StudentLesson studentLesson = student.getStudentLessons().get(selectedLesson.getId());
                studentLesson.setRegistered(true);
                studentLesson.setRegistrationTime(LocalTime.now());
                studentLesson.setRegistrationType(Constants.REGISTRATION_TYPE_MANUAL);
                studentLessonList.add(studentLesson);
            }
            EntityDAO.update(new ArrayList<>(studentLessonList));
            updateSkipInfo(selectedStudents);

            presentStudents.addAll(selectedStudents);
            absentStudents.removeAll(selectedStudents);
            allStudents.removeAll(selectedStudents);

            selectedAbsentStudents.clear();

            updateLessonStudents();
        }

        selectStudent(null);
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


            List<StudentLesson> updateStudentLessonList = new ArrayList<>();
            List<StudentLesson> removeStudentLessonList = new ArrayList<>();

            for (Student student : selectedStudents) {
                StudentLesson studentLesson = student.getStudentLessons().get(selectedLesson.getId());

                if (lessonStudents.contains(student)) {

                    studentLesson.setRegistrationType(null);
                    studentLesson.setRegistrationTime(null);
                    studentLesson.setRegistered(false);
                    updateStudentLessonList.add(studentLesson);
                    absentStudents.add(student);
                } else {
                    student.getStudentLessons().remove(selectedLesson.getId());
                    selectedLesson.getStudentLessons().remove(student.getId());
                    allStudents.add(student);
                    removeStudentLessonList.add(studentLesson);
                }
            }
            EntityDAO.update(new ArrayList<>(updateStudentLessonList));
            EntityDAO.delete(new ArrayList<>(removeStudentLessonList));
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
        final long t = System.currentTimeMillis();
        LOGGER.info("==> updateSkipInfo();");
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

        LOGGER.info("<== updateSkipInfo(); " + (System.currentTimeMillis() - t));
    }

	/* LESSON STUDENTS TABLES */

    private void generateLessonStudents(List<LessonStudentModel> lessonStudentModelList, List<Student> students) {
        lessonStudentModelList.clear();
        for (Student st : students) {
            LessonStudentModel lessonStudentModel = new LessonStudentModel(st);
            lessonStudentModel.setRegistrationTime(
                st.getStudentLessons().get(selectedLesson.getId()).getRegistrationTime());
            lessonStudentModel.setAdditional(!lessonStudents.contains(st));
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
        final long t = System.currentTimeMillis();
        LOGGER.info("==> updateLessonStudents();");
        generateLessonStudents(lessonAbsentStudents, absentStudents);
        generateLessonStudents(lessonPresentStudents, presentStudents);
        LOGGER.info("<== updateLessonStudents(); " + (System.currentTimeMillis() - t));
    }

    public void addLessonStudent(LessonStudentModel lessonStudentModel) {
        addStudent(lessonStudentModel.getStudent());
    }

    public void removeLessonStudent(LessonStudentModel lessonStudentModel) {
        removeStudent(lessonStudentModel.getStudent());
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
//
        }
    }


    public String getStudentSkip(Student student) {
        LocaleUtils localeUtils = new LocaleUtils(localeBean.getLocale());
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
        selectStudent(((LessonStudentModel) event.getObject()).getStudent());
    }

    public void onStudentRowDblClckSelect(SelectEvent event) {
        lessonModeBean.setLesson(selectedLesson);
        lessonModeBean.setStream(selectedLesson.getStream());
        sessionBean.setActiveView("studentMode");
        studentModeBean.initStudentMode(((LessonStudentModel) event.getObject()).getStudent(), selectedLesson.getStream());
    }

    public void onPresentStudentsSelect(ToggleSelectEvent event) {
        selectedAllPresentStudents = event.isSelected();
    }

    public void onAbsentStudentsSelect(ToggleSelectEvent event) {
        selectedAllAbsentStudents = event.isSelected();
    }

    public void calculateTimer() {
        timer = 0;

        LocalDateTime date = selectedLesson.getDate();
        LocalTime begin = selectedLesson.getSchedule().getBegin();
        LocalDateTime beginDate = date.plus(begin.toNanoOfDay(), ChronoUnit.NANOS);
        LocalDateTime now = LocalDateTime.now();
        if (beginDate.isAfter(now)) {
            timer = now.until(beginDate, ChronoUnit.SECONDS);
        }
        if (timer == 0) {
            LocalTime end = selectedLesson.getSchedule().getEnd();
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

    private void selectStudent(Student student) {
        if (student != null) {
            processedStudent = student;
            notes = new ArrayList<>();
            if (processedStudent.getNotes() != null) {
                notes.addAll(processedStudent.getNotes());
            }
            if (processedStudent.getStudentLessons() != null) {
                processedStudent.getStudentLessons().values().forEach(sc -> notes.addAll(sc.getNotes()));
            }
        } else {
            processedStudent = null;
            notes = null;
        }
    }

    /**
     * Генерация всплывабщего уведомления с информацией о студенте
     *
     * @return уведомление
     */
    private Notification createStudentDesktopNotification() {
        LocaleUtils localeUtils = new LocaleUtils(localeBean.getLocale());
        Notification notification = new Notification();
        notification.setTimeout(3000);

        if (!studentNotExist) {
            notification.setBody(getStudentSkip(processedStudent).replace(localeUtils.getMessage("label.skips") + ": ", "").replace("&nbsp;&nbsp;&nbsp;", " - "));
            notification.setTitle(processedStudent.getFullName());
        } else {
            notification.setBody(localeUtils.getMessage("label.studentNotExist"));
            notification.setTitle(processedStudent.getCardUid());
        }

        notification.setImage(imageBean.getImagePath(processedStudent.getCardUid()));
        return notification;
    }

    /**
     * Отправка сгенерированного уведомления клиенту (браузеру).
     */
    private void pushStudentDesktopNotification() {
        FacesUtils.push("/notify", createStudentDesktopNotification());
    }

    private String newNotification;
    private LessonStudentModel selectedStudent;

    public void removeNotification(StudentNotification notification) {
        EntityDAO.delete(notification);
        selectedStudent.getStudent().getNotifications().remove(notification);
    }

    public void saveNotification() {
        if (newNotification != null && !newNotification.isEmpty()) {
            StudentNotification studentNotification = new StudentNotification();
            studentNotification.setActive(Boolean.TRUE);
            studentNotification.setDescription(newNotification);
            studentNotification.setStudent(selectedStudent.getStudent());
            studentNotification.setCreateDate(LocalDateTime.now());
            selectedStudent.getStudent().getNotifications().add(studentNotification);
        }
        newNotification = null;
        EntityDAO.save(selectedStudent.getStudent().getNotifications());
        FacesUtils.closeDialog("notificationDialog");
    }

    private void checkStudentNotifications(Student student) {
        if (notificationSettingsBean.isActive()) {

            NotificationSetting studentNotificationSettings = notificationSettingsBean.getSettings().get(NotificationType.STUDENT.name());
            if (studentNotificationSettings != null && studentNotificationSettings.getActive()) {
                if (student.getNotifications().parallelStream().anyMatch(StudentNotification::getActive)) {
                    notificationSettingsBean.play(studentNotificationSettings);
                    return;
                }
            }

            NotificationSetting absenceNotificationSettings = notificationSettingsBean.getSettings().get(NotificationType.ABSENCE.name());
            if (absenceNotificationSettings != null && absenceNotificationSettings.getActive()) {
                if (skipInfo.containsKey(student.getId())) {
                    Integer totalSkip = skipInfo.get(student.getId()).get(Constants.TOTAL_SKIP);
                    if (totalSkip != null && totalSkip >= absenceNotificationSettings.getData()) {
                        notificationSettingsBean.play(absenceNotificationSettings);
                        return;
                    }
                }
            }

            NotificationSetting praepostorNotificationSettings = notificationSettingsBean.getSettings().get(NotificationType.PRAEPOSTOR.name());
            if (praepostorNotificationSettings != null && praepostorNotificationSettings.getActive()) {
                if (selectedLesson.getGroup() == null) {
                    if (selectedLesson.getStream().getGroups().parallelStream().anyMatch(g -> student.getPraepostorGroups().contains(g))) {
                        notificationSettingsBean.play(praepostorNotificationSettings);
                    }
                } else {
                    if (student.getPraepostorGroups().contains(selectedLesson.getGroup())) {
                        notificationSettingsBean.play(praepostorNotificationSettings);
                    }
                }
            }

        }
    }
}
