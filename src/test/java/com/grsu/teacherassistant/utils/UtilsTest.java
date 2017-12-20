package com.grsu.teacherassistant.utils;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 * @author Pavel Zaychick
 */
public class UtilsTest {
    @Test
    public void parseDouble() throws Exception {
        Locale.setDefault(Locale.US);
        assertEquals(1.0, Utils.parseDouble("1.0", 0), 0);
        assertEquals(0, Utils.parseDouble("test", 0), 0);
        assertEquals(10.0, Utils.parseDouble("1,0", 0), 0);

        Locale.setDefault(new Locale("ru"));
        assertEquals(1.0, Utils.parseDouble("1.0", 0), 0);
        assertEquals(0, Utils.parseDouble("test", 0), 0);
        assertEquals(1.0, Utils.parseDouble("1,0", 0), 0);
    }

}
