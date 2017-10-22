package com.grsu.teacherassistant.entities;

import lombok.Data;

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

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}
