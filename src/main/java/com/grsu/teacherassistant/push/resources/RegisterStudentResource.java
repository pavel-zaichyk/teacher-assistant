package com.grsu.teacherassistant.push.resources;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/register")
public class RegisterStudentResource {

    @OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String cardUid) {
        return cardUid;
    }
}
