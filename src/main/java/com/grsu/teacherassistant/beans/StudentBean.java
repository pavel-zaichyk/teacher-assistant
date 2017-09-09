package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.execute;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean implements Serializable {

	private Student selectedStudent;

	private DualListModel<Group> selectedGroups;
	private List<Student> filteredStudents;

	private String url;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public DualListModel<Group> getSelectedGroups() {
		if (selectedStudent == null) {
			setSelectedGroups(new DualListModel<>(sessionBean.getGroups(), Collections.emptyList()));
		} else if (selectedGroups == null) {
			List<Group> sourceGroups = new ArrayList<>(sessionBean.getGroups());
			if (selectedStudent.getGroups() != null) {
				sourceGroups.removeAll(selectedStudent.getGroups());
				setSelectedGroups(new DualListModel<>(sourceGroups, selectedStudent.getGroups()));
			} else {
				setSelectedGroups(new DualListModel<>(sourceGroups, Collections.emptyList()));
			}

		}
		return selectedGroups;
	}

	public void setSelectedGroups(DualListModel<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
		if (selectedStudent != null) {
			selectedStudent.setGroups(selectedGroups == null ? null : selectedGroups.getTarget());
		}
	}

	public List<Student> getStudents() {
		return sessionBean.getStudents();
	}

	public void exit() {
		setSelectedStudent(null);
		setSelectedGroups(null);
		closeDialog("studentDialog");
	}

	public void save() {
		EntityDAO.save(selectedStudent);
		sessionBean.updateStudents();
		closeDialog("studentDialog");
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		EntityDAO.delete(selectedStudent);
		if (filteredStudents != null) {
			filteredStudents.clear();
		}
		sessionBean.updateStudents();
		execute("PF('studentsTable').clearFilters()");
		exit();
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public List<Student> getFilteredStudents() {
		return filteredStudents;
	}

	public void setFilteredStudents(List<Student> filteredStudents) {
		this.filteredStudents = filteredStudents;
	}
}
