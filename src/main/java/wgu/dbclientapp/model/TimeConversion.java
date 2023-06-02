package wgu.dbclientapp.model;

import java.time.*;
import java.util.TimeZone;

/**
 * Time Conversion. All the time conversion methods are in here
 */
public class TimeConversion {

    /**
     * Conversion from local system time to UTC
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param min min
     * @return the converted time
     */
    public static String localToUTC(int year, int month, int day, int hour, int min) {
        LocalDate myLD = LocalDate.of(year, month, day);
        LocalTime myLt =  LocalTime.of(hour, min);
        LocalDateTime myLDT = LocalDateTime.of(myLD, myLt);
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);

        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        String dateTime = utcZDT.toLocalDate().toString() + " " + utcZDT.toLocalTime().toString();
        return dateTime+":00";
    }

    /**
     * Converts from UTC to Local System time
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param min min
     * @return the converted time./
     */
    public static String utcToLocal(int year, int month, int day, int hour, int min) {
        LocalDate utcLD = LocalDate.of(year, month, day);
        LocalTime utcLT =  LocalTime.of(hour, min);
        LocalDateTime utcLDT = LocalDateTime.of(utcLD, utcLT);
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.of(utcLDT, utcZoneId);

        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime localZDT = ZonedDateTime.ofInstant(utcZDT.toInstant(), myZoneId);
        String dateTime = localZDT.toLocalDate().toString() + " " + localZDT.toLocalTime().toString();

        return dateTime+":00";
    }

    /**
     * Convert from local system time to EST.
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param min min
     * @return Converted Time.
     */
    public static int localToEST(int year, int month, int day, int hour, int min) {
        LocalDate myLD = LocalDate.of(year, month, day);
        LocalTime myLt =  LocalTime.of(hour, min);
        LocalDateTime myLDT = LocalDateTime.of(myLD, myLt);
        ZoneId myZoneId = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(myLDT, myZoneId);
        ZoneId estZoneId = ZoneId.of("EST",ZoneId.SHORT_IDS);
        ZonedDateTime estZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), estZoneId);
        int estHour = estZDT.getHour();
        return estHour;
    }
}
