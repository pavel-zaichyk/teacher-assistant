package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.beans.utility.SerialBean;
import com.grsu.teacherassistant.beans.utility.SerialListenerBean;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.GroupDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Student;
import com.grsu.teacherassistant.push.resources.PushMessage;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;

@ManagedBean(name = "studentBean")
@ViewScoped
@Data
public class StudentBean implements Serializable, SerialListenerBean {

    @ManagedProperty(value = "#{serialBean}")
    private SerialBean serialBean;

    private Student student;

    private DualListModel<Group> groups;

    private boolean recordStarted = false;
    private SerialListenerBean oldSerialListener;
    private boolean oldRecordStarted;

    private Group[] selectedPraepostorGroups;

    private String updateId = "views";

    public void initStudent(Student student) {
        oldSerialListener = serialBean.getCurrentListener();
        oldRecordStarted = serialBean.isRecordStarted();

        if (student != null) {
            this.student = student;
            selectedPraepostorGroups = student.getPraepostorGroups().toArray(new Group[]{});
        } else {
            this.student = new Student();
            this.student.setGroups(new ArrayList<>());
            this.student.setPraepostorGroups(new ArrayList<>());
        }
        List<Group> source = GroupDAO.getAll();
        source.removeAll(this.student.getGroups());
        groups = new DualListModel<>(source, this.student.getGroups());

        FacesUtils.showDialog("studentDialog");
    }

    public void exit() {
        stopRecord();
        student = null;
        oldSerialListener = null;
        selectedPraepostorGroups = null;
        updateId = "views";
        closeDialog("studentDialog");
    }

    public void save() {
        student.setGroups(groups.getTarget());
        student.getPraepostorGroups().parallelStream().forEach(g -> g.setPraepostor(null));
        student.setPraepostorGroups(new ArrayList<>());
        Stream.of(selectedPraepostorGroups).forEach(g -> {
            student.getPraepostorGroups().add(g);
            g.setPraepostor(student);
        });
        EntityDAO.save(student.getPraepostorGroups());
        EntityDAO.save(student);
        exit();
    }

    @Override
    public boolean process(String uid, String name) {
        student.setCardUid(uid);
        FacesUtils.push("/register", new PushMessage(uid));
        return true;
    }

    public void startRecord() {
        serialBean.setCurrentListener(this);
        serialBean.startRecord();
        recordStarted = serialBean.isRecordStarted();
    }

    public void stopRecord() {
        recordStarted = false;
        serialBean.setCurrentListener(oldSerialListener);
        serialBean.setRecordStarted(oldRecordStarted);
        if (!serialBean.isRecordStarted()) {
            serialBean.stopRecord();
        }
    }
}
