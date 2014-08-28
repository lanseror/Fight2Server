package com.fight2.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

    public static String getRemainTime(final Date endDate) {
        long millis = endDate.getTime() - System.currentTimeMillis();

        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        return String.format("%d天  %s:%s", days, DECIMAL_FORMAT.format(hours), DECIMAL_FORMAT.format(minutes));
    }

    public static int getRemainTimeInSecond(final Date endDate) {
        final long millis = endDate.getTime() - System.currentTimeMillis();
        final int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);
        return seconds;
    }
}
