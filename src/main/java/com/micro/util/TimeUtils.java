package com.micro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.UnknownFormatFlagsException;

/**
 * 时间工具
 *
 * @author CC Zhao
 * @date 2019/09/11
 */
public class TimeUtils {

    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String yyyyMMddHHmmss = "yyyyMMdd HH:mm:ss";

    public static final String yyyyMMddHHmm = "yyyyMMdd HH:mm";

    public static final long ONE_DAY_SECONDS = 24 * 60 * 60 * 1000;
    public static final long ONE_DAY_MILLISECONDS = ONE_DAY_SECONDS * 1000;
    public static final int ONE_DAY_HOURS = 24;
    public static final int DAY_OF_WEEK = 7;

    /**
     * Formats date/time depend on type of object into a time string.
     *
     * @param o
     * @param pattern
     * @return
     */
    public static String format2StrRoute(Object o, String pattern) {
        try {
            if (o instanceof LocalDateTime) {
                return localTimeDate2Str((LocalDateTime) o, pattern);
            } else if (o instanceof Date) {
                return date2Str((Date) o, pattern);
            } else if (o instanceof LocalDate) {
                return localDate2Str((LocalDate) o, pattern);
            } else if (o instanceof LocalTime) {
                return localTime2Str((LocalTime) o, pattern);
            } else if (o instanceof Long) {
                // formats timestamp(unix time) into a time string
                return date2Str(new Date((Long) o), pattern);
            }
        } catch (Exception e) {
            throw new UnknownFormatFlagsException("unknown type of object");
        }
        return null;
    }

    /**
     * Official tip: this data-time will be passed to the formatter to product a string.
     *
     * @param time    date-time
     * @param pattern the pattern that you want
     * @return product a string
     */
    public static String localTimeDate2Str(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDate2Str(LocalDate time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localTime2Str(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Formats a date/time into a time string
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    // 字符串转时间对象

    /**
     * Formats time string into date.
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String time) throws ParseException {
        return new SimpleDateFormat().parse(time);
    }

    /**
     * Formats timestamp(unix time) into date.
     *
     * @param timestamp
     * @return
     */
    public static Date timestamp2Date(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * Formats time string into localtime.
     *
     * @param time
     * @param pattern
     * @return
     */
    public static LocalTime str2LocalTime(String time, String pattern) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Formats time string into localDate.
     *
     * @param time
     * @param pattern
     * @return
     */
    public static LocalDate str2LocalDate(String time, String pattern) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Formats time string into localDateTime.
     *
     * @param time
     * @param pattern
     * @return
     */
    public static LocalDateTime str2LocalDateTime(String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static Date localDateTime2Date(LocalDateTime time) {
        // Asia/Shanghai  TimeZone.getDefault().getID()
        return new Date(toUnixTime(time));
    }

    public static long toUnixTime(LocalDateTime time) {
        return time.toEpochSecond(ZoneOffset.of("+08:00"));
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        // Asia/Shanghai as default zoneId
        return LocalDateTime.ofEpochSecond(date.getTime(), 0, ZoneOffset.of("+08:00"));
    }

    public static LocalDate getLocalDate(LocalDateTime time) {
        return time.toLocalDate();
    }

    public static LocalTime getLocalTime(LocalDateTime time) {
        return time.toLocalTime();
    }


    // 求两个时间差


    // 求时间先后

    /**
     * time1 compare to time2.
     * <p>
     * return <code>0</code> if time1 as the same as time2,
     * <code>1</code> time1 is after time2, otherwise <code>-1</code> time1 is before time2.
     * </p>
     *
     * <p>
     * class of {@link LocalDateTime}, {@link LocalDate}, {@link LocalTime}
     * have two methods <code>isAfter</code> and <code>isBefore</code> which can
     * </p>
     *
     * <p>
     * if the given date-time instanceof difference class, we can get the same of type,then compare
     * </p>
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int compareTo(LocalDateTime time1, LocalDateTime time2) {
        return time1.isAfter(time2) ? 1 : (time1.isBefore(time2) ? -1 : 0);
    }

    public static int compareTo(Date date1, Date date2) {
        return date1.after(date2) ? 1 : (date1.before(date2) ? -1 : 0);
    }


    // 指定时间间隔后的时间（前/后）


    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault().getID());
    }
}
