package com.grsu.teacherassistant.push.resources;

import com.grsu.teacherassistant.models.Notification;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 * @author Pavel Zaychick
 */
@PushEndpoint("/notify")
public class NotificationResource {

    @OnMessage(encoders = {JSONEncoder.class})
    public Notification onMessage(Notification notification) {
        return notification;
    }
}
