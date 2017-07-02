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
 * Created by pavel on 5/29/17.
 */
@ManagedBean(name = "serialBean")
@ViewScoped
@Getter @Setter
public class SerialBean implements Serializable {
	private boolean recordStarted = false;
	private boolean soundEnabled = true;
	private SerialListenerBean currentListener;


	public boolean process(String uid) {
		return currentListener.process(uid);
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
		soundEnabled = true;
	}

	public void disableSound() {
		soundEnabled = false;
	}
}
