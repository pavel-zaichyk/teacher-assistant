package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.utils.FacesUtils;
import com.grsu.teacherassistant.utils.LocaleUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "menuBean")
@ViewScoped
public class MenuBean implements Serializable {

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	private boolean showMenu = true;

/*
	public void save() {
		addMessage("Success", "Data saved");
	}

	public void update() {
		addMessage("Success", "Data updated");
	}

	public void delete() {
		addMessage("Success", "Data deleted");
	}

	public void quit() {
		FacesUtils.addInfo("Quit", "Some quit action");
	}
*/

	public void loadCSV() {
		List<Group> processedGroups = new ArrayList<>(sessionBean.updateGroupsFromCSV());
		LocaleUtils localeUtils = new LocaleUtils();

		if (processedGroups.isEmpty()) {
			FacesUtils.addInfo(
					localeUtils.getMessage("info"),
					localeUtils.getMessage("info.groups.not.addedOrUpdated")
			);
			FacesUtils.update("menuForm:messages");
		} else {
			sessionBean.updateEntities();

			StringBuilder sb = new StringBuilder(localeUtils.getMessage("info.groups.addedOrUpdated"));
			for (Group group : processedGroups) {
				sb.append("<br/>");
				sb.append(localeUtils.getMessage("info.group.addedOrUpdated", group.getName(), group.getStudents().size()));
			}

			FacesUtils.addInfo(
					localeUtils.getMessage("info"),
					sb.toString()
			);

			FacesUtils.update("wrapper");
			if ("students".equals(sessionBean.getActiveView())) {
				FacesUtils.execute("PF('studentsTable').clearFilters()");
			}
		}
	}

	public void connect() {
		sessionBean.connect();
	}

	public void disconnect() {
		sessionBean.disconnect();
	}

	public void changeView(String viewName) {
		sessionBean.setActiveView(viewName);
	}

	public void loadStudentsPhoto() {
		sessionBean.loadStudentsPhoto();
	}

	public void showMenu() {
		setShowMenu(true);
	}

	public void hideMenu() {
		setShowMenu(false);
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
}
