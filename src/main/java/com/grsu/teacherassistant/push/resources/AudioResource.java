package com.grsu.teacherassistant.push.resources;

import com.grsu.teacherassistant.models.Sound;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * @author Pavel Zaychick
 */
@PushEndpoint("/audio")
public class AudioResource {

    @OnMessage(encoders = {JSONEncoder.class})
    public Sound onMessage(Sound sound) {
        return sound;
    }
}
