package com.grsu.teacherassistant.serial;

import com.grsu.teacherassistant.beans.SerialBean;
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

/*
32 30 31 34 2D 32 C1 C0 D5 C0 D0 20 DE CB B2 DF 20 C0 CB DF CA D1 C0 CD C4 D0 C0 A1 CD C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
2014-2БАХАР ЮЛЂЯ АЛЯКСАНДРАϱНА



32 30 31 34 2D 39 CC C0 B2 D1 C5 C5 C2 C0 20 C0 CB C5 D1 DF 20 C0 CB C5 C3 C0 A1 CD C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
2014-9МАЂСЕЕВА АЛЕСЯ АЛЕГАϱНА

32 30 31 34 2D 31 CA C0 C2 C0 CB DC D7 D3 CA 20 CD C0 D2 C0 CB CB DF 20 C2 B2 CA D2 C0 D0 C0 A1 CD C0 00 00 00 00 00 00 00 00 00 00 00 00 00 00
2014-1КАВАЛЬЧУК НАТАЛЛЯ ВЂКТАРАϱНА

 */
