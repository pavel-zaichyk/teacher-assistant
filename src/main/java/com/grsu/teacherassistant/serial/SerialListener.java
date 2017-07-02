package com.grsu.teacherassistant.serial;

import com.grsu.teacherassistant.beans.SerialBean;
import com.grsu.teacherassistant.utils.SerialUtils;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import static com.grsu.teacherassistant.constants.Constants.*;

public class SerialListener implements SerialPortEventListener {
	private SerialPort serialPort;
	private SerialBean serialBean;

	public SerialListener(SerialPort serialPort, SerialBean serialBean) {
		this.serialPort = serialPort;
		this.serialBean = serialBean;
	}

	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			System.out.println("---");
			try {
				//Получаем ответ от устройства, обрабатываем данные и т.д.
				String data = serialPort.readString(event.getEventValue());
				System.out.println("Data received: ' " + data + " '");
				//И снова отправляем запрос
				if (data.startsWith(SERIAL_CARD_UID_PREFIX)) {
					String uid = data.replace(SERIAL_CARD_UID_PREFIX, "").substring(0, 8);
					System.out.println("Received card with uid: " + uid);
					SerialUtils.sendResponse(
							serialPort,
							serialBean.process(uid),
							serialBean.isSoundEnabled()
					);

					/*Student student = EntityUtils.getPersonByUid(lessonBean.getAbsentStudents(), uid);
					if (student == null) {
						if (EntityUtils.getPersonByUid(lessonBean.getPresentStudents(), uid) != null) {
							System.out.println("Student not registered. Reason: Uid[ " + uid + " ] already exists.");
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
						System.out.println("Student not registered. Reason: Uid[ " + uid + " ] not exist in database.");
						SerialUtils.sendResponse(
								serialPort,
								false,
								lessonBean.isSoundEnabled()
						);
					}*/
				}
			} catch (SerialPortException ex) {
				System.out.println(ex);
			}
		}
	}
}
