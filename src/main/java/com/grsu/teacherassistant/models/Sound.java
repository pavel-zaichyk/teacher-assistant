package com.grsu.teacherassistant.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Pavel Zaychick
 */
@Data
@AllArgsConstructor
public class Sound {
    private String data;

    private double volume;
}
