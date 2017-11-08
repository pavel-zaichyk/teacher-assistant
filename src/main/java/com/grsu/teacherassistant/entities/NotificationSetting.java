package com.grsu.teacherassistant.entities;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.models.NotificationType;
import lombok.Data;

import javax.persistence.*;
import java.util.Base64;

/**
 * @author Pavel Zaychick
 */
@Entity
@Table(name = "NOTIFICATION_SETTING")
@Data
public class NotificationSetting implements AssistantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean active;

    private Integer data;

    private double volume;

    private byte[] sound;

    public String getSoundData() {
        if (sound != null && sound.length > 0) {
            return "data:audio/mpeg;base64," + new String(Base64.getEncoder().encode(sound));
        } else {
            return Constants.DEFAULT_NOTIFICATION_SOUND;
        }
    }

    @Override
    public String toString() {
        return "NotificationSetting{" +
            "id=" + id +
            ", type=" + type +
            ", active=" + active +
            ", data=" + data +
            ", volume=" + volume +
            '}';
    }
}
