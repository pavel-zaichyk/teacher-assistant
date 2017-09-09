package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.StreamDAO;
import com.grsu.teacherassistant.entities.Stream;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "streamsBean")
@ViewScoped
@Data
public class StreamsBean implements Serializable {

    private List<Stream> streams;
    private List<Stream> filteredStreams;
    private Stream selectedStream;

    private boolean showClosed;

    public void removeStream(Stream stream) {
        EntityDAO.delete(stream);
        streams.remove(stream);
    }

    public List<Stream> getStreams() {
        if (streams == null) {
            streams = StreamDAO.getAll(showClosed);
        }
        return streams;
    }

    public void search() {
        streams = null;
    }
}
