package com.grsu.teacherassistant.entities;

import lombok.Data;
import org.primefaces.model.UploadedFile;

import javax.persistence.*;

/**
 * @author Pavel Zaychick
 */
@Entity
@Data
public class Alarm implements AssistantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean active;

    private Integer time;

    private String description;

    private String sound;

    @Transient
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
//    public boolean isActive() {
//        return Boolean.TRUE.equals(active);
//    }
}
