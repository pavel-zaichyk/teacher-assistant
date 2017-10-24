package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.dao.AlarmDAO;
import com.grsu.teacherassistant.dao.EntityDAO;
import com.grsu.teacherassistant.dao.LessonDAO;
import com.grsu.teacherassistant.entities.Alarm;
import com.grsu.teacherassistant.entities.Lesson;
import com.grsu.teacherassistant.models.AlarmTask;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.Data;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static com.grsu.teacherassistant.utils.FacesUtils.closeDialog;
import static com.grsu.teacherassistant.utils.FacesUtils.update;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "alarmBean")
@ViewScoped
@Data
public class AlarmBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmBean.class);

    private boolean active = true;

    public void changeActive() {
        active = !active;
        setAlarms();
    }

    private Timer timer;

    private List<Alarm> alarms;

    public List<Alarm> getAlarms() {
        if (alarms == null) {
            alarms = AlarmDAO.getAll();
        }
        return alarms;
    }

    public void setAlarms() {
        LOGGER.info("==> setAlarms();");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        if (active) {
            List<Lesson> lessons = LessonDAO.getAll(LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), false, null);

            for (Lesson lesson : lessons) {
                if (lesson.getDate().toLocalDate().equals(LocalDate.now()) && lesson.getSchedule().getEnd().isAfter(LocalTime.now())) {
                    for (Alarm alarm : getAlarms()) {
                        if (Boolean.TRUE.equals(alarm.getActive())) {
                            Date startTime = new Date(lesson.getSchedule().getBegin().atDate(LocalDate.now()).plusMinutes(alarm.getTime()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                            if (startTime.getTime() > new Date().getTime()) {
                                timer.schedule(new AlarmTask(alarm, this), startTime);
                                LOGGER.info("Create alarm task: startTime = " + startTime + " alarm = " + alarm);
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("<== setAlarms();");
    }

    public void initAlarm() {
        FacesUtils.showDialog("alarmsDialog");
    }

    public void exit() {
        closeDialog("alarmsDialog");
    }

    public void save() {
        AlarmDAO.save(alarms);
        update("views");
        exit();
    }

    public void handleFileUpload(FileUploadEvent event) {
        LOGGER.info("!!!!!!!!!!!!!!");
        LOGGER.info("!!!!!!!!!!!!!!");
        LOGGER.info("!!!!!!!!!!!!!!");
        LOGGER.info("!!!!!!!!!!!!!!");
LOGGER.info(event.getFile().getFileName());
        LOGGER.info("!!!!!!!!!!!!!!");
        LOGGER.info("!!!!!!!!!!!!!!");
        LOGGER.info("!!!!!!!!!!!!!!");
    }
}
