package com.nidjo123.treasury.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for different operations with LocalDate objects. Has functions for converting
 * Strings to LocalDates and vice versa.
 */
public class DateUtil {

    /**
     * Pattern for parsing date.
     */
    public static final String DATE_PATTERN = "dd.MM.yyyy.";

    /**
     * Pattern for date with abbreviated month.
     */
    public static final String DATE_ABBREV_PATTERN = "dd. LLL. yyyy.";

    /**
     * Instance of date formatter for parsing and formatting dates with given date pattern.
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.forLanguageTag("hr"));

    public static final DateTimeFormatter DATE_ABBREV_FOMATTER = DateTimeFormatter.ofPattern(DATE_ABBREV_PATTERN, Locale.forLanguageTag("hr"));

    /**
     * Converts LocalDate object ot a well formed String.
     *
     * @param date LocalDate object to convert.
     * @return Returns a well formated String representation of given LocalDate object.
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }

        return DATE_FORMATTER.format(date);
    }

    /**
     * Returns LocalDate object made from given date string. Returns null if given string is not valid.
     *
     * @param dateString String representation of the date.
     * @return LocalDate object representing date in the String or null if given String could not be parsed.
     */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Returns formatted string representing provided date, but with abbreviated month name in form dd. LLL. yyyy.
     *
     * @param date date to format.
     * @return Returns formatted string representing provided date, but with abbreviated month name in form dd. LLL. yyyy.
     */
    public static String formatAbbrevMonth(LocalDate date) {
        return DATE_ABBREV_FOMATTER.format(date);
    }

    /**
     * Returns LocalDate converted from provided Date object.
     *
     * @param date Date object to convert.
     * @return Returns instance of LocalDate converted from provided Date object.
     */
    public static LocalDate fromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Returns {@link Date} converted from {@link LocalDate} object.
     *
     * @param date LocalDate object to convert.
     * @return Returns instance of {@link Date} converted from provided {@link LocalDate} object.
     */
    public static Date fromLocalDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Checks if given date string is valid.
     *
     * @param dateString Date string to check.
     * @return Returns true if given date string is a valid representation of the date, false otherwise.
     */
    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }
}
