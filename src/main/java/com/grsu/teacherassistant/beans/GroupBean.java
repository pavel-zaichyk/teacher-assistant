package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.beans.mode.RegistrationModeBean;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "groupBean")
@ViewScoped
@Data
public class GroupBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupBean.class);

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    private Group group;
    private DualListModel<Student> students;

    public void initGroup(Group group) {
        if (group != null) {
            this.group = group;
        } else {
            this.group = new Group();
            this.group.setStudents(new ArrayList<>());
        }
        LOGGER.info("!!!!!!Start loading students");
        students = new DualListModel<>(sessionBean.getStudents(), this.group.getStudents());
        LOGGER.info("!!!!!!End loading students");

        FacesUtils.showDialog("groupInfoDialog");
    }

    public void exit() {
        group = null;
        closeDialog("groupInfoDialog");
    }

    public void save() {
        group.setStudents(students.getTarget());
        EntityDAO.save(group);
        update("views");
        exit();
    }

    private List<Student> groupStudents;
    private List<Student> filteredGroupStudents;

    private String dialogAction;





    public void saveAndExit() {
        save();
    }


    /*
        STUDENTS
    */
    public void exitStudents() {
        setSelectedGroup(null);
        setDialogAction(null);
        setFilteredGroupStudents(null);
        sessionBean.updateStudents();
        closeDialog("groupStudentsDialog");
    }

    public void addStudent(Student student) {
        group.getStudents().add(student);
        EntityDAO.update(group);
        groupStudents.remove(student);
        if (filteredGroupStudents != null) {
            filteredGroupStudents.remove(student);
        }
    }

    public void deleteStudent(Student student) {
        group.getStudents().remove(student);
        EntityDAO.update(group);
        groupStudents.remove(student);
        if (filteredGroupStudents != null) {
            filteredGroupStudents.remove(student);
        }
    }

    /*
        GETTERS & SETTERS
     */
    public List<Group> getGroups() {
        return sessionBean.getGroups();
    }

    public Group getSelectedGroup() {
        return group;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.group = selectedGroup;

        if (selectedGroup != null) {
            if (selectedGroup.getId() == null) {
                groupStudents = Collections.emptyList();
            } else {
                if ("add".equals(dialogAction)) {
                    List<Student> allStudents = new ArrayList<>(sessionBean.getStudents());
                    List<Student> studentsFromGroup = new ArrayList<>(selectedGroup.getStudents());
                    allStudents.removeAll(studentsFromGroup);
                    groupStudents = allStudents;
                } else if ("delete".equals(dialogAction)) {
                    groupStudents = selectedGroup.getStudents();
                }
            }
        }
    }

    public List<Student> getGroupStudents() {
        return groupStudents;
    }

    public void setGroupStudents(List<Student> groupStudents) {
        this.groupStudents = groupStudents;
    }

    public List<Student> getFilteredGroupStudents() {
        return filteredGroupStudents;
    }

    public void setFilteredGroupStudents(List<Student> filteredGroupStudents) {
        this.filteredGroupStudents = filteredGroupStudents;
    }

    public String getDialogAction() {
        return dialogAction;
    }

    public void setDialogAction(String dialogAction) {
        this.dialogAction = dialogAction;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
