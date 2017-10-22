package com.grsu.teacherassistant.push.resources;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * @author Pavel Zaychick
 */
@PushEndpoint("/audio")
public class AudioResource {

    @OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String soundPath) {
        return soundPath;
    }
}
