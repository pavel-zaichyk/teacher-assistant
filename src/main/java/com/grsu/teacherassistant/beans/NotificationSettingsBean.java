package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.entities.Alarm;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.entities.NotificationSetting;
import com.grsu.teacherassistant.models.AlarmTask;
import com.grsu.teacherassistant.models.NotificationType;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "notificationSettingsBean")
@ViewScoped
@Data
public class NotificationSettingsBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationSettingsBean.class);

    private boolean active = true;

    private Timer timer;

    private Map<String, NotificationSetting> settings;

    public Map<String, NotificationSetting> getSettings() {
        if (settings == null) {
            settings = new HashMap<>();
            for (NotificationSetting s : EntityDAO.getAll(NotificationSetting.class)) {
                settings.put(s.getType().name(), s);
                System.out.println(s);
                System.out.println(settings.get(s.getType().name()));
            }
        }
        return settings;
    }

    public void init() {
        FacesUtils.showDialog(Constants.NOTIFICATION_SETTINGS_DIALOG_NAME);
    }

    public void exit() {
        closeDialog(Constants.NOTIFICATION_SETTINGS_DIALOG_NAME);
    }

    public void save() {
        EntityDAO.save(settings.values());
        update("views");
        exit();
    }

    public void handleFileUpload(FileUploadEvent event) {
        NotificationSetting notificationSetting = settings.get(((FileUpload) event.getSource()).getClientId().split("_")[1]);
        if (event.getFile() != null) {
            notificationSetting.setSound(event.getFile().getContents());
        } else {
            notificationSetting.setSound(null);
        }
    }

    public void play(NotificationSetting notificationSetting) {
        FacesUtils.execute("playAudio('" + notificationSetting.getSoundData() + "', " + notificationSetting.getVolume() + ")");
    }
}
