package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.GroupDAO;
import com.grsu.teacherassistant.entities.Group;
import com.grsu.teacherassistant.entities.Stream;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

@ManagedBean(name = "streamBean")
@ViewScoped
@Data
public class StreamBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamBean.class);

    private Stream stream;
    private DualListModel<Group> groups;

    public void initStream(Stream stream) {
        if (stream != null) {
            this.stream = stream;
        } else {
            this.stream = new Stream();
            this.stream.setGroups(new ArrayList<>());
            this.stream.setActive(true);
        }
        List<Group> source = GroupDAO.getAll();
        source.removeAll(this.stream.getGroups());
        groups = new DualListModel<>(source, this.stream.getGroups());

        FacesUtils.showDialog("streamDialog");
    }

    public void exit() {
        stream = null;
        closeDialog("streamDialog");
    }

    public void save() {
        stream.setGroups(groups.getTarget());
        EntityDAO.save(stream);
        update("views");
        exit();
    }

}
