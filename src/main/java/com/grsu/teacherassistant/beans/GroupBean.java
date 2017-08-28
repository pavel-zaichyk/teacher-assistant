package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StudentDAO;
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
            this.group.setActive(true);
        }
        LOGGER.info("!!!!!!Start loading students");
        List<Student> source = StudentDAO.getAll();
        source.removeAll(this.group.getStudents());
        students = new DualListModel<>(source, this.group.getStudents());
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

}
