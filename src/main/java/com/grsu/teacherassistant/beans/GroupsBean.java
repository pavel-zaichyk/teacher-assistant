package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.entities.Group;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "groupsBean")
@ViewScoped
@Data
public class GroupsBean implements Serializable {
    private List<Group> groups;
    private List<Group> filteredGroups;
    private Group selectedGroup;

    public List<Group> getGroups() {
        if (groups == null) {
            groups = EntityDAO.getAll(Group.class);
        }
        return groups;
    }
}
