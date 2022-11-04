package me.opkarol.opc.api.time;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Range;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings({"all"})
public class OpTime extends GregorianCalendar {
    public OpTime(@Range(from = 1970, to = 10000) int year, @MagicConstant(intValues = {CALENDAR_MONTHS.JANUARY, CALENDAR_MONTHS.FEBRUARY, CALENDAR_MONTHS.MARCH, CALENDAR_MONTHS.APRIL, CALENDAR_MONTHS.MAY, CALENDAR_MONTHS.JUNE, CALENDAR_MONTHS.JULY, CALENDAR_MONTHS.AUGUST, CALENDAR_MONTHS.SEPTEMBER, CALENDAR_MONTHS.OCTOBER, CALENDAR_MONTHS.NOVEMBER, CALENDAR_MONTHS.DECEMBER}) @Range(from = 1, to = 12) int month, @Range(from = 0, to = 31) int day, @Range(from = 0, to = 23) int hour, @Range(from = 0, to = 60) int minute, @Range(from = 0, to = 60) int second) {
        super(year, month - 1, day, hour, minute, second);
    }

    public OpTime(@Range(from = 1970, to = 10000) int year, @MagicConstant(intValues = {CALENDAR_MONTHS.JANUARY, CALENDAR_MONTHS.FEBRUARY, CALENDAR_MONTHS.MARCH, CALENDAR_MONTHS.APRIL, CALENDAR_MONTHS.MAY, CALENDAR_MONTHS.JUNE, CALENDAR_MONTHS.JULY, CALENDAR_MONTHS.AUGUST, CALENDAR_MONTHS.SEPTEMBER, CALENDAR_MONTHS.OCTOBER, CALENDAR_MONTHS.NOVEMBER, CALENDAR_MONTHS.DECEMBER}) @Range(from = 1, to = 12) int month, @Range(from = 0, to = 31) int day, @Range(from = 0, to = 23) int hour, @Range(from = 0, to = 60) int minute) {
        super(year, month, day, hour, minute);
    }

    public OpTime(@Range(from = 1970, to = 10000) int year, @MagicConstant(intValues = {CALENDAR_MONTHS.JANUARY, CALENDAR_MONTHS.FEBRUARY, CALENDAR_MONTHS.MARCH, CALENDAR_MONTHS.APRIL, CALENDAR_MONTHS.MAY, CALENDAR_MONTHS.JUNE, CALENDAR_MONTHS.JULY, CALENDAR_MONTHS.AUGUST, CALENDAR_MONTHS.SEPTEMBER, CALENDAR_MONTHS.OCTOBER, CALENDAR_MONTHS.NOVEMBER, CALENDAR_MONTHS.DECEMBER}) @Range(from = 1, to = 12) int month, @Range(from = 0, to = 31) int day, @Range(from = 0, to = 23) int hour) {
        super(year, month, day, hour, 0);
    }

    public OpTime(@Range(from = 1970, to = 10000) int year, @MagicConstant(intValues = {CALENDAR_MONTHS.JANUARY, CALENDAR_MONTHS.FEBRUARY, CALENDAR_MONTHS.MARCH, CALENDAR_MONTHS.APRIL, CALENDAR_MONTHS.MAY, CALENDAR_MONTHS.JUNE, CALENDAR_MONTHS.JULY, CALENDAR_MONTHS.AUGUST, CALENDAR_MONTHS.SEPTEMBER, CALENDAR_MONTHS.OCTOBER, CALENDAR_MONTHS.NOVEMBER, CALENDAR_MONTHS.DECEMBER}) @Range(from = 1, to = 12) int month, @Range(from = 0, to = 31) int day) {
        super(year, month, day);
    }

    public OpTime(@Range(from = 1970, to = 10000) int year, @MagicConstant(intValues = {CALENDAR_MONTHS.JANUARY, CALENDAR_MONTHS.FEBRUARY, CALENDAR_MONTHS.MARCH, CALENDAR_MONTHS.APRIL, CALENDAR_MONTHS.MAY, CALENDAR_MONTHS.JUNE, CALENDAR_MONTHS.JULY, CALENDAR_MONTHS.AUGUST, CALENDAR_MONTHS.SEPTEMBER, CALENDAR_MONTHS.OCTOBER, CALENDAR_MONTHS.NOVEMBER, CALENDAR_MONTHS.DECEMBER}) @Range(from = 1, to = 12) int month) {
        super(year, month, 1);
    }

    public OpTime(@Range(from = 1970, to = 10000) int year) {
        super(year, Calendar.JANUARY, 1);
    }

    public OpTime(long millis) {
        super.setTimeInMillis(millis * 1000L);
    }

    public long toUnix() {
        return super.toInstant().getEpochSecond();
    }

    @Override
    public String toString() {
        return String.valueOf(toUnix());
    }
    
    public static class CALENDAR_MONTHS {
        public static final int JANUARY = 1;

        public static final int FEBRUARY = 2;

        public static final int MARCH = 3;

        public static final int APRIL = 4;

        public static final int MAY = 5;

        public static final int JUNE = 6;

        public static final int JULY = 7;

        public static final int AUGUST = 8;

        public static final int SEPTEMBER = 9;

        public static final int OCTOBER = 10;

        public static final int NOVEMBER = 11;

        public static final int DECEMBER = 12;
    }
}
