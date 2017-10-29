package com.grsu.teacherassistant.models;

import com.grsu.teacherassistant.beans.AlarmBean;
import com.grsu.teacherassistant.entities.Alarm;
import com.grsu.teacherassistant.utils.FacesUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * @author Pavel Zaychick
 */
@AllArgsConstructor
public class AlarmTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmTask.class);
    private Alarm alarm;
    private AlarmBean alarmBean;

    @Override
    public void run() {
        if (alarmBean.isActive()) {
            LOGGER.info("==> run(): alarm = " + alarm);
            FacesUtils.push("/audio", new Sound(alarm.getSoundData(), alarm.getVolume()));
        }
    }
}
