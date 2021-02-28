package com.cookbook.util;

import com.cookbook.exceptions.ServiceRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class DateUtil {

    private DateUtil() {}

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String fromDate(Date date) {
        if (date == null) return null;

        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static Date parseDate(String dateStr) {
        if (isNullOrEmpty(dateStr)) return null;

        try {
            return new Date(new SimpleDateFormat(DATE_FORMAT).parse(dateStr).getTime());
        } catch (ParseException e) {
            throw new ServiceRuntimeException(String.format("Date value should be presented " +
                    "in the following format '%s' (provided - '%s'", DATE_FORMAT, dateStr));
        }
    }

}
