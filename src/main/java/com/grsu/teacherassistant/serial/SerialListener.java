package com.grsu.teacherassistant.serial;

import com.grsu.teacherassistant.beans.utility.SerialBean;
import com.grsu.teacherassistant.utils.SerialUtils;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static com.grsu.teacherassistant.constants.Constants.*;

public class SerialListener implements SerialPortEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialListener.class);

    private SerialPort serialPort;
    private SerialBean serialBean;

    public SerialListener(SerialPort serialPort, SerialBean serialBean) {
        this.serialPort = serialPort;
        this.serialBean = serialBean;
    }

    private StringBuilder readedString;
    private String uid;

    public void serialEvent(SerialPortEvent event) {
        final long t = System.currentTimeMillis();
        LOGGER.info("==> serialEvent()");
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            LOGGER.info("---");
            try {
                //Получаем ответ от устройства, обрабатываем данные и т.д.
                String data = serialPort.readString(event.getEventValue())
                    .replaceAll("\r", "")
                    .replaceAll("\n", "");
                LOGGER.info("Data received: '" + data + "'");
                //И снова отправляем запрос
                if (data.startsWith(SERIAL_CARD_UID_PREFIX)) {
                    uid = data.replace(SERIAL_CARD_UID_PREFIX, "").substring(0, 8);
                    LOGGER.info("Received card with uid: " + uid);
                } else {
                    if (readedString == null && !data.isEmpty() && !data.trim().startsWith("00") && !data.trim().startsWith("0 ")) {
                        readedString = new StringBuilder();
                    }
                    if (readedString != null) {
                        readedString.append(data);
                        System.out.println(readedString.toString());
                        if (readedString.toString().trim().endsWith("00")) {
                            System.out.println("Finish: " + readedString.toString());
                            SerialUtils.sendResponse(
                                serialPort,
                                serialBean.process(uid, decodeHexToText(readedString.toString().replaceAll(" ", ""))),
                                serialBean.isSoundEnabled()
                            );
                            readedString = null;
                            uid = null;
                        }
                    }
                }


            } catch (SerialPortException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        LOGGER.info("<== serialEvent()" + (System.currentTimeMillis() - t));
    }

    private String decodeHexToText(String hexString) {
        try {
            byte[] bytes = Hex.decodeHex(hexString.toCharArray());
            return new String(bytes, "Cp1251").substring(6);
        } catch (DecoderException | UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }
}
