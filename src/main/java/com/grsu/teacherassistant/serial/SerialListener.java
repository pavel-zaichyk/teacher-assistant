package com.grsu.teacherassistant.serial;

import com.grsu.teacherassistant.beans.SerialBean;
import com.grsu.teacherassistant.utils.SerialUtils;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.grsu.teacherassistant.constants.Constants.*;

public class SerialListener implements SerialPortEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerialListener.class);

	private SerialPort serialPort;
	private SerialBean serialBean;

	public SerialListener(SerialPort serialPort, SerialBean serialBean) {
		this.serialPort = serialPort;
		this.serialBean = serialBean;
	}

	public void serialEvent(SerialPortEvent event) {
	    final long t = System.currentTimeMillis();
        LOGGER.info("==>");
        LOGGER.info("==>");
	    LOGGER.info("==> serialEvent()");
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			LOGGER.info("---");
			try {
				//Получаем ответ от устройства, обрабатываем данные и т.д.
				String data = serialPort.readString(event.getEventValue());
				LOGGER.info("Data received: ' " + data + " '");
				//И снова отправляем запрос
				if (data.startsWith(SERIAL_CARD_UID_PREFIX)) {
					String uid = data.replace(SERIAL_CARD_UID_PREFIX, "").substring(0, 8);
					LOGGER.info("Received card with uid: " + uid);
					SerialUtils.sendResponse(
							serialPort,
							serialBean.process(uid),
							serialBean.isSoundEnabled()
					);

					/*Student student = EntityUtils.getPersonByUid(lessonBean.getAbsentStudents(), uid);
					if (student == null) {
						if (EntityUtils.getPersonByUid(lessonBean.getPresentStudents(), uid) != null) {
							LOGGER.info("Student not registered. Reason: Uid[ " + uid + " ] already exists.");
							return;
						} else {
							student = EntityUtils.getPersonByUid(lessonBean.getAllStudents(), uid);
						}
					}
					if (student != null) {
						SerialUtils.sendResponse(
								serialPort,
								lessonBean.processStudent(student),
								lessonBean.isSoundEnabled()
						);
					} else {
						LOGGER.info("Student not registered. Reason: Uid[ " + uid + " ] not exist in database.");
						SerialUtils.sendResponse(
								serialPort,
								false,
								lessonBean.isSoundEnabled()
						);
					}*/
				}
			} catch (SerialPortException ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}

        LOGGER.info("<== serialEvent()" + (System.currentTimeMillis() - t));
        LOGGER.info("<==");
        LOGGER.info("<==");
    }
}
