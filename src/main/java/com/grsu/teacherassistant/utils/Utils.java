package com.grsu.teacherassistant.utils;

/**
 * @author Pavel Zaychick
 */
public class Utils {
    public static double parseDouble(String string, double def) {
        if (string == null) {
            return def;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            return def;
        }
    }
}
