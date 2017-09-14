package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.GroupDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "studentBean")
@ViewScoped
@Data
public class StudentBean implements Serializable, SerialListenerBean {

    @ManagedProperty(value = "#{serialBean}")
    private SerialBean serialBean;

    private Student student;
    private SerialListenerBean oldSerialListener;


    private DualListModel<Group> groups;

    public void initStudent(Student student) {
        oldSerialListener = serialBean.getCurrentListener();
        serialBean.setCurrentListener(this);
        serialBean.startRecord();

        if (student != null) {
            this.student = student;
        } else {
            this.student = new Student();
            this.student.setGroups(new ArrayList<>());
        }
        List<Group> source = GroupDAO.getAll();
        source.removeAll(this.student.getGroups());
        groups = new DualListModel<>(source, this.student.getGroups());

        FacesUtils.showDialog("studentDialog");
    }

    public void exit() {
        serialBean.setCurrentListener(oldSerialListener);
        student = null;
        oldSerialListener = null;
        closeDialog("studentDialog");
    }

    public void save() {
        student.setGroups(groups.getTarget());
        EntityDAO.save(student);
        update("views");
        exit();
    }

    @Override
    public boolean process(String uid) {
        student.setCardUid(uid);
        return true;
    }
}
