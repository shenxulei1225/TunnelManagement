package com.cheers.framework.common.util.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    /**
     * 时区 - 默认
     */
    public static final String TIME_ZONE_DEFAULT = "GMT+8";

    /**
     * 秒转换成毫秒
     */
    public static final long SECOND_MILLIS = 1000;

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_HOUR_MINUTE_SECOND = "HH:mm:ss";

    /**
     * 将 LocalDateTime 转换成 Date
     *
     * @param date LocalDateTime
     * @return LocalDateTime
     */
    public static Date of(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DateUtil.date(date);
    }

    /**
     * 将 Date 转换成 LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime of(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.toLocalDateTime(date);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDate nowDate() {
        return LocalDate.now();
    }

    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return LocalDateTimeUtil.isIn(now(), startTime, endTime);
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDate startTime, LocalDate endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return LocalDateTimeUtil.isIn(now(), startTime.atStartOfDay(), endTime.atStartOfDay());
    }

    /**
     * 判断是否在同一天
     *
     * @param time1 时间 1
     * @param time2 时间 2
     * @return 是否
     */
    public static boolean isSameDay(LocalDateTime time1, LocalDateTime time2) {
        return LocalDateTimeUtil.isSameDay(time1, time2);
    }

    /**
     * 判断是否为同一个月
     *
     * @param time1 时间 1
     * @param time2 时间 2
     * @return 是否
     */
    public static boolean isSameMonth(LocalDateTime time1, LocalDateTime time2) {
        return time1.getMonth() == time2.getMonth()
                && time1.getYear() == time2.getYear();
    }

    /**
     * 获得开始时间
     *
     * @param date 日期
     * @return 开始时间
     */
    public static LocalDateTime getStartTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    /**
     * 获得结束时间
     *
     * @param date 日期
     * @return 结束时间
     */
    public static LocalDateTime getEndTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 获得月份的开始时间
     *
     * @param date 日期
     * @return 开始时间
     */
    public static LocalDateTime getStartTimeOfMonth(LocalDate date) {
        return LocalDateTime.of(date.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 获得月份的结束时间
     *
     * @param date 日期
     * @return 结束时间
     */
    public static LocalDateTime getEndTimeOfMonth(LocalDate date) {
        return LocalDateTime.of(date.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /**
     * 获取当前时间与指定时间的差值
     *
     * @param date 指定时间
     * @return 秒数
     */
    public static long getDifferSecond(LocalDateTime date) {
        return getDifferSecond(date, now());
    }

    /**
     * 获取两个时间的差值
     *
     * @param start 开始时间
     * @param end 结束时间
     * @return 秒数
     */
    public static long getDifferSecond(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).getSeconds();
    }

    /**
     * 判断指定时间是否在当前时间之前
     *
     * @param date 指定时间
     * @return 是否
     */
    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(now());
    }

    /**
     * 判断指定时间是否在当前时间之后
     *
     * @param date 指定时间
     * @return 是否
     */
    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(now());
    }

    /**
     * 判断指定时间是否在当前时间之后
     *
     * @param date 指定时间
     * @return 是否
     */
    public static boolean afterNow(LocalDate date) {
        return date.isAfter(nowDate());
    }

    /**
     * 增加指定天数
     *
     * @param date 日期
     * @param days 天数
     * @return 增加天数后的日期
     */
    public static LocalDateTime addDays(LocalDateTime date, int days) {
        return date.plusDays(days);
    }

    /**
     * 增加指定月数
     *
     * @param date 日期
     * @param months 月数
     * @return 增加月数后的日期
     */
    public static LocalDateTime addMonths(LocalDateTime date, int months) {
        return date.plusMonths(months);
    }

    /**
     * 增加指定年数
     *
     * @param date 日期
     * @param years 年数
     * @return 增加年数后的日期
     */
    public static LocalDateTime addYears(LocalDateTime date, int years) {
        return date.plusYears(years);
    }

} 