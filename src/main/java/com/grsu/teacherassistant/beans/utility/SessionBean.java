package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.GroupDAO;
import com.grsu.teacherassistant.dao.StreamDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
import com.grsu.teacherassistant.entities.Department;
import com.grsu.teacherassistant.entities.Discipline;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.Schedule;
import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.utils.CSVUtils;
import com.grsu.teacherassistant.utils.PhotoStudentUtils;
import com.grsu.teacherassistant.utils.SerialUtils;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

    private boolean connected;
    private String activeView = "lessons";

    private List<Schedule> schedules;
    private List<Discipline> disciplines;
    private List<Department> departments;
    private List<Stream> streams;
    private List<Group> groups;
    private List<Student> students;

    @Getter
    @Setter
    private Lesson lesson;

    @PostConstruct
    public void connect() {
        setConnected(true);
        initData();
    }

    @PreDestroy
    public void disconnect() {
        SerialUtils.disconnect();
        setConnected(false);
    }

    public void initData() {
        updateGroupsFromCSV();
    }

    public List<Group> updateGroupsFromCSV() {
        List<Group> groups = CSVUtils.updateGroupsFromCSV();
        updateEntities();
        return groups;
    }

    public void updateEntities() {
        updateSchedules();
        updateDisciplines();
        updateDepartments();
        updateStreams();
        updateGroups();
        updateStudents();
    }

    public void updateSchedules() {
        schedules = null;
    }

    public void updateDisciplines() {
        disciplines = null;
    }

    public void updateDepartments() {
        departments = null;
    }

    public void updateStreams() {
        streams = null;
    }

    public void updateGroups() {
        groups = null;
    }

    public void updateStudents() {
        students = null;
    }

    public void loadStudentsPhoto() {
        PhotoStudentUtils.storeStudentsPhoto(getStudents());
    }

    /* GETTERS AND SETTERS*/
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getActiveView() {
        return activeView;
    }

    public void setActiveView(String activeView) {
        this.activeView = activeView;
    }

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            schedules = EntityDAO.getAll(Schedule.class);
        }
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Discipline> getDisciplines() {
        if (disciplines == null) {
            disciplines = EntityDAO.getAll(Discipline.class);
        }
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public List<Department> getDepartments() {
        if (departments == null) {
            departments = EntityDAO.getAll(Department.class);
        }
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Stream> getStreams() {
        if (streams == null) {
            streams = StreamDAO.getAll();
        }
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public List<Group> getGroups() {
        if (groups == null) {
            groups = GroupDAO.getAll();
        }
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Student> getStudents() {
        if (students == null) {
            students = StudentDAO.getAll();
        }
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
