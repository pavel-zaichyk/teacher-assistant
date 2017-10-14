package com.grsu.teacherassistant.beans;

import com.grsu.teacherassistant.utils.FacesUtils;
import com.grsu.teacherassistant.utils.LocaleUtils;
import com.grsu.teacherassistant.utils.SerialUtils;
import lombok.Getter;
import lombok.Setter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * @author Pavel Zaychick
 */
@ManagedBean(name = "serialBean")
@ViewScoped
@Getter @Setter
public class SerialBean implements Serializable {
	private boolean recordStarted = false;
	private boolean soundEnabled = false; //TODO: change to true
	private SerialListenerBean currentListener;


	public boolean process(String uid, String name) {
		return currentListener.process(uid, name);
	}

	public void startRecord() {
		if (!recordStarted) {
			recordStarted = SerialUtils.connect(this);
		}
		if (!recordStarted) {
			LocaleUtils localeUtils = new LocaleUtils();
			FacesUtils.addWarning(
					localeUtils.getMessage("warning"),
					localeUtils.getMessage("warning.device.not.connected.reconnect")
			);
			FacesUtils.update("menuForm:messages");
		}
	}

	public void stopRecord() {
		recordStarted = !SerialUtils.disconnect();
	}

	public void enableSound() {
		soundEnabled = false; //TODO: change to true
	}

	public void disableSound() {
		soundEnabled = false;
	}
}
