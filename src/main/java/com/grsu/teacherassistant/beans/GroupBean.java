package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;

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
public class GroupBean implements Serializable {

	private Group selectedGroup;
	private Group copyOfSelectedGroup;

	private List<Student> groupStudents;
	private List<Student> filteredGroupStudents;

	private String dialogAction;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public boolean isInfoChanged() {
		return selectedGroup != null && !selectedGroup.equals(copyOfSelectedGroup);
	}

	/*
		GROUPS
	*/
	public void exitInfo() {
		setSelectedGroup(null);
		setDialogAction(null);
		closeDialog("groupInfoDialog");
	}

	public void save() {
		EntityDAO.save(selectedGroup);
		sessionBean.updateGroups();
		update("views");
	}

	public void saveAndExit() {
		save();
		exitInfo();
	}

	public void deleteGroup() {
		EntityDAO.delete(selectedGroup);
		sessionBean.updateStudents();
		sessionBean.updateGroups();
		update("views");
		exitInfo();
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
		selectedGroup.getStudents().add(student);
		EntityDAO.update(selectedGroup);
		groupStudents.remove(student);
		if (filteredGroupStudents != null) {
			filteredGroupStudents.remove(student);
		}
	}

	public void deleteStudent(Student student) {
		selectedGroup.getStudents().remove(student);
		EntityDAO.update(selectedGroup);
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
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
		copyOfSelectedGroup = selectedGroup == null ? null : new Group(selectedGroup);

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
