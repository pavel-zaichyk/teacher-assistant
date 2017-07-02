package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Discipline;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "disciplineBean")
@ViewScoped
public class DisciplineBean implements Serializable {

	private Discipline selectedDiscipline;
	private Discipline copyOfSelectedDiscipline;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedDiscipline(Discipline selectedDiscipline) {
		this.selectedDiscipline = selectedDiscipline;
		copyOfSelectedDiscipline = this.selectedDiscipline == null ? null : new Discipline(selectedDiscipline);
	}

	public boolean isInfoChanged() {
		return selectedDiscipline != null && !selectedDiscipline.equals(copyOfSelectedDiscipline);
	}

	public List<Discipline> getDisciplines() {
		return sessionBean.getDisciplines();
	}

	public void exit() {
		setSelectedDiscipline(null);
		closeDialog("disciplineDialog");
	}

	public void save() {
		EntityDAO.save(selectedDiscipline);
		sessionBean.updateDisciplines();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		EntityDAO.delete(selectedDiscipline);
		sessionBean.updateDisciplines();
		update("views");
		exit();
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Discipline getSelectedDiscipline() {
		return selectedDiscipline;
	}

	public Discipline getCopyOfSelectedDiscipline() {
		return copyOfSelectedDiscipline;
	}

}
