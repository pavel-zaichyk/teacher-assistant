package com.grsu.teacherassistant.models;

import lombok.Data;

/**
 * @author Pavel Zaychick
 */
@Data
public class Notification {
    private String title;
    private String body;
    private String image;
    private long timeout;
}
