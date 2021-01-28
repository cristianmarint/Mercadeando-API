package com.api.mercadeando.domain.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * @author cristianmarint
 * @Date 2021-01-14 11:47
 */
public class formatDates {
    public static String instantToString(Instant instant) {
        java.util.Date myDate = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }
}
