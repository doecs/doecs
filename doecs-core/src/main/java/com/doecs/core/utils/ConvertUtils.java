package com.doecs.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConvertUtils {
    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class "
                        + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }
    public static Integer toInteger(Object value) {
        Integer ret = null;
        if (value != null) {
            if (value instanceof Integer) {
                ret = (Integer) value;
            } else if (value instanceof String) {
                ret = Integer.valueOf((String) value);
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class "
                        + value.getClass() + " into a Integer.");
            }
        }
        return ret;
    }
}
