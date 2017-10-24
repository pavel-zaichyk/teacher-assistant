package com.grsu.teacherassistant.dao;

import com.grsu.teacherassistant.constants.Constants;
import com.grsu.teacherassistant.entities.Alarm;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Pavel Zaychick
 */
public class AlarmDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmDAO.class);

    public static List<Alarm> getAll() {
        List<Alarm> alarms = EntityDAO.getAll(Alarm.class);

        if (alarms != null) {
            for (Alarm alarm : alarms) {
                alarm.setFile(getFile(alarm.getSound()));
            }
        }

        return alarms;
    }

    public static void save(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            if (alarm.getFile() != null) {
                alarm.setSound(alarm.getFile().getFileName());
            } else {
                alarm.setSound(null);
            }
        }
        EntityDAO.update(alarms);
    }

    private static UploadedFile getFile(String s) {
        if (s != null) {
            UploadedFile uploadedFile = new DefaultUploadedFile();
            try {
                uploadedFile.write(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uploadedFile;
        }
        return null;
    }
}
