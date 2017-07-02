package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Stream;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "streamBean")
@ViewScoped
public class StreamBean implements Serializable {

	private Stream selectedStream;
	private Stream copyOfSelectedStream;

	private DualListModel<Group> selectedGroups;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public void setSelectedStream(Stream selectedStream) {
		this.selectedStream = selectedStream;
		copyOfSelectedStream = this.selectedStream == null ? null : new Stream(selectedStream);
	}

	public DualListModel<Group> getSelectedGroups() {
		if (selectedStream == null) {
			setSelectedGroups(new DualListModel<>(sessionBean.getGroups(), Collections.emptyList()));
		} else if (selectedGroups == null) {
			List<Group> sourceGroups = new ArrayList<>(sessionBean.getGroups());
			if (selectedStream.getGroups() != null) {
				sourceGroups.removeAll(selectedStream.getGroups());
				setSelectedGroups(new DualListModel<>(sourceGroups, selectedStream.getGroups()));
			} else {
				setSelectedGroups(new DualListModel<>(sourceGroups, Collections.emptyList()));
			}

		}
		return selectedGroups;
	}

	public void setSelectedGroups(DualListModel<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
		if (selectedStream != null) {
			selectedStream.setGroups(selectedGroups == null ? null : selectedGroups.getTarget());
		}
	}

	public boolean isInfoChanged() {
		return selectedStream != null && !selectedStream.equals(copyOfSelectedStream);
	}

	public List<Stream> getStreams() {
		return sessionBean.getStreams();
	}

	public void exit() {
		setSelectedStream(null);
		setSelectedGroups(null);
		closeDialog("streamDialog");
	}

	public void save() {
		EntityDAO.save(selectedStream);
		sessionBean.updateStreams();
		update("views");
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public void delete() {
		EntityDAO.delete(selectedStream);
		sessionBean.updateStreams();
		update("views");
		exit();
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Stream getSelectedStream() {
		return selectedStream;
	}

	public Stream getCopyOfSelectedStream() {
		return copyOfSelectedStream;
	}

}
