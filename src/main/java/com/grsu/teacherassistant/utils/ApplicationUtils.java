package com.grsu.teacherassistant.utils;

import static com.grsu.teacherassistant.utils.PropertyUtils.*;

/**
 * @author Pavel Zaychick
 */
public class ApplicationUtils {
    private static final double EXAM_MARK_WEIGHT = 0.6;

    /**
     * Calculate exam mark weight for total mark.
     * Read this value from property file, if the value doesn't found then return default exam mark weight.
     *
     * @return exam mark weight
     */
    public static double examMarkWeight() {
        return Utils.parseDouble(getProperty(EXAM_MARK_WEIGHT_PROPERTY_NAME), EXAM_MARK_WEIGHT);
    }

    /**
     * Calculate attestation mark weight for total mark.
     *
     * @return attestation mark weight
     */
    public static double attestationMarkWeight() {
        return 1 - examMarkWeight();
    }
}
