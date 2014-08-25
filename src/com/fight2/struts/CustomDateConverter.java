package com.fight2.struts;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class CustomDateConverter extends StrutsTypeConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(final Map context, final String[] value, final Class toType) {
        Date result = null;
        final Object fromValue = value[0];

        if (fromValue instanceof String && fromValue != null && ((String) fromValue).length() > 0) {
            final String sa = (String) fromValue;
            if (java.util.Date.class == toType) {
                try {
                    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
                    df.setLenient(false); // let's use strict parsing (XW-341)
                    result = df.parse(sa);
                } catch (final ParseException ignore) {
                }
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(final Map arg0, final Object o) {
        String result = null;
        Object fromValue = o;
        if (o instanceof Object[]) {
            final Object[] array = (Object[]) o;
            if (array.length >= 1) {
                fromValue = array[0];
            } else {
                fromValue = null;
            }
        }
        if (fromValue instanceof Date) {
            final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINA);
            result = df.format(fromValue);
        }
        return result;
    }
}
