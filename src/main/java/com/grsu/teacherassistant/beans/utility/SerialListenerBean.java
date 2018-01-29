package com.grsu.teacherassistant.beans.utility;

import com.grsu.teacherassistant.serial.SerialStatus;

/**
 * @author Pavel Zaychick
 */
public interface SerialListenerBean {
    SerialStatus process(String uid, String name);
}
