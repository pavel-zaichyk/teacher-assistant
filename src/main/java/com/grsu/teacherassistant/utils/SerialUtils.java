package com.grsu.teacherassistant.utils;

import com.grsu.teacherassistant.beans.utility.SerialBean;
import com.grsu.teacherassistant.serial.SerialListener;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static com.grsu.teacherassistant.constants.Constants.*;

public class SerialUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerialUtils.class);

	private static SerialPort serialPort;
	private static Thread shutdownHook;
	private static final String EXCEPTION_PORT_NOT_FOUND = "Port not found";
	private static final String EXCEPTION_PORT_BUSY = "Port busy";

    public static void sendResponse(SerialPort serialPort, boolean success, boolean soundEnabled) throws SerialPortException {
        final long t = System.currentTimeMillis();
        LOGGER.info("==> sendResponse(); success = " + success + "; soundEnabled = " + soundEnabled);
        if (success && soundEnabled) {
            serialPort.writeString(SERIAL_STATUS_OK);
        }
        if (success && !soundEnabled) {
            serialPort.writeString(SERIAL_STATUS_OL);
        }
        if (!success && soundEnabled) {
            serialPort.writeString(SERIAL_STATUS_ER);
        }
        if (!success && !soundEnabled) {
            serialPort.writeString(SERIAL_STATUS_EL);
        }
        LOGGER.info("<== sendResponse()" + (System.currentTimeMillis() - t));
    }

	public static boolean disconnect() {
		if (serialPort != null && serialPort.isOpened()) {
			try {
				serialPort.closePort();
//				Runtime.getRuntime().removeShutdownHook(shutdownHook);
				LOGGER.info("Reader disconnected");
			} catch (SerialPortException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalStateException | SecurityException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean connect(SerialBean serialBean) {
		boolean connected = false;
		String lastConnectionPort = PropertyUtils.getProperty("last.connection.port");
		if (lastConnectionPort != null && !lastConnectionPort.isEmpty()) {
			LOGGER.info("Found previously saved port [ " + lastConnectionPort + " ]. Trying to connect...");
			connected = connect(lastConnectionPort, serialBean);
		}
		return connected || connect(findReader(), serialBean);
	}

	private static boolean connect(String port, SerialBean serialBean) {
		return connect(port, serialBean, true);
	}

	private static boolean connect(String port, SerialBean serialBean, boolean retryIfPortBusy) {
		if (port == null) return false;

		serialPort = new SerialPort(port);
		try {
			//Открываем порт
			serialPort.openPort();
			//Выставляем параметры
			serialPort.setParams(
					SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
					false, false);
			//Включаем аппаратное управление потоком
			//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
			//                              SerialPort.FLOWCONTROL_RTSCTS_OUT);
			//upd: Отключаем так как на windows не работает порт нормально
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			//Устанавливаем ивент лисенер и маску
			Thread.sleep(1000);
			serialPort.addEventListener(
					new SerialListener(serialPort, serialBean),
					SerialPort.MASK_RXCHAR
			);
			LOGGER.info("Serial port listener added. Port: " + port);

			//Отправляем запрос устройству
			Thread.sleep(1000);
			serialPort.writeString(SERIAL_SET_START);

			shutdownHook = getShutdownHook(serialPort);
			Runtime.getRuntime().addShutdownHook(shutdownHook);
		} catch (SerialPortException e) {
			if (EXCEPTION_PORT_NOT_FOUND.equals(e.getExceptionType())) {
				LOGGER.info("Connection failed. Previously saved port not found.");
			} else if (EXCEPTION_PORT_BUSY.equals(e.getExceptionType())) {
				if (retryIfPortBusy) {
					LOGGER.info("Port [ " + serialPort.getPortName() + " ] is busy. Trying to reconnect...");
					disconnect();
					connect(port, serialBean, false);
				} else {
					LOGGER.info("Connection failed. Port [ " + serialPort.getPortName() + " ] is busy.");
				}
			} else {
				e.printStackTrace();
			}
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		PropertyUtils.setProperty("last.connection.port", port);
		return true;
	}

	private static String findReader() {
		LOGGER.info("Reader search started.");
		String foundAt = null;
		for (String portName : getPortNames()) {
			final SerialPort serialPort = new SerialPort(portName);
			try {
				serialPort.openPort();
				serialPort.setParams(
						SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
						false, false);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

				Thread.sleep(1500);
				serialPort.writeString(SERIAL_GET_INFO);

				Thread.sleep(500);
				String info = serialPort.readString();

				serialPort.closePort();

				if (info != null && info.startsWith(SERIAL_READER_INFO_PREFIX)) {
					foundAt = portName;
					break;
				} else {
					LOGGER.info("Port [ " + portName + " ] unsuccessful. Reason: No response");
				}
			} catch (SerialPortException ex) {
				LOGGER.info("Port [ " + portName + " ] unsuccessful. Reason: " + ex.getExceptionType());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (foundAt != null) {
			LOGGER.info("Reader found at port: " + foundAt);
		} else {
			LOGGER.info("Reader not found.");
		}
		return foundAt;
	}

	private static String[] getPortNames() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return SerialPortList.getPortNames("/dev/", Pattern.compile("cu."));
		}
		return SerialPortList.getPortNames();
	}

	private static Thread getShutdownHook(final SerialPort serialPort) {
		return new Thread() {
			@Override
			public void run() {
				if (serialPort != null) {
					try {
						serialPort.closePort();
						LOGGER.info("Serial port closed by shutdown hook!");
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
