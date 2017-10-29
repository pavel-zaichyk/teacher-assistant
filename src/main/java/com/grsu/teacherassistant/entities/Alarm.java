package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Base64;

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

    private byte[] sound;

    private double volume = 1.0;

    public String getSoundData() {
        if (sound != null && sound.length > 0) {
            return "data:audio/mpeg;base64," + new String(Base64.getEncoder().encode(sound));
        } else {
            return Constants.DEFAULT_ALARM_SOUND;
        }
    }

    @Override
    public String toString() {
        return "Alarm{" +
            "id=" + id +
            ", active=" + active +
            ", time=" + time +
            '}';
    }
}
