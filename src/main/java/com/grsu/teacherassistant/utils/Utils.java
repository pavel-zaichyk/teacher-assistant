package com.grsu.teacherassistant.utils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Pavel Zaychick
 */
public class Utils {
    public static double parseDouble(String string, double def) {
        if (string == null) {
            return def;
        }
        try {
            NumberFormat format = NumberFormat.getInstance();
            Number number = format.parse(string);
            return number.doubleValue();
        } catch (ParseException e) {
            return def;
        }
    }
}
